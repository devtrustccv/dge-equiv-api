package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

@Table(name = "eqv_t_tipo_documento")
public class TipoDocumentoEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "status")
    private String status;
    @Column(name = "obrigatorio")
    private Integer obrigatorio;
    @Column(name = "processo")
    private String processo;
    @Column(name = "etapa")
    private String etapa;
    @Column(name = "user_create")
    private Integer userCreate;
    @Column(name = "user_update")
    private Integer userUpdate;
    @Column(name = "date_create")
    private LocalDate dateCreate;
    @Column(name = "date_update")
    private LocalDate dateUpdate;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getObrigatorio() {
        return this.obrigatorio;
    }

    public void setObrigatorio(Integer obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String getProcesso() {
        return this.processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getEtapa() {
        return this.etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public Integer getUserCreate() {
        return this.userCreate;
    }

    public void setUserCreate(Integer userCreate) {
        this.userCreate = userCreate;
    }

    public Integer getUserUpdate() {
        return this.userUpdate;
    }

    public void setUserUpdate(Integer userUpdate) {
        this.userUpdate = userUpdate;
    }

    public LocalDate getDateCreate() {
        return this.dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return this.dateUpdate;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
