package dge.dge_equiv_api.acompanhamento.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dge.dge_equiv_api.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.token.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        // Configura o RestTemplate para usar o ObjectMapper que sabe lidar com LocalDateTime
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
        this.restTemplate.setMessageConverters(converters);
    }

    public void criarAcompanhamento(AcompanhamentoDTO body) {
        try {
            String token = authService.getToken();
            //log.info("Token: " + token);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AcompanhamentoDTO> request = new HttpEntity<>(body, headers);

            log.info("Enviando AcompanhamentoDTO para API: " + body);

            ResponseEntity<AcompanhamentoDTO> response = restTemplate.exchange(
                    createUrl,
                    HttpMethod.POST,
                    request,
                    AcompanhamentoDTO.class
            );
            //log.debug("Payload enviado: {}", objectMapper.writeValueAsString(body));

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Acompanhamento criado com sucesso: " + response.getBody());
            } else {
                log.info("Erro ao criar acompanhamento. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Falha na comunicação com a API de acompanhamento: " + e.getMessage(), e);
        }
    }
}
