package dge.dge_equiv_api.process.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dge.dge_equiv_api.Utils.RestClientHelper;
import dge.dge_equiv_api.document.dto.DocumentoDTO;
import dge.dge_equiv_api.model.entity.EqvTPedido;
import dge.dge_equiv_api.model.entity.EqvTRequerente;
import dge.dge_equiv_api.model.entity.EqvTRequisicao;
import dge.dge_equiv_api.model.entity.TipoDocumentoEntity;
import dge.dge_equiv_api.process.dto.ProcessEquivDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessService {

    private static final Logger log = LogManager.getLogger(ProcessService.class);
    private final RestClientHelper restClientHelper;

    @Value("${process.equiv.start-url}")
    private String processStartUrl;

    public ProcessService(RestClientHelper restClientHelper) {
        this.restClientHelper = restClientHelper;
    }

    public String iniciarProcessoEquivalencia(EqvTRequerente requerente, List<EqvTPedido> pedidos) throws Exception {
        ProcessEquivDto dto = new ProcessEquivDto();


        // --- campos simples do requerente ---
        dto.setId_requerente(requerente.getId() != null ? requerente.getId().toString() : "");
        dto.setId_solicitacao("");
        dto.setId_instensino("");
        dto.setTipo_documento_identificacao_(requerente.getDocIdentificacao());
        dto.setN_documento_identificacao_(requerente.getDocNumero());
        dto.setNif(requerente.getNif() != null ? requerente.getNif().toString() : "");
        dto.setData_emissao(requerente.getDataEmissaoDoc() != null ? requerente.getDataEmissaoDoc().toString() : "");
        dto.setData_validade(requerente.getDataValidadeDoc() != null ? requerente.getDataValidadeDoc().toString() : "");
        dto.setNome(requerente.getNome());
        dto.setNacionalidade(requerente.getNacionalidade());
        dto.setData_de_nascimento_1(requerente.getDataNascimento() != null ? requerente.getDataNascimento().toString() : "");
        dto.setSexo(requerente.getSexo());
        dto.setEmail(requerente.getEmail());
        dto.setTelefonetelemovel(requerente.getContato() != null ? requerente.getContato().toString() : "");
        dto.setHabilitacao_escolar(requerente.getHabilitacao() != null ? requerente.getHabilitacao().toString() : "");
        dto.setDespacho("");

        // --- listas paralelas populadas a partir dos pedidos ---
        dto.setSeparatorlist_2_id(pedidos.stream().map(p -> "").toList());
        //dto.setDespacho_fk(pedidos.stream().map(p -> p.getDespacho).toList());
        //dto.setDespacho_fk_desc(pedidos.stream().map(p -> p.getDespachoDesc() != null ? p.getDespachoDesc() : "").toList());
        dto.setFormacao_profissional__fk(pedidos.stream().map(p -> p.getFormacaoProf() != null ? p.getFormacaoProf() : "").toList());
        dto.setFormacao_profissional__fk_desc(pedidos.stream().map(p -> p.getFormacaoProf() != null ? p.getFormacaoProf() : "").toList());
        dto.setCarga_horaria_fk(pedidos.stream().map(p -> p.getCarga() != null ? p.getCarga().toString() : "").toList());
        dto.setCarga_horaria_fk_desc(pedidos.stream().map(p -> p.getCarga() != null ? p.getCarga().toString() : "").toList());
        dto.setInstituicao_de_ensino_fk(pedidos.stream().map(p -> p. getInstEnsino().getNome() != null ? p.getInstEnsino().getNome() : null).toList());
        dto.setInstituicao_de_ensino_fk_desc(pedidos.stream().map(p -> p. getInstEnsino().getNome() != null ? p.getInstEnsino().getNome() : null).toList());
        dto.setPais_obtencao_fk(pedidos.stream().map(p -> p.getInstEnsino().getPais() != null ? p.getInstEnsino().getPais() : "").toList());
        dto.setPais_obtencao_fk_desc(pedidos.stream().map(p -> p.getInstEnsino().getPais() != null ? p.getInstEnsino().getPais() : "").toList());
        dto.setAno_de_inicio_fk(pedidos.stream().map(p -> p.getAnoInicio() != null ? p.getAnoInicio().toString() : "").toList());
        dto.setAno_de_inicio_fk_desc(pedidos.stream().map(p -> p.getAnoInicio() != null ? p.getAnoInicio().toString() : "").toList());
        dto.setAno_conclusao_fk(pedidos.stream().map(p -> p.getAnoFim() != null ? p.getAnoFim().toString() : "").toList());
        dto.setAno_conclusao_fk_desc(pedidos.stream().map(p -> p.getAnoFim() != null ? p.getAnoFim().toString() : "").toList());


        dto.setSeparatorlist_1_id(pedidos.stream().map(p -> "").toList());
        //dto.setDespacho1_fk(pedidos.stream().map(p -> p.getDespacho1() != null ? p.getDespacho1() : "").toList());
       // dto.setDespacho1_fk_desc(pedidos.stream().map(p -> p.getDespacho1Desc() != null ? p.getDespacho1Desc() : "").toList());
        //dto.setFicheiropdf_file_uploaded_id(requerente.getFicheiroPdfFileUploadedId() != null ? requerente.getFicheiroPdfFileUploadedId() : "");
        dto.setFormacao_profissional1_fk(pedidos.stream().map(p -> p.getFormacaoProf() != null ? p.getFormacaoProf() : "").toList());
        dto.setFormacao_profissional1_fk_desc(pedidos.stream().map(p -> p.getFormacaoProf() != null ? p.getFormacaoProf() : "").toList());
        //dto.setTipo_documento_fk(tipoDocumento.stream().map(p -> p.getTipoDocumentoFk() != null ? p.getTipoDocumentoFk() : "").toList());
        //dto.setTipo_documento_fk_desc(pedidos.stream().map(p -> p.getTipoDocumentoFkDesc() != null ? p.getTipoDocumentoFkDesc() : "").toList());
        //dto.setFicheiropdf_fk(requerente.getFicheiroPdfFk() != null ? requerente.getFicheiroPdfFk() : "");
       // dto.setFicheiropdf_fk_desc(pedidos.stream().map(p -> p.getFicheiropdfFkDesc() != null ? p.getFicheiropdfFkDesc() : "").toList());
       // dto.setIdocumeto_fk(pedidos.stream().map(p -> p.getIdocumetoFk() != null ? p.getIdocumetoFk() : "").toList());
       // dto.setIdocumeto_fk_desc(pedidos.stream().map(p -> p.getIdocumetoFkDesc() != null ? p.getIdocumetoFkDesc() : "").toList());

        // --- headers para enviar JSON ---
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Envia o DTO completo serializado para o endpoint
        ResponseEntity<String> response = restClientHelper.sendRequest(
                processStartUrl,
                HttpMethod.POST,
                dto,
                String.class,
                headers
        );

        // Extrai o ID do processo da resposta JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        String processInstanceId = root.path("processInstanceId").asText();

        log.info("Processo iniciado com ID: {}", processInstanceId);
        return processInstanceId;
    }





}
