package dge.dge_equiv_api.application.pedido.mapper;

import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.infrastructure.primary.EqvTInstEnsino;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequerente;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    // Pedido mappings
    EqvTPedido toEntity(EqvtPedidoDTO dto);
    EqvtPedidoDTO toDTO(EqvTPedido entity);
    List<EqvtPedidoDTO> toDTOList(List<EqvTPedido> entities);

    // Requerente mappings
    @Mapping(target = "habilitacao", expression = "java(dto.getHabilitacao() != null ? Integer.valueOf(dto.getHabilitacao()) : null)")
    EqvTRequerente toRequerenteEntity(EqvTRequerenteDTO dto);

    @Mapping(target = "habilitacao", expression = "java(entity.getHabilitacao() != null ? String.valueOf(entity.getHabilitacao()) : null)")
    EqvTRequerenteDTO toRequerenteDTO(EqvTRequerente entity);

    // Instituição de Ensino mappings
    EqvTInstEnsino toInstEnsinoEntity(EqvTInstEnsinoDTO dto);
    EqvTInstEnsinoDTO toInstEnsinoDTO(EqvTInstEnsino entity);

    // Requisição mappings
    EqvTRequisicao toRequisicaoEntity(EqvTRequisicaoDTO dto);
    EqvTRequisicaoDTO toRequisicaoDTO(EqvTRequisicao entity);
}