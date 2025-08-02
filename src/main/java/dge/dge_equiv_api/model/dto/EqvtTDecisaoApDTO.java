package dge.dge_equiv_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqvtTDecisaoApDTO {

    private Integer id;
    private EqvtPedidoDTO idPedido;         // id_pedido
    private Integer decisao;
    private String obs;
    private String familia;
    private String parecerCnep;
    private Integer nivel;


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

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Integer getDecisao() {
        return decisao;
    }

    public void setDecisao(Integer decisao) {
        this.decisao = decisao;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getParecerCnep() {
        return parecerCnep;
    }

    public void setParecerCnep(String parecerCnep) {
        this.parecerCnep = parecerCnep;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}
