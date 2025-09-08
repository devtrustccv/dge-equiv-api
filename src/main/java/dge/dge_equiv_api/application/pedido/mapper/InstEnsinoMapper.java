package dge.dge_equiv_api.application.pedido.mapper;

import dge.dge_equiv_api.application.pedido.dto.EqvTInstEnsinoDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTInstEnsino;
import org.springframework.stereotype.Component;

@Component
public class InstEnsinoMapper {

    public EqvTInstEnsino toEntity(EqvTInstEnsinoDTO dto) {
        EqvTInstEnsino instEnsino = new EqvTInstEnsino();
        instEnsino.setNome(dto.getNome());
        instEnsino.setPais(dto.getPais());
        instEnsino.setDateCreate(dto.getDateCreate());
        return instEnsino;
    }

    public EqvTInstEnsinoDTO toDTO(EqvTInstEnsino instEnsino) {
        EqvTInstEnsinoDTO dto = new EqvTInstEnsinoDTO();
        dto.setId(instEnsino.getId());
        dto.setNome(instEnsino.getNome());
        dto.setPais(instEnsino.getPais());
        dto.setDateCreate(instEnsino.getDateCreate());
        return dto;
    }
}