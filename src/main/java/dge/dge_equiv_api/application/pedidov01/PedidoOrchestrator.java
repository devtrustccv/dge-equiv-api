package dge.dge_equiv_api.application.pedidov01;

import dge.dge_equiv_api.application.pedido.dto.EqvTRequerenteDTO;
import dge.dge_equiv_api.application.pedido.dto.EqvTRequisicaoDTO;
import dge.dge_equiv_api.application.pedido.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.application.pedido.dto.PortalPedidosDTO;
import dge.dge_equiv_api.application.pedidov01.mapper.PedidoMapper;
import dge.dge_equiv_api.application.process.service.ProcessService;
import dge.dge_equiv_api.domain.pedido.business.EqvTPedidoBusinessService;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.EqvTPagamento;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTRequisicaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoOrchestrator {

    private final EqvTPedidoBusinessService pedidoBusinessService;
    private final ProcessService processService;
    private final PedidoMapper pedidoMapper;
    private final EqvTRequisicaoRepository requisicaoRepository;
    @Value("${link.mf.duc}")
    private String mfkink;

    @Value("${link.api.duc.check}")
    private String ducCheck;

    @Value("${link.report.integration.sigof.duc}")
    private String reporterDuc;

    @Transactional
    public List<EqvtPedidoDTO> criarLotePedidosCompleto(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId
    ) {
        String processInstanceId = null;
        EqvTRequisicao requisicaoSalva = null;

        try {
            log.info("Iniciando criação de lote de pedidos para pessoaId: {}", pessoaId);

            //  Valida dados básicos
            if (pedidosDTO == null || pedidosDTO.isEmpty()) {
                throw new IllegalArgumentException("Lista de pedidos não pode ser nula ou vazia");
            }
            if (requerenteDTO == null) {
                throw new IllegalArgumentException("RequerenteDTO não pode ser nulo");
            }
            if (pessoaId == null) {
                throw new IllegalArgumentException("pessoaId não pode ser nulo");
            }

            pedidoBusinessService.validarDadosCriacao(pedidosDTO, requerenteDTO);

            //  Processa requerente
            var requerente = pedidoBusinessService.processarRequerente(requerenteDTO, pessoaId);
            log.info("Requerente processado: {}", requerente.getId());

            //  Processa instituições
            var instituicoes = pedidoBusinessService.processarInstituicoesEnsino(pedidosDTO);
            log.info("Instituições processadas: {}", instituicoes.size());

            //  CORREÇÃO: Garantir que requisicaoDTO não seja null
            if (requisicaoDTO == null) {
                log.warn("requisicaoDTO é null, criando DTO padrão");
                requisicaoDTO = criarRequisicaoDTOPadrao(pessoaId);
            }
            log.info("Usando requisicaoDTO: {}", requisicaoDTO);

            //  Processa requisição
            var requisicao = pedidoBusinessService.processarRequisicao(requisicaoDTO, pessoaId);
            requisicaoSalva = requisicao; // Guardar referência para possível rollback
            log.info("Requisição processada: {}", requisicao.getId());

            //  Inicia processo externo
            processInstanceId = pedidoBusinessService.iniciarProcesso(requerente, pedidosDTO, instituicoes);
            log.info("Processo externo iniciado: {}", processInstanceId);

            //  Atualiza requisição com número do processo
            requisicao.setNProcesso(Integer.valueOf(processInstanceId));
            requisicao = requisicaoRepository.save(requisicao); // Salvar atualização
            log.info("Requisição atualizada com nProcesso: {}", requisicao.getNProcesso());

            //  Gera DUC
            var duc = pedidoBusinessService.gerarDUC(requerenteDTO, requisicao);
            log.info("DUC gerado: {}", duc.getNuDuc());

            //  Cria pedidos
            List<EqvTPedido> pedidosSalvos = pedidoBusinessService.criarLotePedidos(
                    pedidosDTO, requisicao, requerente, instituicoes
            );
            log.info("Pedidos criados: {}", pedidosSalvos.size());

            //  Salva documentos
            pedidoBusinessService.salvarDocumentosDosPedidos(pedidosDTO, pedidosSalvos);
            log.info("Documentos salvos");

            //  Cria acompanhamento
            pedidoBusinessService.criarAcompanhamento(requisicao, pedidosSalvos, pessoaId, duc);
            log.info("Acompanhamento criado");

            // Envia e-mail com DUC
            pedidoBusinessService.enviarEmailDuc(duc, requerente, requisicao, pedidosSalvos);
            log.info("Email enviado");

            List<EqvtPedidoDTO> result = pedidosSalvos.stream()
                    .map(pedidoMapper::toDTO)
                    .collect(Collectors.toList());

            //preencher dados pagamento
            preencherDadosDUC(result, duc);

            log.info("Lote de pedidos criado com sucesso. Processo: {}", processInstanceId);


            return result;

        } catch (Exception e) {
            log.error("Erro ao criar lote de pedidos");

            // Rollback: remove processo iniciado E requisição se necessário
            if (processInstanceId != null) {
                try {
                    log.warn("Eliminando processo externo {}", processInstanceId);
                    processService.deleteProcess(processInstanceId);
                } catch (Exception ex) {
                    log.error("Falha ao deletar processo externo {}", processInstanceId, ex);
                }
            }

            // Se a requisição foi salva mas houve erro depois, deletar também
            if (requisicaoSalva != null && requisicaoSalva.getId() != null) {
                try {
                    log.warn("Eliminando requisição {} devido a erro no processo", requisicaoSalva.getId());
                    requisicaoRepository.delete(requisicaoSalva);
                } catch (Exception ex) {
                    log.error("Falha ao deletar requisição {}", requisicaoSalva.getId(), ex);
                }
            }

            throw new BusinessException("Erro ao criar lote de pedidos: " + e.getMessage());
        }
    }


    // No PedidoOrchestrator.java
    public List<EqvtPedidoDTO> updatePedidosByRequisicaoId(Integer requisicaoId, PortalPedidosDTO portalPedidosDTO) {

        return pedidoBusinessService.updatePedidosByRequisicaoId(requisicaoId, portalPedidosDTO);
    }

    /**
     * Cria um DTO de requisição padrão quando não é fornecido
     */
    private EqvTRequisicaoDTO criarRequisicaoDTOPadrao(Integer pessoaId) {
        return EqvTRequisicaoDTO.builder()
                .dataCreate(LocalDate.now())
                .status(1)
                .etapa(1)
                .idPessoa(pessoaId)
                .userCreate(pessoaId)
                .build();
    }

    private void preencherDadosDUC(List<EqvtPedidoDTO> pedidosDTO, EqvTPagamento duc) {
        if (duc == null) {
            log.warn("DUC é null, não é possível preencher dados do pagamento");
            return;
        }

        for (EqvtPedidoDTO dto : pedidosDTO) {
            String urlPagamento = mfkink + duc.getEntidade()
                    + "&referencia=" + duc.getReferencia()
                    + "&montante=" + duc.getTotal()
                    + "&call_back_url=" + ducCheck + duc.getNuDuc();

            String linkverduc = reporterDuc + duc.getNuDuc();

            dto.setUrlDucPagamento(urlPagamento);
            dto.setNuDuc(duc.getNuDuc() != null ? duc.getNuDuc().toString() : null);
            dto.setEntidade(duc.getEntidade());
            dto.setReferencia(duc.getReferencia() != null ? duc.getReferencia().toString() : null);
            dto.setValorDuc(duc.getTotal() != null ? duc.getTotal().toString() : null);
            dto.setVerduc(linkverduc);

            log.debug("Dados do DUC preenchidos para o pedido: {}", dto.getId());
        }

        log.info("Dados do DUC preenchidos para {} pedidos", pedidosDTO.size());
    }
}