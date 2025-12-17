package dge.dge_equiv_api.application.logs.service;

import dge.dge_equiv_api.application.logs.dto.LogDTO;
import dge.dge_equiv_api.application.logs.dto.LogItemDTO;
import dge.dge_equiv_api.application.logs.dto.ParecerCnepHistoricoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final RestTemplate restTemplate;

    public LogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Value("${api.base.service.url}")
    private  String logs;
    /**
     * Retorna TODOS os logs de uma decisão
     */
    public List<LogDTO> getLogsByDecisaoId(Integer decisaoId) {
        try {
            String url = logs  + "/logs?appDad=equiv&tableName=eqv_t_decisao_ap";

            ResponseEntity<List<LogDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LogDTO>>() {}
            );

            return response.getBody().stream()
                    .filter(log -> log.getTableId() != null)
                    .filter(log -> log.getTableId().equals(String.valueOf(decisaoId)))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro ao buscar logs: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retorna APENAS os logs que alteraram o campo parecerCnep para uma decisão específica
     */
    public List<LogDTO> getParecerCnepLogsByDecisaoId(Integer decisaoId) {
        try {
            String url = logs + "/logs?appDad=equiv&tableName=eqv_t_decisao_ap";

            ResponseEntity<List<LogDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LogDTO>>() {}
            );

            return response.getBody().stream()
                    .filter(log -> log.getTableId() != null)
                    .filter(log -> log.getTableId().equals(String.valueOf(decisaoId)))
                    .filter(this::contemAlteracaoParecerCnep)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro ao buscar logs do parecerCnep: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retorna o histórico do parecer CNEP em formato específico
     */
    public List<ParecerCnepHistoricoDTO> getHistoricoParecerCnep(Integer decisaoId) {
        List<LogDTO> logs = getParecerCnepLogsByDecisaoId(decisaoId);

        return logs.stream().map(log -> {
            ParecerCnepHistoricoDTO historico = new ParecerCnepHistoricoDTO();

            List<LogItemDTO> itemsParecer = getParecerCnepItemsOnly(log);
            if (!itemsParecer.isEmpty()) {
                LogItemDTO item = itemsParecer.get(0);
                historico.setDataAlteracao(item.getDate());
                historico.setValorAnterior(item.getOldValue());
                historico.setValorNovo(item.getNewValue());
            }

            return historico;
        }).collect(Collectors.toList());
    }

    /**
     * Verifica se o log contém alteração no campo parecerCnep
     */
    private boolean contemAlteracaoParecerCnep(LogDTO log) {
        if (log.getLogsItems() == null || log.getLogsItems().isEmpty()) {
            return false;
        }

        return log.getLogsItems().stream()
                .anyMatch(item -> "parecerCnep".equals(item.getColumn()));
    }

    /**
     * Método auxiliar: Retorna apenas os LogItemDTO relacionados ao parecerCnep
     */
    public List<LogItemDTO> getParecerCnepItemsOnly(LogDTO log) {
        if (log.getLogsItems() == null) {
            return Collections.emptyList();
        }

        return log.getLogsItems().stream()
                .filter(item -> "parecerCnep".equals(item.getColumn()))
                .collect(Collectors.toList());
    }
}