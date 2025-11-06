package dge.dge_equiv_api.application.acompanhamento.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.application.token.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AcompanhamentoService {

    private static final Logger log = LogManager.getLogger(AcompanhamentoService.class);

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.acompanhamento.create}")
    private String createUrl;

    public AcompanhamentoService(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.restTemplate = new RestTemplate();
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Desabilita a serialização de datas
    }

    public void criarAcompanhamento(AcompanhamentoDTO body) {
        try {
            String token = authService.getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            // DEBUG: Log do JSON que será enviado
            String jsonPayload = objectMapper.writeValueAsString(body);
            log.info("JSON que será enviado para a API: {}", jsonPayload);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            log.info("Enviando requisição para: {}", createUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                    createUrl,
                    HttpMethod.POST,
                    request,
                    String.class  // Mude para String para ver a resposta completa
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Acompanhamento criado com sucesso! Resposta: {}", response.getBody());
            } else {
                log.warn("Resposta não sucedida. Status: {}, Body: {}",
                        response.getStatusCode(), response.getBody());
            }

        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar AcompanhamentoDTO para JSON", e);
        } catch (HttpClientErrorException e) {
            log.error("ERRO 400 - Bad Request na API de acompanhamento");
            log.error("Status: {}", e.getStatusCode());
            log.error("Response Body: {}", e.getResponseBodyAsString());
            log.error("Headers: {}", e.getResponseHeaders());

            // Log adicional para debug
            log.debug("Tentando logar o objeto body para verificação:");
            try {
                log.debug("Body object: {}", objectMapper.writeValueAsString(body));
            } catch (JsonProcessingException ex) {
                log.debug("Não foi possível serializar body para debug");
            }
        } catch (Exception e) {
            log.error("Falha na comunicação com a API de acompanhamento: {}", e.getMessage(), e);
        }
    }
}