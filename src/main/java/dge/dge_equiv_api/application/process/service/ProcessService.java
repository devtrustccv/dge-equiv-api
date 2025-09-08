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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessService {

    private final RestClientHelper restClientHelper;
    private final ObjectMapper mapper;

    @Value("${process.equiv.start-url}")
    private String processStartUrl;

    /** Inicia o processo de equivalência */
    public String iniciarProcessoEquivalencia(EqvTRequerente requerente, List<EqvTPedido> pedidos) {
        validarRequerente(requerente);
        validarPedidos(pedidos);

        ProcessEquivDto dto = new ProcessEquivDto();
        preencherDadosRequerente(dto, requerente);
        preencherDadosPedidos(dto, pedidos);

        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        try {
            ResponseEntity<String> response = restClientHelper.sendRequest(
                    processStartUrl,
                    HttpMethod.POST,
                    dto,
                    String.class,
                    headers
            );

            String processInstanceId = parseProcessId(response.getBody());

            log.info("[ProcessEquiv] Processo iniciado com ID: {}", processInstanceId);
            return processInstanceId;

        } catch (Exception e) {
            log.error("[ProcessEquiv] Falha ao iniciar processo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao iniciar processo de equivalência.", e);
        }
    }

    // ================================================================
    // VALIDAÇÕES
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
        dto.setNacionalidade(r.getNacionalidade());
        dto.setData_de_nascimento_1(r.getDataNascimento() != null ? r.getDataNascimento().toString() : "");
        dto.setNif(r.getNif() != null ? r.getNif().toString() : "");
        dto.setHabilitacao_escolar(r.getHabilitacao() != null ? r.getHabilitacao().toString() : "");
        dto.setTelefonetelemovel(r.getContato() != null ? r.getContato().toString() : "");
        dto.setData_emissao(r.getDataEmissaoDoc() != null ? r.getDataEmissaoDoc().toString() : "");
        dto.setData_validade(r.getDataValidadeDoc() != null ? r.getDataValidadeDoc().toString() : "");
    }

    private void preencherDadosPedidos(ProcessEquivDto dto, List<EqvTPedido> pedidos) {
        dto.setFormacao_profissional__fk(mapOrEmpty(pedidos, EqvTPedido::getFormacaoProf));
        dto.setFormacao_profissional__fk_desc(dto.getFormacao_profissional__fk());

        dto.setInstituicao_de_ensino_fk(mapOrEmpty(pedidos, p -> p.getInstEnsino().getNome()));
        dto.setInstituicao_de_ensino_fk_desc(dto.getInstituicao_de_ensino_fk());

        dto.setPais_obtencao_fk(mapOrEmpty(pedidos, p -> p.getInstEnsino().getPais()));
        dto.setPais_obtencao_fk_desc(dto.getPais_obtencao_fk());

        dto.setAno_de_inicio_fk(mapOrEmptyString(pedidos, EqvTPedido::getAnoInicio));
        dto.setAno_de_inicio_fk_desc(dto.getAno_de_inicio_fk());

        dto.setAno_conclusao_fk(mapOrEmptyString(pedidos, EqvTPedido::getAnoFim));
        dto.setAno_conclusao_fk_desc(dto.getAno_conclusao_fk());

        dto.setCarga_horaria_fk(mapOrEmptyString(pedidos, EqvTPedido::getCarga));
        dto.setCarga_horaria_fk_desc(dto.getCarga_horaria_fk());

        dto.setFormacao_profissional1_fk(dto.getFormacao_profissional__fk());
        dto.setFormacao_profissional1_fk_desc(dto.getFormacao_profissional__fk_desc());
        dto.setSeparatorlist_1_id(pedidos.stream().map(p -> "").toList());
        dto.setSeparatorlist_2_id(pedidos.stream().map(p -> "").toList());
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
        if (json == null || json.isBlank()) return null;
        JsonNode root = mapper.readTree(json);
        return root.path("processInstanceId").asText();
    }
}
