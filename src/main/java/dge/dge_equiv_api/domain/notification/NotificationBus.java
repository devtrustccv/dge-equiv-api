package dge.dge_equiv_api.domain.notification;

import dge.dge_equiv_api.application.notification.dto.NotificationRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class NotificationBus {

    @Value("${api.base.service.url}")
    private String notificacaoUrl;

    public void sendEmail(NotificationRequestDTO dto) {
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String charset = StandardCharsets.UTF_8.name();
        String CRLF = "\r\n";

        try {
            URL url = new URL(notificacaoUrl + "/notification");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Accept", "application/json");

            try (
                    OutputStream output = conn.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)
            ) {
                writeFormField(writer, boundary, "email", dto.getEmail(), charset, CRLF);
                writeFormField(writer, boundary, "message", dto.getMensagem(), charset, CRLF);
                writeFormField(writer, boundary, "subject", dto.getAssunto(), charset, CRLF);
                writeFormField(writer, boundary, "tipoProcesso", dto.getTipoProcesso(), charset, CRLF);
                writeFormField(writer, boundary, "idProcesso", dto.getIdProcesso(), charset, CRLF);
                writeFormField(writer, boundary, "appName", dto.getAppName(), charset, CRLF);
                writeFormField(writer, boundary, "idRelacao", dto.getIdRelacao(), charset, CRLF);
                writeFormField(writer, boundary, "tipoRelacao", dto.getTipoRelacao(), charset, CRLF);
                writeFormField(writer, boundary, "isAlert", dto.getIsAlert() != null ? dto.getIsAlert() : "NAO", charset, CRLF);

                writer.append("--").append(boundary).append("--").append(CRLF).flush();
            }

            int status = conn.getResponseCode();

            // apenas lê resposta (erro ou sucesso)
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            status > 299 ? conn.getErrorStream() : conn.getInputStream(),
                            StandardCharsets.UTF_8))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    response.append(line);

                if (status > 299) {
                    System.err.println("Erro ao enviar email: " + response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage());
        }
    }

    private void writeFormField(PrintWriter writer, String boundary, String name, String value, String charset, String CRLF) {
        writer.append("--").append(boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
        writer.append(CRLF).append(value != null ? value : "").append(CRLF).flush();
    }
}

