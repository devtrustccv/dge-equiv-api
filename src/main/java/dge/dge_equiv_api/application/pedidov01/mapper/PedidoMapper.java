package dge.dge_equiv_api.application.pedidov01.mapper;

import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.infrastructure.primary.*;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {LocalDate.class})
public interface PedidoMapper {

    // ===================== ENTITY -> DTO =====================
    @Mapping(target = "documentos", ignore = true)
    @Mapping(target = "documentosresp", ignore = true)
    @Mapping(target = "urlDucPagamento",ignore = true )
    @Mapping(target = "nuDuc", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "referencia", ignore = true)
    @Mapping(target = "ValorDuc", ignore = true)
    @Mapping(target = "verduc", ignore = true)
    @Mapping(target = "despacho", source = "despacho")
    @Mapping(target = "requerente", source = "requerente") 
    @Mapping(target = "instEnsino", source = "instEnsino")
    @Mapping(target = "requisicao", source = "requisicao")
    EqvtPedidoDTO toDTO(EqvTPedido pedido);

    List<EqvtPedidoDTO> toDTOList(List<EqvTPedido> pedidos);

    // ===================== Requerente =====================
    @Mapping(target = "habilitacao", expression = "java(integerToString(requerente.getHabilitacao()))")
    EqvTRequerenteDTO toRequerenteDTO(EqvTRequerente requerente);

    // ===================== Instituição =====================
    EqvTInstEnsinoDTO toInstEnsinoDTO(EqvTInstEnsino instEnsino);

    // ===================== Requisição =====================
    EqvTRequisicaoDTO toRequisicaoDTO(EqvTRequisicao requisicao);

    // ===================== DTO -> ENTITY =====================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requerente", ignore = true)
    @Mapping(target = "instEnsino", ignore = true)
    @Mapping(target = "requisicao", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "etapa", ignore = true)
    @Mapping(target = "despacho", source = "despacho")
    @Mapping(target = "dataDespacho", source = "dataDespacho")
    EqvTPedido toEntity(EqvtPedidoDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreate", ignore = true)
    @Mapping(target = "idPessoa", ignore = true)
    @Mapping(target = "habilitacao", expression = "java(stringToInteger(dto.getHabilitacao()))")
    @Mapping(target = "dataUpdate", ignore = true)
    @Mapping(target = "userUpdate", ignore = true)
    EqvTRequerente toRequerenteEntity(EqvTRequerenteDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreate", ignore = true)
    @Mapping(target = "status", ignore = true)
    EqvTInstEnsino toInstEnsinoEntity(EqvTInstEnsinoDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCreate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "etapa", ignore = true)
    @Mapping(target = "dataUpdate", ignore = true)
    @Mapping(target = "userUpdate", ignore = true)
    @Mapping(target = "idPessoa", ignore = true)
    @Mapping(target = "NProcesso",ignore = true)
    EqvTRequisicao toRequisicaoEntity(EqvTRequisicaoDTO dto);

    // ===================== MÉTODOS AUXILIARES =====================

    /**
     * Converte String para Integer de forma segura
     */
    default Integer stringToInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converte Integer para String de forma segura
     */
    default String integerToString(Integer value) {
        return value != null ? value.toString() : null;
    }

    /**
     * Converte String para BigDecimal de forma segura
     */
    default BigDecimal stringToBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Método seguro para mapear requisição - nunca retorna null
     */
    default EqvTRequisicao safeToRequisicaoEntity(EqvTRequisicaoDTO dto) {
        if (dto == null) {
            EqvTRequisicao requisicao = new EqvTRequisicao();
            requisicao.setStatus(1);
            requisicao.setEtapa(1);
            requisicao.setDataCreate(LocalDate.now());
            return requisicao;
        }

        EqvTRequisicao requisicao = toRequisicaoEntity(dto);
        if (requisicao == null) {
            requisicao = new EqvTRequisicao();
        }

        // Garantir valores padrão
        if (requisicao.getStatus() == null) {
            requisicao.setStatus(1);
        }
        if (requisicao.getEtapa() == null) {
            requisicao.setEtapa(1);
        }
        if (requisicao.getDataCreate() == null) {
            requisicao.setDataCreate(LocalDate.now());
        }

        return requisicao;
    }

    /**
     * Método seguro para mapear requerente - nunca retorna null
     */
    default EqvTRequerente safeToRequerenteEntity(EqvTRequerenteDTO dto) {
        if (dto == null) {
            return new EqvTRequerente();
        }

        EqvTRequerente requerente = toRequerenteEntity(dto);
        return requerente != null ? requerente : new EqvTRequerente();
    }

    /**
     * Método seguro para mapear instituição - nunca retorna null
     */
    default EqvTInstEnsino safeToInstEnsinoEntity(EqvTInstEnsinoDTO dto) {
        if (dto == null) {
            return new EqvTInstEnsino();
        }

        EqvTInstEnsino instEnsino = toInstEnsinoEntity(dto);
        return instEnsino != null ? instEnsino : new EqvTInstEnsino();
    }

    // ===================== AFTER MAPPING =====================
    @AfterMapping
    default void afterMappingToDTO(@MappingTarget EqvtPedidoDTO dto, EqvTPedido pedido) {
        if (pedido.getRequerente() != null) {
            dto.setRequerente(toRequerenteDTO(pedido.getRequerente()));
        }
        if (pedido.getInstEnsino() != null) {
            dto.setInstEnsino(toInstEnsinoDTO(pedido.getInstEnsino()));
        }
        if (pedido.getRequisicao() != null) {
            dto.setRequisicao(toRequisicaoDTO(pedido.getRequisicao()));
        }
    }

    @AfterMapping
    default void afterMappingToRequerenteEntity(@MappingTarget EqvTRequerente entity, EqvTRequerenteDTO dto) {
        // Garantir que campos críticos não sejam nulos
        if (entity.getDateCreate() == null) {
            entity.setDateCreate(LocalDate.now());
        }
    }

    @AfterMapping
    default void afterMappingToInstEnsinoEntity(@MappingTarget EqvTInstEnsino entity, EqvTInstEnsinoDTO dto) {
        // Garantir que campos críticos não sejam nulos
        if (entity.getDateCreate() == null) {
            entity.setDateCreate(LocalDate.now());
        }
        if (entity.getStatus() == null) {
            entity.setStatus("A");
        }
    }

    @AfterMapping
    default void afterMappingToRequisicaoEntity(@MappingTarget EqvTRequisicao entity, EqvTRequisicaoDTO dto) {
        // Garantir que campos críticos não sejam nulos
        if (entity.getDataCreate() == null) {
            entity.setDataCreate(LocalDate.now());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        if (entity.getEtapa() == null) {
            entity.setEtapa(1);
        }
    }
}