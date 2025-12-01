package dge.dge_equiv_api.application.pedidov01.mapper;


import dge.dge_equiv_api.application.pedidov01.dto.EqvTInstEnsinoDTO;
import dge.dge_equiv_api.application.pedidov01.dto.EqvTRequerenteDTO;
import dge.dge_equiv_api.application.pedidov01.dto.EqvTRequisicaoDTO;
import dge.dge_equiv_api.application.pedidov01.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.infrastructure.primary.*;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {LocalDate.class})
public interface PedidoMapper {

    Logger log = LoggerFactory.getLogger(PedidoMapper.class);

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

    // ===================== UPDATE METHODS =====================

    //  REQUISIÇÃO - Update completo
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCreate", ignore = true)
    @Mapping(target = "dataUpdate", expression = "java(java.time.LocalDate.now())")
    void updateRequisicaoFromDTO(EqvTRequisicaoDTO dto, @MappingTarget EqvTRequisicao entity);

    //  REQUERENTE - Update completo (todos os campos que vierem no DTO)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreate", ignore = true)
    @Mapping(target = "idPessoa", ignore = true)
    @Mapping(target = "dataUpdate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "userCreate", ignore = true)
    @Mapping(target = "userUpdate", ignore = true)
    @Mapping(target = "habilitacao", expression = "java(stringToInteger(dto.getHabilitacao()))")
    void updateRequerenteFromDTO(EqvTRequerenteDTO dto, @MappingTarget EqvTRequerente entity);

    // INSTITUIÇÃO ENSINO - Update completo
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreate", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateInstEnsinoFromDTO(EqvTInstEnsinoDTO dto, @MappingTarget EqvTInstEnsino entity);

    //  PEDIDO - Update completo
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requerente", ignore = true)
    @Mapping(target = "instEnsino", ignore = true)
    @Mapping(target = "requisicao", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "etapa", ignore = true)
    void updatePedidoFromDTO(EqvtPedidoDTO dto, @MappingTarget EqvTPedido entity);

    // ===================== UPDATE CONDICIONAL METHODS =====================

    /**
     * Atualiza apenas os campos não nulos do DTO para a entidade requerente
     * Campos null no DTO mantêm os valores existentes na entidade
     */
    default void updateRequerenteFromDTOConditional(EqvTRequerenteDTO dto, EqvTRequerente entity) {
        if (dto == null || entity == null) {
            return;
        }

        log.debug("Atualizando requerente ID: {} - Apenas campos não nulos", entity.getId());

        // ✅ Atualizar apenas campos não nulos
        if (dto.getNome() != null) {
            entity.setNome(dto.getNome());
            log.debug("Nome atualizado: {}", dto.getNome());
        }
        if (dto.getNif() != null) {
            entity.setNif(dto.getNif());
            log.debug("NIF atualizado: {}", dto.getNif());
        }
        if (dto.getDocNumero() != null) {
            entity.setDocNumero(dto.getDocNumero());
            log.debug("DocNumero atualizado: {}", dto.getDocNumero());
        }
        if (dto.getDataNascimento() != null) {
            entity.setDataNascimento(dto.getDataNascimento());
            log.debug("DataNascimento atualizada: {}", dto.getDataNascimento());
        }
        if (dto.getNacionalidade() != null) {
            entity.setNacionalidade(dto.getNacionalidade());
            log.debug("Nacionalidade atualizada: {}", dto.getNacionalidade());
        }
        if (dto.getSexo() != null) {
            entity.setSexo(dto.getSexo());
            log.debug("Sexo atualizado: {}", dto.getSexo());
        }
        if (dto.getHabilitacao() != null) {
            entity.setHabilitacao(stringToInteger(dto.getHabilitacao()));
            log.debug("Habilitacao atualizada: {}", dto.getHabilitacao());
        }
        if (dto.getDocIdentificacao() != null) {
            entity.setDocIdentificacao(dto.getDocIdentificacao());
            log.debug("DocIdentificacao atualizado: {}", dto.getDocIdentificacao());
        }
        if (dto.getDataEmissaoDoc() != null) {
            entity.setDataEmissaoDoc(dto.getDataEmissaoDoc());
            log.debug("DataEmissaoDoc atualizada: {}", dto.getDataEmissaoDoc());
        }
        if (dto.getDataValidadeDoc() != null) {
            entity.setDataValidadeDoc(dto.getDataValidadeDoc());
            log.debug("DataValidadeDoc atualizada: {}", dto.getDataValidadeDoc());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
            log.debug("Email atualizado: {}", dto.getEmail());
        }
        if (dto.getContato() != null) {
            entity.setContato(dto.getContato());
            log.debug("Contato atualizado: {}", dto.getContato());
        }

        // Sempre atualizar data de modificação
        entity.setDataUpdate(LocalDate.now());
        log.debug("DataUpdate atualizada: {}", LocalDate.now());
    }

    /**
     * Atualiza apenas os campos não nulos do DTO para a entidade pedido
     */
    default void updatePedidoFromDTOConditional(EqvtPedidoDTO dto, EqvTPedido entity) {
        if (dto == null || entity == null) {
            return;
        }

        log.debug("Atualizando pedido ID: {} - Apenas campos não nulos", entity.getId());

        // ✅ Atualizar apenas campos não nulos
        if (dto.getFormacaoProf() != null) {
            entity.setFormacaoProf(dto.getFormacaoProf());
        }
        if (dto.getCarga() != null) {
            entity.setCarga(dto.getCarga());
        }
        if (dto.getAnoInicio() != null) {
            entity.setAnoInicio(dto.getAnoInicio());
        }
        if (dto.getAnoFim() != null) {
            entity.setAnoFim(dto.getAnoFim());
        }
        if (dto.getNivel() != null) {
            entity.setNivel(dto.getNivel());
        }
        if (dto.getFamilia() != null) {
            entity.setFamilia(dto.getFamilia());
        }

        if (dto.getNumDeclaracao() != null) {
            entity.setNumDeclaracao(dto.getNumDeclaracao());
        }
        if (dto.getDataDespacho() != null) {
            entity.setDataDespacho(dto.getDataDespacho());
        }
    }

    /**
     * Atualiza apenas os campos não nulos do DTO para a entidade requisição
     */
    default void updateRequisicaoFromDTOConditional(EqvTRequisicaoDTO dto, EqvTRequisicao entity) {
        if (dto == null || entity == null) {
            return;
        }

        log.debug("Atualizando requisição ID: {} - Apenas campos não nulos", entity.getId());

        // ✅ Atualizar apenas campos não nulos
        if (dto.getNProcesso() != null) {
            entity.setNProcesso(dto.getNProcesso());
        }
        if (dto.getDataCreate() != null) {
            entity.setDataCreate(dto.getDataCreate());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getEtapa() != null) {
            entity.setEtapa(dto.getEtapa());
        }
        if (dto.getUserCreate() != null) {
            entity.setUserCreate(dto.getUserCreate());
        }
        if (dto.getUserUpdate() != null) {
            entity.setUserUpdate(dto.getUserUpdate());
        }
        if (dto.getIdPessoa() != null) {
            entity.setIdPessoa(dto.getIdPessoa());
        }

        // Sempre atualizar data de modificação
        entity.setDataUpdate(LocalDate.now());
    }

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