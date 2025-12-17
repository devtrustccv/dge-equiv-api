package dge.dge_equiv_api.application.motivo_retidicado.mapper;

import dge.dge_equiv_api.application.motivo_retidicado.dto.MotivoRetificacaoResponseDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvtTDecisaoVp;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MotivoRetificacaoMapper {

    MotivoRetificacaoMapper INSTANCE = Mappers.getMapper(MotivoRetificacaoMapper.class);

    @Mapping(target = "numeroProcesso", source = "pedido.requisicao.NProcesso")
    @Mapping(target = "motivoRetificacao", source = "motivoRetificado")
    MotivoRetificacaoResponseDTO toDto(EqvtTDecisaoVp decisaoVp);

    List<MotivoRetificacaoResponseDTO> toDtoList(List<EqvtTDecisaoVp> decisoesVp);

    /**
     * Custom mapping para tratar valores nulos
     */
    @AfterMapping
    default void afterMapping(@MappingTarget MotivoRetificacaoResponseDTO dto, EqvtTDecisaoVp decisaoVp) {
        // Garantir que strings vazias sejam tratadas como null
        if (dto.getMotivoRetificacao() != null && dto.getMotivoRetificacao().trim().isEmpty()) {
            dto.setMotivoRetificacao(null);
        }

    }
}