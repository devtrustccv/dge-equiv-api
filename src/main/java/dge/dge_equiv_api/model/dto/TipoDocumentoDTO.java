package dge.dge_equiv_api.model.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class TipoDocumentoDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private String status;
    private Integer obrigatorio;
    private String processo;
    private String etapa;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Integer obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }
}

