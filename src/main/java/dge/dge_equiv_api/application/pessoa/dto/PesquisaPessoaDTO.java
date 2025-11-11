package dge.dge_equiv_api.application.pessoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PesquisaPessoaDTO {
    @JsonProperty("NOME_PROPRIO") private String nomeProprio;
    @JsonProperty("NOME_APELIDO") private String nomeApelido;
    @JsonProperty("NOME_COMPLETO") private String nomeCompleto;
    @JsonProperty("NOME_PAI_PROPRIO") private String nomePaiProprio;
    @JsonProperty("NOME_PAI_APELIDO") private String nomePaiApelido;
    @JsonProperty("NOME_MAE_PROPRIO") private String nomeMaeProprio;
    @JsonProperty("NOME_MAE_APELIDO") private String nomeMaeApelido;
    @JsonProperty("DATA_NASC") private String dataNascimento;
    @JsonProperty("NACIONALIDADE_ID") private String nacionalidadeId;
    @JsonProperty("NACIONALIDADE") private String nacionalidade;
    @JsonProperty("NATURALIDADE_ID") private String naturalidadeId;
    @JsonProperty("NATURALIDADE") private String naturalidade;
    @JsonProperty("ESTADO_CIVIL") private String estadoCivil;
    @JsonProperty("SEXO") private String sexo;
    @JsonProperty("NIF") private String nif;
    @JsonProperty("DT_EMISSAO") private String dataEmissao;
    @JsonProperty("DT_VALIDADE") private String dataValidade;
    @JsonProperty("NUM_DOCUMENTO") private String numDocumento;
    @JsonProperty("id_tp_doc") private String tipoDocumento;
    @JsonProperty("TELEMOVEL") private Integer telemovel;
    @JsonProperty("EMAIL") private String email;
    @JsonProperty("MORADA") private String morada;
    @JsonProperty("LOCALIDADE") private String localidade;
    @JsonProperty("CONCELHO_ID") private String concelhoId;
    @JsonProperty("ILHA_ID") private String ilhaId;
    @JsonProperty("FACE") private String faceUrl;
    @JsonProperty("EMISSOR_DOC") private String emissorDoc;
    @JsonProperty("EMISSOR_DESCRICAO") private String emissorDescricao;

}