package dge.dge_equiv_api.application.logs.service;

import dge.dge_equiv_api.application.logs.dto.LogDTO;
import dge.dge_equiv_api.application.logs.dto.LogItemDTO;
import dge.dge_equiv_api.application.logs.dto.ParecerCnepHistoricoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final RestTemplate restTemplate;

    public LogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${api.base.service.url}")
    private String logs;

    /**
     * Tabelas associadas ao fluxo de decisão do pedido
     */
    private static final List<String> TABLE_NAMES = List.of(
            "eqv_t_decisao_ap",
            "eqv_t_decisao_vp",
            "eqv_t_decisao_despacho"
    );


    public List<ParecerCnepHistoricoDTO> getHistoricoParecerCompletoByPedidoId(Integer pedidoId) {

        return TABLE_NAMES.stream()
                .flatMap(tableName -> buscarLogsPorTabelaEPedido(pedidoId, tableName).stream())
                .filter(log -> log.getLogsItems() != null && !log.getLogsItems().isEmpty())
                .flatMap(log ->
                        log.getLogsItems().stream()
                                .filter(item -> item.getOldValue() != null || item.getNewValue() != null)
                                .map(item -> montarHistorico(log, item))
                )
                .filter(Objects::nonNull)
                .distinct() // <- remove duplicados
                .collect(Collectors.toList());


    }

    /**
     * ============================
     * BUSCA LOGS POR TABELA + PEDIDO
     * ============================
     */
    private List<LogDTO> buscarLogsPorTabelaEPedido(Integer pedidoId, String tableName) {
        try {
            String url = String.format(
                    "%s/logs?appDad=equiv&tablid=%d&pageSize=100",
                    logs, pedidoId
            );

            ResponseEntity<List<LogDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LogDTO>>() {}
            );

            return response.getBody() != null
                    ? response.getBody()
                    : Collections.emptyList();

        } catch (Exception e) {
            System.err.printf(
                    "Erro ao buscar logs | tabela=%s | pedidoId=%d | erro=%s%n",
                    tableName, pedidoId, e.getMessage()
            );
            return Collections.emptyList();
        }
    }

    /**
     * ============================
     * MAPEAMENTO PARA HISTÓRICO
     * ============================
     */
    private ParecerCnepHistoricoDTO montarHistorico(LogDTO log, LogItemDTO item) {
        if (item == null) return null;

        ParecerCnepHistoricoDTO historico = new ParecerCnepHistoricoDTO();

        historico.setEtapa(resolverEtapa(log.getTableName()));
        historico.setCampo(item.getColumn());


        if ("motivoRetificado".equals(item.getColumn())) {
            historico.setValorAnterior(mapMotivoRetificado(item.getOldValue()));
            historico.setValorNovo(mapMotivoRetificado(item.getNewValue()));
        } else if (!"nivel".equalsIgnoreCase(item.getColumn())) {
            historico.setValorAnterior(mapValor(item.getOldValue()));
            historico.setValorNovo(mapValor(item.getNewValue()));
        } else {
            historico.setValorAnterior(item.getOldValue());
            historico.setValorNovo(item.getNewValue());
        }


        return historico;
    }

    /**
     * Mapeia 1 -> "Deferido", 2 -> "Indeferido", 3 -> "Retificado"
     * Mantém os demais valores inalterados
     */
    private String mapValor(String valor) {
        if (valor == null) return null;

        switch (valor) {
            case "1": return "Deferido";
            case "2": return "Indeferido";
            case "3": return "Retificado";
            default:  return valor;
        }
    }

    private String resolverEtapa(String tableName) {

        if (tableName == null) return null;

        switch (tableName) {
            case "eqv_t_decisao_ap":
                return "Análise Prévia";

            case "eqv_t_decisao_vp":
                return "verificação Previa";

            case "eqv_t_decisao_despacho":
                return "Despacho";

            default:
                return tableName; // fallback
        }
    }
    private static final Map<String, String> MOTIVO_MAP = Map.of(
            "1", "Atualização de dados pessoais",
            "2", "Atualização de dados académicos",
            "3", "Substituição de documentos",
            "4", "Adição de novos documentos"
    );

    private String mapMotivoRetificado(String valor) {
        if (valor == null || valor.isEmpty()) return valor;
        return Arrays.stream(valor.split(";"))
                .map(String::trim)
                .map(MOTIVO_MAP::get)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }


}
