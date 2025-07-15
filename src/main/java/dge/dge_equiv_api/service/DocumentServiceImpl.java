package dge.dge_equiv_api.service;
import dge.dge_equiv_api.Utils.RestClientHelper;
import dge.dge_equiv_api.model.dto.DocRelacaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final RestClientHelper restClientHelper;

    @Value("${api.base.service.url}")
    private String url;

    public DocumentServiceImpl(RestClientHelper restClientHelper) {
        this.restClientHelper = restClientHelper;
    }

    public String save(DocRelacaoDTO dto) {
        String apiUrl = url + "/documentos";

        // Criar headers
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);

        // Criar corpo da requisição
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("tipoRelacao", dto.getTipoRelacao());
        body.add("idRelacao", dto.getIdRelacao());
        body.add("estado", dto.getEstado());
        body.add("idTpDoc", dto.getIdTpDoc());
        body.add("appCode", dto.getAppCode());
        body.add("fileName", dto.getFileName());
        String ext = getFileExtension(dto.getFile().getOriginalFilename());
        var path = getPathFile(dto.getFileName(),  dto.getTipoRelacao(), dto.getIdRelacao(), dto.getAppCode(), ext);
        body.add("path", path);

        if (dto.getIdTpDoc() != null) {
            body.add("idTpDoc", dto.getIdTpDoc());
        }

        MultipartFile file = dto.getFile();
        if (file != null && !file.isEmpty()) {
            try {
                ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                body.add("file", fileResource);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar o arquivo", e);
            }
        }

        // Enviar requisição via helper
        ResponseEntity<String> response = restClientHelper.sendRequest(
                apiUrl,
                HttpMethod.POST,
                body,
                String.class,
                headersMap
        );
        System.out.println("documento");

        // Verificar resposta
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Documento salvo com sucesso!");
        } else {
            System.err.println("Erro ao salvar documento: " + response.getBody());
        }

        return path;
    }

    public String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return ""; // Sem extensão
    }

    public String getPathFile(String fileName, String tipoRelacao, Integer idRelacao, String appCode, String ext) {
        System.out.println("ext " + ext);
        return appCode + "/"+LocalDateTime.now().getYear()+"/modulos/"+tipoRelacao+"/"+idRelacao+"/"+fileName +"."+ ext;
    }
}
