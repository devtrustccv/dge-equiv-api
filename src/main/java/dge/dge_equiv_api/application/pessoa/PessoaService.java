package dge.dge_equiv_api.application.pessoa;


import com.fasterxml.jackson.databind.ObjectMapper;
import dge.dge_equiv_api.Utils.HttpClientHelper;
import dge.dge_equiv_api.application.pessoa.dto.PesquisaPessoaDTO;
import dge.dge_equiv_api.application.token.service.AuthService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PessoaService {

    @Value("${api.pessoa.url}")
    private String apiPessoaUrl;
    private static final ObjectMapper mapper = new ObjectMapper();

    private final AuthService authService;

    public PessoaService(AuthService authService) {
        this.authService = authService;
    }


    // 🔹 Buscar pessoa por documento
    public PesquisaPessoaDTO searchPessoa(String numDoc) {
        try {
            String token = authService.getToken();
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            JSONObject requestBody = new JSONObject()
                    .put("num_doc", numDoc)
                    .put("nome_completo", "")
                    .put("data_nasc", "");

            String response = HttpClientHelper.post(apiPessoaUrl + "/search-person-by-document", requestBody.toString(), token);


            // API devolve array → pegar último
            PesquisaPessoaDTO[] pessoas = mapper.readValue(response, PesquisaPessoaDTO[].class);
            return pessoas.length > 0 ? pessoas[pessoas.length - 1] : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 Criar pessoa
    public int createPessoaCentral(String numDoc, String nif, String telefone) {
        try {
            String token = authService.getToken();
            JSONObject requestBody = new JSONObject()
                    .put("num_documento", numDoc)
                    .put("nif", nif)
                    .put("telefone", telefone);

            String response = HttpClientHelper.post(apiPessoaUrl + "/create-without-cni", requestBody.toString(), token);
            return mapper.readTree(response).path("id").asInt();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}