package dge.dge_equiv_api.application.motivo_retidicado.service;

import dge.dge_equiv_api.application.motivo_retidicado.dto.MotivoRetificacaoResponseDTO;
import dge.dge_equiv_api.application.motivo_retidicado.mapper.MotivoRetificacaoMapper;
import dge.dge_equiv_api.infrastructure.primary.EqvtTDecisaoVp;

import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
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
}