package dge.dge_equiv_api.application.process.dto;

import java.util.List;

public class ProcessEquivDto {
    private String id_requerente;
    private String id_solicitacao;
    private String id_instensino;
    private String tipo_documento_identificacao_;
    private String n_documento_identificacao_;
    private String nif;
    private String data_emissao;
    private String data_validade;
    private String nome;
    private String nacionalidade;
    private String data_de_nascimento_1;
    private String sexo;
    private String email;
    private String telefonetelemovel;
    private String habilitacao_escolar;
    private String despacho;
    private String formacao_profissional_;
    private String carga_horaria;
    private String instituicao_de_ensino;
    private String pais_obtencao;
    private String ano_de_inicio;
    private String ano_conclusao;

    private List<String> separatorlist_2_id;
    private List<String> despacho_fk;
    private List<String> despacho_fk_desc;
    private List<String> formacao_profissional__fk;
    private List<String> formacao_profissional__fk_desc;
    private List<String> carga_horaria_fk;
    private List<String> carga_horaria_fk_desc;
    private List<String> instituicao_de_ensino_fk;
    private List<String> instituicao_de_ensino_fk_desc;
    private List<String> pais_obtencao_fk;
    private List<String> pais_obtencao_fk_desc;
    private List<String> ano_de_inicio_fk;
    private List<String> ano_de_inicio_fk_desc;
    private List<String> ano_conclusao_fk;
    private List<String> ano_conclusao_fk_desc;

    private String despacho1;
    private String formacao_profissional1;
    private String idocumeto;
    private List<String> separatorlist_1_id;
    private List<String> despacho1_fk;
    private List<String> despacho1_fk_desc;

    private String ficheiropdf_file_uploaded_id;
    private List<String> formacao_profissional1_fk;
    private List<String> formacao_profissional1_fk_desc;
    private List<String> tipo_documento_fk;
    private List<String> tipo_documento_fk_desc;

    private String ficheiropdf_fk;
    private List<String> ficheiropdf_fk_desc;
    private List<String> idocumeto_fk;
    private List<String> idocumeto_fk_desc;

    public ProcessEquivDto() {
    }

    public ProcessEquivDto(
            String id_requerente, String id_solicitacao, String id_instensino,
            String tipo_documento_identificacao_, String n_documento_identificacao_,
            String nif, String data_emissao, String data_validade, String nome,
            String nacionalidade, String data_de_nascimento_1, String sexo, String email,
            String telefonetelemovel, String habilitacao_escolar, String despacho,
            String formacao_profissional_, String carga_horaria, String instituicao_de_ensino,
            String pais_obtencao, String ano_de_inicio, String ano_conclusao,
            List<String> separatorlist_2_id, List<String> despacho_fk, List<String> despacho_fk_desc,
            List<String> formacao_profissional__fk, List<String> formacao_profissional__fk_desc,
            List<String> carga_horaria_fk, List<String> carga_horaria_fk_desc,
            List<String> instituicao_de_ensino_fk, List<String> instituicao_de_ensino_fk_desc,
            List<String> pais_obtencao_fk, List<String> pais_obtencao_fk_desc,
            List<String> ano_de_inicio_fk, List<String> ano_de_inicio_fk_desc,
            List<String> ano_conclusao_fk, List<String> ano_conclusao_fk_desc,
            String despacho1, String formacao_profissional1, String idocumeto,
            List<String> separatorlist_1_id, List<String> despacho1_fk, List<String> despacho1_fk_desc,
            String ficheiropdf_file_uploaded_id, List<String> formacao_profissional1_fk,
            List<String> formacao_profissional1_fk_desc, List<String> tipo_documento_fk,
            List<String> tipo_documento_fk_desc, String ficheiropdf_fk, List<String> ficheiropdf_fk_desc,
            List<String> idocumeto_fk, List<String> idocumeto_fk_desc
    ) {
        this.id_requerente = id_requerente;
        this.id_solicitacao = id_solicitacao;
        this.id_instensino = id_instensino;
        this.tipo_documento_identificacao_ = tipo_documento_identificacao_;
        this.n_documento_identificacao_ = n_documento_identificacao_;
        this.nif = nif;
        this.data_emissao = data_emissao;
        this.data_validade = data_validade;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.data_de_nascimento_1 = data_de_nascimento_1;
        this.sexo = sexo;
        this.email = email;
        this.telefonetelemovel = telefonetelemovel;
        this.habilitacao_escolar = habilitacao_escolar;
        this.despacho = despacho;
        this.formacao_profissional_ = formacao_profissional_;
        this.carga_horaria = carga_horaria;
        this.instituicao_de_ensino = instituicao_de_ensino;
        this.pais_obtencao = pais_obtencao;
        this.ano_de_inicio = ano_de_inicio;
        this.ano_conclusao = ano_conclusao;
        this.separatorlist_2_id = separatorlist_2_id;
        this.despacho_fk = despacho_fk;
        this.despacho_fk_desc = despacho_fk_desc;
        this.formacao_profissional__fk = formacao_profissional__fk;
        this.formacao_profissional__fk_desc = formacao_profissional__fk_desc;
        this.carga_horaria_fk = carga_horaria_fk;
        this.carga_horaria_fk_desc = carga_horaria_fk_desc;
        this.instituicao_de_ensino_fk = instituicao_de_ensino_fk;
        this.instituicao_de_ensino_fk_desc = instituicao_de_ensino_fk_desc;
        this.pais_obtencao_fk = pais_obtencao_fk;
        this.pais_obtencao_fk_desc = pais_obtencao_fk_desc;
        this.ano_de_inicio_fk = ano_de_inicio_fk;
        this.ano_de_inicio_fk_desc = ano_de_inicio_fk_desc;
        this.ano_conclusao_fk = ano_conclusao_fk;
        this.ano_conclusao_fk_desc = ano_conclusao_fk_desc;
        this.despacho1 = despacho1;
        this.formacao_profissional1 = formacao_profissional1;
        this.idocumeto = idocumeto;
        this.separatorlist_1_id = separatorlist_1_id;
        this.despacho1_fk = despacho1_fk;
        this.despacho1_fk_desc = despacho1_fk_desc;
        this.ficheiropdf_file_uploaded_id = ficheiropdf_file_uploaded_id;
        this.formacao_profissional1_fk = formacao_profissional1_fk;
        this.formacao_profissional1_fk_desc = formacao_profissional1_fk_desc;
        this.tipo_documento_fk = tipo_documento_fk;
        this.tipo_documento_fk_desc = tipo_documento_fk_desc;
        this.ficheiropdf_fk = ficheiropdf_fk;
        this.ficheiropdf_fk_desc = ficheiropdf_fk_desc;
        this.idocumeto_fk = idocumeto_fk;
        this.idocumeto_fk_desc = idocumeto_fk_desc;
    }

    public String getId_requerente() {
        return id_requerente;
    }

    public void setId_requerente(String id_requerente) {
        this.id_requerente = id_requerente;
    }

    public String getId_solicitacao() {
        return id_solicitacao;
    }

    public void setId_solicitacao(String id_solicitacao) {
        this.id_solicitacao = id_solicitacao;
    }

    public String getId_instensino() {
        return id_instensino;
    }

    public void setId_instensino(String id_instensino) {
        this.id_instensino = id_instensino;
    }

    public String getTipo_documento_identificacao_() {
        return tipo_documento_identificacao_;
    }

    public void setTipo_documento_identificacao_(String tipo_documento_identificacao_) {
        this.tipo_documento_identificacao_ = tipo_documento_identificacao_;
    }

    public String getN_documento_identificacao_() {
        return n_documento_identificacao_;
    }

    public void setN_documento_identificacao_(String n_documento_identificacao_) {
        this.n_documento_identificacao_ = n_documento_identificacao_;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getData_validade() {
        return data_validade;
    }

    public void setData_validade(String data_validade) {
        this.data_validade = data_validade;
    }

    public String getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(String data_emissao) {
        this.data_emissao = data_emissao;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_de_nascimento_1() {
        return data_de_nascimento_1;
    }

    public void setData_de_nascimento_1(String data_de_nascimento_1) {
        this.data_de_nascimento_1 = data_de_nascimento_1;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonetelemovel() {
        return telefonetelemovel;
    }

    public void setTelefonetelemovel(String telefonetelemovel) {
        this.telefonetelemovel = telefonetelemovel;
    }

    public String getHabilitacao_escolar() {
        return habilitacao_escolar;
    }

    public void setHabilitacao_escolar(String habilitacao_escolar) {
        this.habilitacao_escolar = habilitacao_escolar;
    }

    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    public String getFormacao_profissional_() {
        return formacao_profissional_;
    }

    public void setFormacao_profissional_(String formacao_profissional_) {
        this.formacao_profissional_ = formacao_profissional_;
    }

    public String getCarga_horaria() {
        return carga_horaria;
    }

    public void setCarga_horaria(String carga_horaria) {
        this.carga_horaria = carga_horaria;
    }

    public String getInstituicao_de_ensino() {
        return instituicao_de_ensino;
    }

    public void setInstituicao_de_ensino(String instituicao_de_ensino) {
        this.instituicao_de_ensino = instituicao_de_ensino;
    }

    public String getPais_obtencao() {
        return pais_obtencao;
    }

    public void setPais_obtencao(String pais_obtencao) {
        this.pais_obtencao = pais_obtencao;
    }

    public String getAno_de_inicio() {
        return ano_de_inicio;
    }

    public void setAno_de_inicio(String ano_de_inicio) {
        this.ano_de_inicio = ano_de_inicio;
    }

    public String getAno_conclusao() {
        return ano_conclusao;
    }

    public void setAno_conclusao(String ano_conclusao) {
        this.ano_conclusao = ano_conclusao;
    }

    public List<String> getSeparatorlist_2_id() {
        return separatorlist_2_id;
    }

    public void setSeparatorlist_2_id(List<String> separatorlist_2_id) {
        this.separatorlist_2_id = separatorlist_2_id;
    }

    public List<String> getDespacho_fk() {
        return despacho_fk;
    }

    public void setDespacho_fk(List<String> despacho_fk) {
        this.despacho_fk = despacho_fk;
    }

    public List<String> getDespacho_fk_desc() {
        return despacho_fk_desc;
    }

    public void setDespacho_fk_desc(List<String> despacho_fk_desc) {
        this.despacho_fk_desc = despacho_fk_desc;
    }

    public List<String> getFormacao_profissional__fk() {
        return formacao_profissional__fk;
    }

    public void setFormacao_profissional__fk(List<String> formacao_profissional__fk) {
        this.formacao_profissional__fk = formacao_profissional__fk;
    }

    public List<String> getFormacao_profissional__fk_desc() {
        return formacao_profissional__fk_desc;
    }

    public void setFormacao_profissional__fk_desc(List<String> formacao_profissional__fk_desc) {
        this.formacao_profissional__fk_desc = formacao_profissional__fk_desc;
    }

    public List<String> getCarga_horaria_fk() {
        return carga_horaria_fk;
    }

    public void setCarga_horaria_fk(List<String> carga_horaria_fk) {
        this.carga_horaria_fk = carga_horaria_fk;
    }

    public List<String> getCarga_horaria_fk_desc() {
        return carga_horaria_fk_desc;
    }

    public void setCarga_horaria_fk_desc(List<String> carga_horaria_fk_desc) {
        this.carga_horaria_fk_desc = carga_horaria_fk_desc;
    }

    public List<String> getInstituicao_de_ensino_fk() {
        return instituicao_de_ensino_fk;
    }

    public void setInstituicao_de_ensino_fk(List<String> instituicao_de_ensino_fk) {
        this.instituicao_de_ensino_fk = instituicao_de_ensino_fk;
    }

    public List<String> getInstituicao_de_ensino_fk_desc() {
        return instituicao_de_ensino_fk_desc;
    }

    public void setInstituicao_de_ensino_fk_desc(List<String> instituicao_de_ensino_fk_desc) {
        this.instituicao_de_ensino_fk_desc = instituicao_de_ensino_fk_desc;
    }

    public List<String> getPais_obtencao_fk() {
        return pais_obtencao_fk;
    }

    public void setPais_obtencao_fk(List<String> pais_obtencao_fk) {
        this.pais_obtencao_fk = pais_obtencao_fk;
    }

    public List<String> getPais_obtencao_fk_desc() {
        return pais_obtencao_fk_desc;
    }

    public void setPais_obtencao_fk_desc(List<String> pais_obtencao_fk_desc) {
        this.pais_obtencao_fk_desc = pais_obtencao_fk_desc;
    }

    public List<String> getAno_de_inicio_fk() {
        return ano_de_inicio_fk;
    }

    public void setAno_de_inicio_fk(List<String> ano_de_inicio_fk) {
        this.ano_de_inicio_fk = ano_de_inicio_fk;
    }

    public List<String> getAno_de_inicio_fk_desc() {
        return ano_de_inicio_fk_desc;
    }

    public void setAno_de_inicio_fk_desc(List<String> ano_de_inicio_fk_desc) {
        this.ano_de_inicio_fk_desc = ano_de_inicio_fk_desc;
    }

    public List<String> getAno_conclusao_fk() {
        return ano_conclusao_fk;
    }

    public void setAno_conclusao_fk(List<String> ano_conclusao_fk) {
        this.ano_conclusao_fk = ano_conclusao_fk;
    }

    public List<String> getAno_conclusao_fk_desc() {
        return ano_conclusao_fk_desc;
    }

    public void setAno_conclusao_fk_desc(List<String> ano_conclusao_fk_desc) {
        this.ano_conclusao_fk_desc = ano_conclusao_fk_desc;
    }

    public String getDespacho1() {
        return despacho1;
    }

    public void setDespacho1(String despacho1) {
        this.despacho1 = despacho1;
    }

    public String getFormacao_profissional1() {
        return formacao_profissional1;
    }

    public void setFormacao_profissional1(String formacao_profissional1) {
        this.formacao_profissional1 = formacao_profissional1;
    }

    public String getIdocumeto() {
        return idocumeto;
    }

    public void setIdocumeto(String idocumeto) {
        this.idocumeto = idocumeto;
    }

    public List<String> getSeparatorlist_1_id() {
        return separatorlist_1_id;
    }

    public void setSeparatorlist_1_id(List<String> separatorlist_1_id) {
        this.separatorlist_1_id = separatorlist_1_id;
    }

    public List<String> getDespacho1_fk() {
        return despacho1_fk;
    }

    public void setDespacho1_fk(List<String> despacho1_fk) {
        this.despacho1_fk = despacho1_fk;
    }

    public List<String> getDespacho1_fk_desc() {
        return despacho1_fk_desc;
    }

    public void setDespacho1_fk_desc(List<String> despacho1_fk_desc) {
        this.despacho1_fk_desc = despacho1_fk_desc;
    }

    public String getFicheiropdf_file_uploaded_id() {
        return ficheiropdf_file_uploaded_id;
    }

    public void setFicheiropdf_file_uploaded_id(String ficheiropdf_file_uploaded_id) {
        this.ficheiropdf_file_uploaded_id = ficheiropdf_file_uploaded_id;
    }

    public List<String> getFormacao_profissional1_fk() {
        return formacao_profissional1_fk;
    }

    public void setFormacao_profissional1_fk(List<String> formacao_profissional1_fk) {
        this.formacao_profissional1_fk = formacao_profissional1_fk;
    }

    public List<String> getFormacao_profissional1_fk_desc() {
        return formacao_profissional1_fk_desc;
    }

    public void setFormacao_profissional1_fk_desc(List<String> formacao_profissional1_fk_desc) {
        this.formacao_profissional1_fk_desc = formacao_profissional1_fk_desc;
    }

    public List<String> getTipo_documento_fk() {
        return tipo_documento_fk;
    }

    public void setTipo_documento_fk(List<String> tipo_documento_fk) {
        this.tipo_documento_fk = tipo_documento_fk;
    }

    public List<String> getTipo_documento_fk_desc() {
        return tipo_documento_fk_desc;
    }

    public void setTipo_documento_fk_desc(List<String> tipo_documento_fk_desc) {
        this.tipo_documento_fk_desc = tipo_documento_fk_desc;
    }

    public String getFicheiropdf_fk() {
        return ficheiropdf_fk;
    }

    public void setFicheiropdf_fk(String ficheiropdf_fk) {
        this.ficheiropdf_fk = ficheiropdf_fk;
    }

    public List<String> getFicheiropdf_fk_desc() {
        return ficheiropdf_fk_desc;
    }

    public void setFicheiropdf_fk_desc(List<String> ficheiropdf_fk_desc) {
        this.ficheiropdf_fk_desc = ficheiropdf_fk_desc;
    }

    public List<String> getIdocumeto_fk() {
        return idocumeto_fk;
    }

    public void setIdocumeto_fk(List<String> idocumeto_fk) {
        this.idocumeto_fk = idocumeto_fk;
    }

    public List<String> getIdocumeto_fk_desc() {
        return idocumeto_fk_desc;
    }

    public void setIdocumeto_fk_desc(List<String> idocumeto_fk_desc) {
        this.idocumeto_fk_desc = idocumeto_fk_desc;
    }
}
