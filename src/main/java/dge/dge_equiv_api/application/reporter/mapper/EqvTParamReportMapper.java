package dge.dge_equiv_api.application.reporter.mapper;

import dge.dge_equiv_api.application.reporter.dto.EqvTParamReportDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTParamReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface EqvTParamReportMapper {

    // Converte Entity para DTO
    EqvTParamReportDTO toDTO(EqvTParamReport entity);

    // Opcional: converte DTO para Entity
    EqvTParamReport toEntity(EqvTParamReportDTO dto);
}
