package dge.dge_equiv_api.application.document.service;
import dge.dge_equiv_api.Utils.RestClientHelper;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
//@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private EqvTPedidoRepository pedidoRepository; // Adicione esta injeção



    private final RestClientHelper restClientHelper;

    @Value("${api.base.service.url}")
    private String url;

    public DocumentServiceImpl(RestClientHelper restClientHelper) {
        this.restClientHelper = restClientHelper;
    }

    public String save(DocRelacaoDTO dto) {
        String apiUrl = url + "/documentos";

        EqvTPedido pedido = pedidoRepository.findById(dto.getIdRelacao())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        // Obter o número do processo da requisição
        String nProcesso = pedido.getRequisicao() != null
                ? String.valueOf(pedido.getRequisicao().getNProcesso())
                : "SEM-PROCESSO";
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

        var path = getPathFile(dto.getFileName(),  dto.getTipoRelacao(),  dto.getIdRelacao(), nProcesso, dto.getAppCode(), ext);
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

    public String getPathFile(String fileName,  String tipoRelacao, Integer idRelacao, String nprocesso, String appCode, String ext) {
        System.out.println("ext " + ext);
        return appCode + "/"+LocalDateTime.now().getYear()+"/processos/"+tipoRelacao+"/"+nprocesso+"/"+idRelacao+"/"+fileName +"."+ ext;
    }

    public static String getBasePathForProcess(String appDad, @NonNull String processTypeKey, @Nullable String processInstanceID, @Nullable String taskKey) {

        var thisYear = new DateTime().year().getAsString();
        var task = (taskKey == null || taskKey.isEmpty() ? "":taskKey+"/");
        var processId = (processInstanceID == null || processInstanceID.isEmpty() ? "":processInstanceID+"/");

        return appDad+"/"+thisYear+"/processos/"+processTypeKey+"/"+processId+task;
    }
    //pegar doc no minio

    public byte[] previewDocumento(Integer idRelacao, String tipoRelacao, String appCode, boolean download) {
        String apiUrl = url + "/documentos/preview-by-tipo-rel"
                + "?idRelacao=" + idRelacao
                + "&tipoRelacao=" + tipoRelacao
                + "&appCode=" + appCode
                + "&download=" + download;

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE);

        ResponseEntity<byte[]> response = restClientHelper.sendRequest(
                apiUrl,
                HttpMethod.GET,
                null,
                byte[].class,
                headers
        );

        System.out.println("link...."+apiUrl);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Erro ao buscar preview do documento: " + response.getStatusCode());
        }
    }

    @Override
    public List<DocumentoDTO> getDocumentosPorRelacao(Integer idRelacao, String tipoRelacao, String appCode) {
        String apiUrl = url + "/documentos/preview-by-tipo-rel"
                + "?idRelacao=" + idRelacao
                + "&tipoRelacao=" + tipoRelacao
                + "&appCode=" + appCode;

        ResponseEntity<DocumentoDTO[]> response = restClientHelper.sendRequest(
                apiUrl,
                HttpMethod.GET,
                null,
                DocumentoDTO[].class,
                Collections.emptyMap()
        );

        System.out.println("link......."+apiUrl);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            //log.warn("Nenhum documento encontrado para idRelacao={} tipoRelacao={}", idRelacao, tipoRelacao);
            return Collections.emptyList();
        }
    }

}
