package dge.dge_equiv_api.application.reporter.service;

import dge.dge_equiv_api.application.reporter.dto.EqvTParamReportDTO;

import java.util.List;

public interface IEqvTParamReportService {
    List<EqvTParamReportDTO> getAll();
}
