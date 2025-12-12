package dge.dge_equiv_api.application.reclamacao.mapper;

import dge.dge_equiv_api.application.reclamacao.dto.EqvTReclamacaoDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTReclamacao;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EqvTReclamacaoMapper {

    public EqvTReclamacaoDTO toDto(EqvTReclamacao entity) {
        if (entity == null) {
            return null;
        }

        EqvTReclamacaoDTO dto = new EqvTReclamacaoDTO();
        dto.setId(entity.getId());
        dto.setObservacao(entity.getObservacao());
        dto.setDecisao(entity.getDecisao());
        dto.setAnexo(entity.getAnexo());
        dto.setUserCreate(entity.getUserCreate());
        dto.setUserUpdate(entity.getUserUpdate());
        dto.setDateCreate(entity.getDateCreate());
        dto.setDateUpdate(entity.getDateUpdate());

        // Extrair IDs dos relacionamentos
        if (entity.getIdPedido() != null) {
            dto.setIdPedido(Long.valueOf(entity.getIdPedido().getId()));
        }
        if (entity.getIdRequisicao() != null) {
            dto.setIdRequisicao(Long.valueOf(entity.getIdRequisicao().getId()));
        }

        return dto;
    }

    public EqvTReclamacao toEntity(EqvTReclamacaoDTO dto, EqvTPedido pedido, EqvTRequisicao requisicao) {
        if (dto == null) {
            return null;
        }

        EqvTReclamacao entity = new EqvTReclamacao();
        entity.setId(dto.getId());
        entity.setIdPedido(pedido);
        entity.setIdRequisicao(requisicao);
        entity.setObservacao(dto.getObservacao());
        entity.setDecisao(dto.getDecisao());
        entity.setAnexo(dto.getAnexo());
        entity.setUserCreate(dto.getUserCreate());
        entity.setUserUpdate(dto.getUserUpdate());
        entity.setDateCreate(dto.getDateCreate());
        entity.setDateUpdate(dto.getDateUpdate());

        return entity;
    }

    public EqvTReclamacao toEntity(EqvTReclamacaoDTO dto) {
        return toEntity(dto, null, null);
    }

    public EqvTReclamacao updateEntityFromDto(EqvTReclamacao entity, EqvTReclamacaoDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Não atualizamos o ID
        if (dto.getObservacao() != null) {
            entity.setObservacao(dto.getObservacao());
        }
        if (dto.getDecisao() != null) {
            entity.setDecisao(dto.getDecisao());
        }
        if (dto.getAnexo() != null) {
            entity.setAnexo(dto.getAnexo());
        }
        if (dto.getUserUpdate() != null) {
            entity.setUserUpdate(dto.getUserUpdate());
        }
        if (dto.getDateUpdate() != null) {
            entity.setDateUpdate(dto.getDateUpdate());
        } else {
            entity.setDateUpdate(LocalDate.now());
        }

        return entity;
    }
}