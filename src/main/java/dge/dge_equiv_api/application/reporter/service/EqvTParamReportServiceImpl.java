package dge.dge_equiv_api.application.reporter.service;

import dge.dge_equiv_api.application.reporter.dto.EqvTParamReportDTO;
import dge.dge_equiv_api.application.reporter.mapper.EqvTParamReportMapper;
import dge.dge_equiv_api.application.reporter.service.IEqvTParamReportService;
import dge.dge_equiv_api.infrastructure.primary.EqvTParamReport;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTParamReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EqvTParamReportServiceImpl implements IEqvTParamReportService {

    private final EqvTParamReportRepository repository;
    private final EqvTParamReportMapper mapper;

    @Override
    public List<EqvTParamReportDTO> getAll() {
        List<EqvTParamReport> entities = repository.findAll();
        return entities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
