package dge.dge_equiv_api.application.nif;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class PesquisaNifService {

    private static final Logger logger = LoggerFactory.getLogger(PesquisaNifService.class);

    @Value("${api.validar.url}")
    private String apiUrl;

    @Value("${api.validar.token}")
    private String tokenLinkPublic;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PesquisaNifService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public boolean validarNif(Integer nif) {
        if (nif == null || nif <= 0) {
            logger.warn("NIF inválido ou nulo: {}", nif);
            return false;
        }

        try {
            String url = apiUrl + "?num_nif=" + nif;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(tokenLinkPublic);
            headers.set("charset", "utf-8");

            HttpEntity<String> entity = new HttpEntity<>("{}", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null || response.getBody().isBlank()) {
                logger.warn("Resposta inválida da API para NIF {}. Status: {}", nif, response.getStatusCode());
                return false;
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode entriesNode = root.path("Entries");

            if (entriesNode.isMissingNode() || entriesNode.isNull()) {
                logger.warn("Campo 'Entries' não encontrado para NIF {}", nif);
                return false;
            }

            JsonNode entryNode = entriesNode.path("Entry");

            if (entryNode.isMissingNode() || entryNode.isNull()) {
                logger.warn("Campo 'Entry' não encontrado para NIF {}", nif);
                return false;
            }

            String apiNifStr;

            // Caso venha como objeto
            if (entryNode.isObject()) {
                apiNifStr = entryNode.path("NU_NIF").asText();
            }
            // Caso algum dia venha como array
            else if (entryNode.isArray() && entryNode.size() > 0) {
                apiNifStr = entryNode.get(0).path("NU_NIF").asText();
            } else {
                logger.warn("Campo 'Entry' veio num formato inesperado para NIF {}", nif);
                return false;
            }

            if (apiNifStr == null || apiNifStr.isBlank()) {
                logger.warn("NU_NIF vazio na resposta da API para NIF {}", nif);
                return false;
            }

            Integer apiNif = Integer.valueOf(apiNifStr);
            boolean valido = apiNif.equals(nif);

            logger.info("Validação do NIF {}: {}", nif, valido);
            return valido;

        } catch (Exception e) {
            logger.error("Erro ao validar NIF {}: {}", nif, e.getMessage(), e);
            return false;
        }
    }
}