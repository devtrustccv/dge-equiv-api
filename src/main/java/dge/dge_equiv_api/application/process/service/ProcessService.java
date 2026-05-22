package dge.dge_equiv_api.application.process.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dge.dge_equiv_api.Utils.RestClientHelper;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequerente;
import dge.dge_equiv_api.application.process.dto.ProcessEquivDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessService {

    private final RestClientHelper restClientHelper;
    private final ObjectMapper mapper;

    @Value("${process.equiv-url}")
    private String processStartUrl;

    @Value("${process.equiv-profile-code:equiv}")
    private String processProfileCode;

    @Value("${process.equiv-email:}")
    private String processEmail;


    /** Inicia o processo de equivalência */
    public String iniciarProcessoEquivalencia(EqvTRequerente requerente, List<EqvTPedido> pedidos) {
        validarRequerente(requerente);
        validarPedidos(pedidos);

        ProcessEquivDto dto = new ProcessEquivDto();

        preencherDadosRequerente(dto, requerente);
        preencherDadosPedidos(dto, pedidos);

        log.info("saida dto .....{}", dto);

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        try {
            ResponseEntity<String> response = restClientHelper.sendRequest(
                    processStartUrl + "/start",
                    HttpMethod.POST,
                    criarParamProcessDTO(dto, null),
                    String.class,
                    headers
            );

            validarRespostaProcesso(response);
            String processInstanceId = parseProcessId(response.getBody());

            log.info("[ProcessEquiv] Processo iniciado com ID: {}", processInstanceId);
            return processInstanceId;

        } catch (Exception e) {
            log.error("[ProcessEquiv] Falha ao iniciar processo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao iniciar processo de equivalência.", e);
        }
    }


    public String avancarProcessoEquivalencia(EqvTRequerente requerente, List<EqvTPedido> pedidos, String numProcesso) {

        ProcessEquivDto dto = new ProcessEquivDto();
        dto.setId_solicitacao(numProcesso);
        preencherDadosRequerente(dto, requerente);
        preencherDadosPedidos(dto, pedidos);

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        try {
            validarIdProcesso(numProcesso);
            ResponseEntity<String> response = restClientHelper.sendRequest(
                    processStartUrl + "/advance",
                    HttpMethod.POST,
                    criarParamProcessDTO(dto, numProcesso),
                    String.class,
                    headers
            );

            validarRespostaProcesso(response);
            String processInstanceId = parseProcessId(response.getBody());

            log.info("[ProcessEquiv] Processo iniciado com ID: {}", processInstanceId);
            return processInstanceId;

        } catch (Exception e) {
            log.error("[ProcessEquiv] Falha ao iniciar processo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao iniciar processo de equivalência.", e);
        }
    }

    // ================================================================
    // VALIDAÇÕES Mapeamento na campos de igrp
    // ================================================================
    private void validarRequerente(EqvTRequerente r) {
        if (r == null) throw new IllegalArgumentException("Requerente não pode ser nulo.");
        if (r.getNome() == null || r.getNome().isBlank()) throw new IllegalArgumentException("Nome obrigatório.");
        if (r.getDocIdentificacao() == null || r.getDocIdentificacao().isBlank())
            throw new IllegalArgumentException("Tipo de documento obrigatório.");
        if (r.getDocNumero() == null || r.getDocNumero().isBlank())
            throw new IllegalArgumentException("Número do documento obrigatório.");
        if (r.getEmail() == null || r.getEmail().isBlank()) throw new IllegalArgumentException("Email obrigatório.");
    }

    private void validarPedidos(List<EqvTPedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) throw new IllegalArgumentException("Lista de pedidos vazia.");
        for (EqvTPedido p : pedidos) {
            if (p.getFormacaoProf() == null || p.getFormacaoProf().isBlank())
                throw new IllegalArgumentException("Formação profissional obrigatória em todos os pedidos.");
            if (p.getInstEnsino() == null || p.getInstEnsino().getNome() == null
                    || p.getInstEnsino().getNome().isBlank())
                throw new IllegalArgumentException("Instituição de ensino obrigatória em todos os pedidos.");
        }
    }

    // ================================================================
    // PREENCHIMENTO DTO
    // ================================================================
    private void preencherDadosRequerente(ProcessEquivDto dto, EqvTRequerente r) {
        dto.setId_requerente(r.getId() != null ? r.getId().toString() : "");
        dto.setTipo_documento_identificacao_(r.getDocIdentificacao());
        dto.setN_documento_identificacao_(r.getDocNumero());
        dto.setNome(r.getNome());
        dto.setEmail(r.getEmail());
        dto.setSexo(r.getSexo());
        dto.setTelefonetelemovel(String.valueOf(r.getContato()));
        dto.setNacionalidade(r.getNacionalidade());
        dto.setData_de_nascimento_1(r.getDataNascimento() != null ? r.getDataNascimento().toString() : "");
        dto.setNif(r.getNif() != null ? r.getNif().toString() : "");
        dto.setHabilitacao_escolar(r.getHabilitacao() != null ? r.getHabilitacao().toString() : "");
        dto.setTelefonetelemovel(r.getContato() != null ? r.getContato().toString() : "");
        dto.setData_emissao(r.getDataEmissaoDoc() != null ? r.getDataEmissaoDoc().toString() : "");
        dto.setData_validade(r.getDataValidadeDoc() != null ? r.getDataValidadeDoc().toString() : "");
    }

    private void preencherDadosPedidos(ProcessEquivDto dto, List<EqvTPedido> pedidos) {

        if (dto == null) {
            return; // nada a fazer
        }

        // Se pedidos for null, transforma em lista vazia
        List<EqvTPedido> lista = pedidos == null ? Collections.emptyList() : pedidos;

        dto.setFormacao_profissional__fk(
                mapOrEmpty(lista, p -> p != null ? p.getFormacaoProf() : null)
        );
        dto.setFormacao_profissional__fk_desc(dto.getFormacao_profissional__fk());

        dto.setInstituicao_de_ensino_fk(
                mapOrEmpty(lista, p -> p != null && p.getInstEnsino() != null ? p.getInstEnsino().getNome() : null)
        );
        dto.setInstituicao_de_ensino_fk_desc(dto.getInstituicao_de_ensino_fk());

        dto.setPais_obtencao_fk(
                mapOrEmpty(lista, p -> p != null && p.getInstEnsino() != null ? p.getInstEnsino().getPais() : null)
        );
        dto.setPais_obtencao_fk_desc(dto.getPais_obtencao_fk());

        dto.setAno_de_inicio_fk(
                mapOrEmptyString(lista, p -> p != null ? p.getAnoInicio() : null)
        );
        dto.setAno_de_inicio_fk_desc(dto.getAno_de_inicio_fk());

        dto.setAno_conclusao_fk(
                mapOrEmptyString(lista, p -> p != null ? p.getAnoFim() : null)
        );
        dto.setAno_conclusao_fk_desc(dto.getAno_conclusao_fk());

        dto.setCarga_horaria_fk(
                mapOrEmptyString(lista, p -> p != null ? p.getCarga() : null)
        );
        dto.setCarga_horaria_fk_desc(dto.getCarga_horaria_fk());

        // Replicação
        dto.setFormacao_profissional1_fk(dto.getFormacao_profissional__fk());
        dto.setFormacao_profissional1_fk_desc(dto.getFormacao_profissional__fk_desc());

        // Preenche listas separadoras com número correto de itens
        dto.setSeparatorlist_1_id(
                lista.stream().map(p -> "").toList()
        );

        dto.setSeparatorlist_2_id(
                lista.stream().map(p -> "").toList()
        );
    }


    // ================================================================
    // HELPERS
    // ================================================================
    private List<String> mapOrEmpty(List<EqvTPedido> pedidos, Function<EqvTPedido, String> mapper) {
        return pedidos.stream().map(p -> {
            String val = mapper.apply(p);
            return val != null ? val : "";
        }).toList();
    }

    private List<String> mapOrEmptyString(List<EqvTPedido> pedidos, Function<EqvTPedido, Object> mapper) {
        return pedidos.stream().map(p -> {
            Object val = mapper.apply(p);
            return val != null ? val.toString() : "";
        }).toList();
    }

    private String parseProcessId(String json) throws IOException {
        if (json == null || json.isBlank()) {
            throw new IllegalStateException("Resposta do processo sem body.");
        }

        String body = json.trim();
        if (!body.startsWith("{") && !body.startsWith("[") && !body.startsWith("\"")) {
            return body;
        }

        JsonNode root = mapper.readTree(json);
        String processInstanceId = root.isTextual()
                ? root.asText()
                : root.path("processInstanceId").asText();

        if (processInstanceId == null || processInstanceId.isBlank()) {
            throw new IllegalStateException("Resposta do processo sem identificador.");
        }

        return processInstanceId;
    }

    private void validarRespostaProcesso(ResponseEntity<String> response) {
        if (response == null) {
            throw new IllegalStateException("Servico de processo nao retornou resposta.");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Servico de processo retornou status " + response.getStatusCode());
        }
    }

    private Map<String, String[]> convertDtoToMap(Object dto) throws IllegalAccessException {
        Map<String, String[]> finalMap = new HashMap<>();

        if (dto instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                Object value = entry.getValue();

                if (value == null) continue;

                addValue(finalMap, key, value);
            }

            return finalMap;
        }

        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String key = field.getName();
            Object value = field.get(dto);

            if (value == null) continue;

            addValue(finalMap, key, value);
        }

        return finalMap;
    }

    private void addValue(Map<String, String[]> finalMap, String key, Object value) {
        String cleanedKey = key.replaceAll("(_desc)?_[0-9]+$", "");
        String finalKey = cleanedKey.startsWith("p_") ? cleanedKey : "p_" + cleanedKey;

        if (value instanceof List<?> list) {
            String[] values = list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toArray(String[]::new);

            finalMap.put(finalKey, values);

        } else if (value instanceof String[] arr) {
            finalMap.put(finalKey, arr);

        } else if (value.getClass().isArray()) {
            finalMap.put(finalKey, arrayToStringArray(value));

        } else {
            finalMap.put(finalKey, new String[]{value.toString()});
        }
    }

    private String[] arrayToStringArray(Object array) {
        int length = java.lang.reflect.Array.getLength(array);
        String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            Object item = java.lang.reflect.Array.get(array, i);
            result[i] = item != null ? item.toString() : "";
        }

        return result;
    }


    private ParamProcessDTO criarParamProcessDTO(ProcessEquivDto dto, String idProcesso) throws IllegalAccessException {
        String email = obterEmailProcesso(dto);
        return new ParamProcessDTO(
                "processo_equivalencia",
                convertDtoToVariables(dto),
                idProcesso,
                processProfileCode,
                email
        );
    }

    private String obterEmailProcesso(ProcessEquivDto dto) {
        String email = processEmail != null && !processEmail.isBlank()
                ? processEmail
                : dto.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email e obrigatorio para iniciar/avancar processo.");
        }

        return email;
    }

    private void validarIdProcesso(String idProcesso) {
        if (idProcesso == null || idProcesso.isBlank()) {
            throw new IllegalArgumentException("NProcesso e obrigatorio para avancar processo.");
        }
    }

    private Map<String, Object> convertDtoToVariables(ProcessEquivDto dto) throws IllegalAccessException {
        return new HashMap<>(convertDtoToMap(dto));
    }

    private record ParamProcessDTO(
            String tipoProcesso,
            Map<String, Object> variables,
            String idProcesso,
            String profileCode,
            String email
    ) {
    }

    public   void  deleteProcess(String processId) {

        try {
            ResponseEntity<String> response = restClientHelper.sendRequest(
                    processStartUrl+ "/delete/"+processId,
                    HttpMethod.DELETE,
                    null,
                    String.class,
                    null
            );

        } catch (Exception e) {
            log.error("[ProcessEquiv] falha ao deletar processo: {}", e.getMessage(), e);
            throw new RuntimeException("falha ao deletar processo.", e);
        }

    }
}
