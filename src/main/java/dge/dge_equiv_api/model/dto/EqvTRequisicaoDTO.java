package dge.dge_equiv_api.model.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EqvTRequisicaoDTO {

    private Integer id;
    private Integer nProcesso;
    private LocalDate dataCreate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getnProcesso() {
        return nProcesso;
    }

    public void setnProcesso(Integer nProcesso) {
        this.nProcesso = nProcesso;
    }

    public LocalDate getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(LocalDate dataCreate) {
        this.dataCreate = dataCreate;
    }
}
