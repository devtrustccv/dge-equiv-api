package dge.dge_equiv_api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PedidoSimplesDTO {
    private Integer id;
    private String formacaoProf;
    private Integer carga;
    private BigDecimal anoInicio;
    private BigDecimal anoFim;
    private Integer nivel;
    private String familia;
    private String despacho;
    private String numDeclaracao;
    private LocalDate dataDespacho;
    // getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormacaoProf() {
        return formacaoProf;
    }

    public void setFormacaoProf(String formacaoProf) {
        this.formacaoProf = formacaoProf;
    }

    public Integer getCarga() {
        return carga;
    }

    public void setCarga(Integer carga) {
        this.carga = carga;
    }

    public BigDecimal getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(BigDecimal anoFim) {
        this.anoFim = anoFim;
    }

    public BigDecimal getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(BigDecimal anoInicio) {
        this.anoInicio = anoInicio;
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

    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    public LocalDate getDataDespacho() {
        return dataDespacho;
    }

    public void setDataDespacho(LocalDate dataDespacho) {
        this.dataDespacho = dataDespacho;
    }

    public String getNumDeclaracao() {
        return numDeclaracao;
    }

    public void setNumDeclaracao(String numDeclaracao) {
        this.numDeclaracao = numDeclaracao;
    }
}
