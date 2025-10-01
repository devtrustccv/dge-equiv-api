package dge.dge_equiv_api.application.pedido.mapper;

import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.infrastructure.primary.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(target = "documentos", ignore = true)
    @Mapping(target = "documentosresp", ignore = true)
    @Mapping(target = "urlDucPagamento", ignore = true)
    @Mapping(target = "nuDuc", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "referencia", ignore = true)
    @Mapping(target = "verduc", ignore = true)
    EqvtPedidoDTO toDto(EqvTPedido pedido);

    List<EqvtPedidoDTO> toDtoList(List<EqvTPedido> pedidos);

    EqvTRequerenteDTO toRequerenteDto(EqvTRequerente requerente);
    EqvTInstEnsinoDTO toInstEnsinoDto(EqvTInstEnsino instEnsino);
    EqvTRequisicaoDTO toRequisicaoDto(EqvTRequisicao requisicao);

    @Mapping(target = "despacho", source = "despacho")
    PedidoSimplesDTO toSimplesDto(EqvtPedidoDTO pedido);

    EqvTPedido toEntity(EqvtPedidoDTO dto);
    EqvTRequerente toRequerenteEntity(EqvTRequerenteDTO dto);
    EqvTInstEnsino toInstEnsinoEntity(EqvTInstEnsinoDTO dto);
    EqvTRequisicao toRequisicaoEntity(EqvTRequisicaoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePedidoFromDto(EqvtPedidoDTO dto, @MappingTarget EqvTPedido pedido);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequerenteFromDto(EqvTRequerenteDTO dto, @MappingTarget EqvTRequerente requerente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateInstEnsinoFromDto(EqvTInstEnsinoDTO dto, @MappingTarget EqvTInstEnsino instEnsino);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequisicaoFromDto(EqvTRequisicaoDTO dto, @MappingTarget EqvTRequisicao requisicao);
}