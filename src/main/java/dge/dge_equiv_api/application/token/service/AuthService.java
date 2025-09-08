package dge.dge_equiv_api.application.token.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Value("${api.auth.url}")
    private String authUrl;

    @Value("${api.auth.client_id}")
    private String clientId;

    @Value("${api.auth.client_secret}")
    private String clientSecret;

    private String token;
    private Instant expirationTime;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public String getToken() {
        if (token == null || Instant.now().isAfter(expirationTime)) {
            log.info("Token ausente ou expirado. Gerando novo token...");
            gerarToken();
        }
        return token;
    }

    private void gerarToken() {
        log.info("Solicitando token com client_id={} para {}", clientId, authUrl);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // ← JSON!
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("grant_type", "client_credentials");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<AuthTokenResponse> response = restTemplate.exchange(
                    authUrl,
                    HttpMethod.POST,
                    request,
                    AuthTokenResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AuthTokenResponse tokenResponse = response.getBody();
                token = tokenResponse.getAccessToken();
                expirationTime = Instant.now().plusSeconds(tokenResponse.getExpiresIn() - 60);
                log.info("Token obtido com sucesso, expira em {}", expirationTime);
            } else {
                log.error("Erro ao obter token. Status: {}", response.getStatusCode());
                throw new RuntimeException("Erro ao obter token");
            }
        } catch (HttpClientErrorException e) {
            log.error("Erro 401 Unauthorized. Verifique client_id e client_secret");
            throw e;
        }
    }


}
