package dge.dge_equiv_api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eqv_t_requerente")
public class EqvTRequerente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNif() {
        return nif;
    }

    public void setNif(Integer nif) {
        this.nif = nif;
    }

    public String getDocNumero() {
        return docNumero;
    }

    public void setDocNumero(String docNumero) {
        this.docNumero = docNumero;
    }

    public LocalDate getDataEmissaoDoc() {
        return dataEmissaoDoc;
    }

    public void setDataEmissaoDoc(LocalDate dataEmissaoDoc) {
        this.dataEmissaoDoc = dataEmissaoDoc;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataValidadeDoc() {
        return dataValidadeDoc;
    }

    public void setDataValidadeDoc(LocalDate dataValidadeDoc) {
        this.dataValidadeDoc = dataValidadeDoc;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
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

    public Integer getContato() {
        return contato;
    }

    public void setContato(Integer contato) {
        this.contato = contato;
    }

    public Integer getHabilitacao() {
        return habilitacao;
    }

    public void setHabilitacao(Integer habilitacao) {
        this.habilitacao = habilitacao;
    }

    public String getDocIdentificacao() {
        return docIdentificacao;
    }

    public void setDocIdentificacao(String docIdentificacao) {
        this.docIdentificacao = docIdentificacao;
    }

    public Integer getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(Integer userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Integer getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Integer userCreate) {
        this.userCreate = userCreate;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(LocalDate dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer nif;

    @Column(name = "doc_numero")
    private String docNumero;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "data_emissao_doc")
    private LocalDate dataEmissaoDoc;

    @Column(name = "data_validade_doc")
    private LocalDate dataValidadeDoc;

    private String nacionalidade;

    private String sexo;

    private String email;

    private Integer contato;

    private Integer habilitacao;

    @Column(name = "doc_identificacao")
    private String docIdentificacao;

    @Column(name = "user_create")
    private Integer userCreate;

    @Column(name = "user_update")
    private Integer userUpdate;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "data_update")
    private LocalDate dataUpdate;

}
