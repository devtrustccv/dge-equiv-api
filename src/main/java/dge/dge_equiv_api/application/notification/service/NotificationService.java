package dge.dge_equiv_api.application.notification.service;

import dge.dge_equiv_api.Utils.RestClientHelper;
import dge.dge_equiv_api.application.notification.dto.DefaultReponseDTO;
import dge.dge_equiv_api.application.notification.dto.NotificationRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final RestClientHelper restClientHelper;

    @Value("${api.base.service.url}")
    private String notificationBaseUrl;

    public NotificationService(RestClientHelper restClientHelper) {
        this.restClientHelper = restClientHelper;
    }

    public DefaultReponseDTO enviarEmail(NotificationRequestDTO dto) {
        String url = notificationBaseUrl + "/notification";

        // Construir corpo multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("appCode", dto.getAppName());
        body.add("Subject", dto.getSubject());
        body.add("message", dto.getMessage());
        body.add("email", dto.getEmail());

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);

        // Enviar a requisição
        ResponseEntity<DefaultReponseDTO> response = restClientHelper.sendRequest(
                url,
                HttpMethod.POST,
                body,
                DefaultReponseDTO.class,
                headers
        );

        return response.getBody();
    }
}
