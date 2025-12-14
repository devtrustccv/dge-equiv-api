package dge.dge_equiv_api.application.motivo_retidicado.service;

import dge.dge_equiv_api.application.motivo_retidicado.dto.MotivoRetificacaoResponseDTO;
import dge.dge_equiv_api.application.motivo_retidicado.mapper.MotivoRetificacaoMapper;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import dge.dge_equiv_api.infrastructure.primary.EqvtTDecisaoVp;

import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTRequisicaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MotivoRetificacaoService {

    private final EqvTPedidoRepository pedidoRepository;
    private final MotivoRetificacaoMapper motivoRetificacaoMapper;
    private final EqvTRequisicaoRepository requisicaoRepository;

    /**
     * Método principal usando MapStruct
     */
    public List<MotivoRetificacaoResponseDTO> buscarMotivoRetificacaoPorProcesso(Integer numeroProcesso) {
        log.info("Buscando motivo de retificação para o processo: {}", numeroProcesso);

        // Query otimizada
        List<EqvtTDecisaoVp> decisoes = pedidoRepository
                .findDecisoesVpComMotivoByNumeroProcesso(numeroProcesso);

        if (decisoes.isEmpty()) {
            log.info("Nenhuma decisão com motivo de retificação encontrada para o processo: {}", numeroProcesso);
            return List.of();
        }

        log.info("Encontradas {} decisão(ões) com motivo de retificação", decisoes.size());

        // Usando MapStruct para mapeamento
        return decisoes.stream()
                .map(motivoRetificacaoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Método alternativo usando MapStruct com lista
     */
    public List<MotivoRetificacaoResponseDTO> buscarMotivoRetificacaoPorProcessoOtimizado(Integer numeroProcesso) {
        log.info("Buscando motivo de retificação (otimizado) para o processo: {}", numeroProcesso);

        List<EqvtTDecisaoVp> decisoes = pedidoRepository
                .findDecisoesVpComMotivoByNumeroProcesso(numeroProcesso);

        // Usando MapStruct com método de lista
        return motivoRetificacaoMapper.toDtoList(decisoes);
    }

    public boolean verificarPedidosEmAlterSolic(String numeroProcesso) {
        try {
            // Converter número do processo para Integer
            Integer numeroProcessoInt;
            try {
                numeroProcessoInt = Integer.valueOf(numeroProcesso);
            } catch (NumberFormatException e) {
                log.warn("Número de processo inválido: {}", numeroProcesso);
                return false;
            }

            // Buscar a requisição pelo número do processo
            EqvTRequisicao requisicao = requisicaoRepository.findByNProcesso(numeroProcessoInt)
                    .orElse(null);

            if (requisicao == null) {
                log.warn("Processo não encontrado: {}", numeroProcesso);
                return false;
            }

            // Buscar todos os pedidos da requisição
            List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

            if (pedidos.isEmpty()) {
                return false;
            }

            // Verificar se algum pedido tem etapa = "alter_solic"
            for (EqvTPedido pedido : pedidos) {
                if ("alter_solic".equalsIgnoreCase(pedido.getEtapa())) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            log.error("Erro ao verificar etapa alter_solic para processo {}: {}",
                    numeroProcesso, e.getMessage());
            return false;
        }
    }

    public boolean verificarPedidosReclamado(String numeroProcesso) {
        try {
            // Converter número do processo para Integer
            Integer numeroProcessoInt;
            try {
                numeroProcessoInt = Integer.valueOf(numeroProcesso);
            } catch (NumberFormatException e) {
                log.warn("Número de processo inválido: {}", numeroProcesso);
                return false;
            }

            // Buscar a requisição pelo número do processo
            EqvTRequisicao requisicao = requisicaoRepository.findByNProcesso(numeroProcessoInt)
                    .orElse(null);

            if (requisicao == null) {
                log.warn("Processo não encontrado: {}", numeroProcesso);
                return false;
            }

            // Buscar todos os pedidos da requisição
            List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

            if (pedidos.isEmpty()) {
                return false;
            }

            // Verificar se algum pedido tem etapa = "alter_solic"
            for (EqvTPedido pedido : pedidos) {
                if ("reclamacao".equalsIgnoreCase(pedido.getEtapa())) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            log.error("Erro ao verificar etapa alter_solic para processo {}: {}",
                    numeroProcesso, e.getMessage());
            return false;
        }
    }
}