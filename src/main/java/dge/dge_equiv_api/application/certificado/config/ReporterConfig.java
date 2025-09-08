package dge.dge_equiv_api.application.certificado.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReporterConfig {

    @Value("${reporter.eqv.url}")
    private String reporterEqvUrl;

    public String getReporterEqvUrl() {
        return reporterEqvUrl;
    }
}
