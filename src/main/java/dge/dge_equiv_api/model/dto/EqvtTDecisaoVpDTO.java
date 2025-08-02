package dge.dge_equiv_api.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EqvtTDecisaoVpDTO {
    private Integer id;
    private EqvtPedidoDTO idPedido;
    private Integer nivel;
    private String familia;
    private Integer decisao;
    private String obsVp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EqvtPedidoDTO getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(EqvtPedidoDTO idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public Integer getDecisao() {
        return decisao;
    }

    public void setDecisao(Integer decisao) {
        this.decisao = decisao;
    }
    public String getObsVp() {
        return obsVp;
    }

    public void setObsVp(String obsVp) {
        this.obsVp = obsVp;
    }
}

