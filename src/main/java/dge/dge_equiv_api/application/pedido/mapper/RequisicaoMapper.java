package dge.dge_equiv_api.application.pedido.mapper;

import dge.dge_equiv_api.application.pedido.dto.EqvTRequisicaoDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import org.springframework.stereotype.Component;

@Component
public class RequisicaoMapper {

    public EqvTRequisicao toEntity(EqvTRequisicaoDTO dto) {
        if (dto == null) {
            return new EqvTRequisicao();
        }
        EqvTRequisicao requisicao = new EqvTRequisicao();
        if (dto.getNProcesso() != null) {
            requisicao.setNProcesso(dto.getNProcesso());
        }
        requisicao.setStatus(dto.getStatus());
        requisicao.setEtapa(dto.getEtapa());
        requisicao.setDataUpdate(dto.getDataUpdate());
        requisicao.setUserCreate(dto.getUserCreate());
        requisicao.setUserUpdate(dto.getUserUpdate());
        return requisicao;
    }

    public EqvTRequisicaoDTO toDTO(EqvTRequisicao requisicao) {
        EqvTRequisicaoDTO dto = new EqvTRequisicaoDTO();
        dto.setId(requisicao.getId());
        dto.setNProcesso(requisicao.getNProcesso());
        dto.setStatus(requisicao.getStatus());
        dto.setEtapa(requisicao.getEtapa());
        dto.setDataCreate(requisicao.getDataCreate());
        dto.setDataUpdate(requisicao.getDataUpdate());
        return dto;
    }
}