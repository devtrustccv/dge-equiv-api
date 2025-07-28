package dge.dge_equiv_api.process.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProcessConfig {

    @Value("${process.equiv.start-url}")
    private String startProcessUrl;

    public String getStartProcessUrl() {
        return startProcessUrl;
    }
}

