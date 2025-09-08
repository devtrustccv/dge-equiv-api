package dge.dge_equiv_api.web.param.controller;

import dge.dge_equiv_api.application.reporter.dto.EqvTParamReportDTO;
import dge.dge_equiv_api.application.reporter.service.IEqvTParamReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/param-reports")
@RequiredArgsConstructor
public class EqvTParamReportController {

    private final IEqvTParamReportService service;

    @GetMapping
    public List<EqvTParamReportDTO> getAllParamReports() {
        return service.getAll();
    }
}
