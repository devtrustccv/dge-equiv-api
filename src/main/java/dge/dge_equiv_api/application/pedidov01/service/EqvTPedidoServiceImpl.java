package dge.dge_equiv_api.application.pedidov01.service;

import dge.dge_equiv_api.application.certificado.config.ReporterConfig;
import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
import dge.dge_equiv_api.application.document.service.DocumentService;
import dge.dge_equiv_api.application.geografia.service.GlobalGeografiaService;
import dge.dge_equiv_api.application.pedidov01.PedidoOrchestrator;
import dge.dge_equiv_api.application.pedidov01.dto.*;
import dge.dge_equiv_api.application.pedidov01.mapper.PedidoMapper;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTRequisicaoRepository;
import dge.dge_equiv_api.Utils.AESUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EqvTPedidoServiceImpl implements EqvTPedidoService {

    private final PedidoOrchestrator pedidoOrchestrator;
    private final EqvTPedidoRepository pedidoRepository;
    private final EqvTRequisicaoRepository requisicaoRepository;
    private final DocumentService documentService;
    private final PedidoMapper pedidoMapper;
    private final ReporterConfig reporterConfig;
    private final GlobalGeografiaService globalGeografiaService;

    /**
     * Cria o lote de pedidos com requisição e requerente únicos.
     */
    @Override
    @Transactional
    public List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId) {

        return pedidoOrchestrator.criarLotePedidosCompleto(
                pedidosDTO,
                requisicaoDTO,
                requerenteDTO,
                pessoaId
        );
    }

    /**
     * Atualiza o lote de pedidos com requisição e requerente únicos.
     */
    @Override
    @Transactional
    public List<EqvtPedidoDTO> updateLotePedidosComRequisicao(
            Integer requisicaoId,
            PortalPedidosDTO portalPedidosDTO, String numProcesso) {

        log.info("Iniciando atualização de pedidos para requisição ID: {}", requisicaoId);

        // Validar dados de entrada
        if (portalPedidosDTO == null) {
            throw new IllegalArgumentException("PortalPedidosDTO não pode ser nulo");
        }

        if (portalPedidosDTO.getPedidos() == null || portalPedidosDTO.getPedidos().isEmpty()) {
            throw new IllegalArgumentException("Lista de pedidos não pode ser nula ou vazia");
        }

        try {
            // Delegar a lógica de atualização para o business service
            List<EqvtPedidoDTO> pedidosAtualizados = pedidoOrchestrator.updatePedidosByRequisicaoId(
                    requisicaoId,
                    portalPedidosDTO



            );

            log.info("Atualização concluída para requisição ID: {} - {} pedidos atualizados",
                    requisicaoId, pedidosAtualizados.size());

            return pedidosAtualizados;

        } catch (Exception e) {
            log.error("Erro ao atualizar pedidos para requisição ID: {}", requisicaoId, e);
            throw new BusinessException("Erro ao atualizar pedidos: " + e.getMessage());
        }
    }

    /**
     * Retorna os pedidos de uma requisição, incluindo documentos.
     */
    @Override
    public List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId) {
        EqvTRequisicao requisicao = requisicaoRepository.findById(requisicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));

        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

        return pedidos.stream().map(pedido -> {
            EqvtPedidoDTO dto = pedidoMapper.toDTO(pedido);

            List<DocumentoResponseDTO> documentos = documentService.getDocumentosPorRelacao(
                    pedido.getId(),
                    "SOLITACAO",
                    "equiv"
            );

            dto.setDocumentosresp(documentos);
            log.info("Encontrados {} documentos para o pedido {}", documentos.size(), pedido.getId());

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Retorna os pedidos de uma requisição sem documentos.
     */
    @Override
    public List<EqvtPedidoDTO> findPedidosByRequisicaoId(Integer idRequisicao) {
        return pedidoRepository.findByRequisicaoId(idRequisicao).stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Monta o DTO do certificado de equivalência para um pedido.
     */
    @Override
    public CertificadoEquivalenciaDTO montarCertificado(EqvtPedidoDTO pedidoDTO) {
        CertificadoEquivalenciaDTO dto = new CertificadoEquivalenciaDTO();

        String idCriptografado = AESUtil.encrypt(pedidoDTO.getId().toString());
        String url = reporterConfig.getReporterEqvUrl() + "equiv/declaracao_equivalencia/" + idCriptografado;

        dto.setFormacaoOriginal(pedidoDTO.getFormacaoProf());
        dto.setEntidadeOriginal(pedidoDTO.getInstEnsino() != null ? pedidoDTO.getInstEnsino().getNome() : null);
        dto.setEquivalencia("Equivalência Profissional");
        dto.setNivelQualificacao(pedidoDTO.getNivel() != null ? pedidoDTO.getNivel().toString() : null);

        if ("5".equals(pedidoDTO.getDespacho())) {
            dto.setUrl(url);
        }

        dto.setDataEmissao(java.time.LocalDate.now());
        dto.setEntidadeEmissora("DGERT - Direção-Geral do Emprego e das Relações de Trabalho");
        dto.setNumeroProcesso(pedidoDTO.getRequisicao() != null && pedidoDTO.getRequisicao().getNProcesso() != null
                ? pedidoDTO.getRequisicao().getNProcesso().toString()
                : null);

        if (pedidoDTO.getInstEnsino() != null && pedidoDTO.getInstEnsino().getPais() != null) {
            dto.setPaisOrigem(globalGeografiaService.buscarNomePorCodigoPais(pedidoDTO.getInstEnsino().getPais()));
        }

        return dto;
    }

    @Override
    public List<CertificadoEquivalenciaDTO> montarCertificadosPorRequisicao(Integer idRequisicao) {
        // Pode ser implementado posteriormente chamando montarCertificado para cada pedido
        return List.of();
    }

    @Override
    public PedidoSimplesDTO convertToSimplesDTO(EqvtPedidoDTO pedido) {
        // Pode ser implementado conforme necessidade
        return null;
    }
}