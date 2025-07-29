package dge.dge_equiv_api.model.dto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EqvTInstEnsinoDTO {

    private Integer id;
    private String nome;
    private LocalDate dateCreate;
    private Integer UserCreate;
    private String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getUserCreate() {
        return UserCreate;
    }

    public void setUserCreate(Integer userCreate) {
        UserCreate = userCreate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    private String pais;

}
