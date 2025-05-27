package dge.dge_equiv_api.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EqvtPedidoDTO {
    private Integer id;
    private EqvTRequerenteDTO requerente;
    private EqvTInstEnsinoDTO instEnsino;
    private EqvTRequisicaoDTO requisicao;
    private String formacaoProf;
    private Integer carga;
    private BigDecimal anoInicio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EqvTRequerenteDTO getRequerente() {
        return requerente;
    }

    public void setRequerente(EqvTRequerenteDTO requerente) {
        this.requerente = requerente;
    }

    public EqvTInstEnsinoDTO getInstEnsino() {
        return instEnsino;
    }

    public void setInstEnsino(EqvTInstEnsinoDTO instEnsino) {
        this.instEnsino = instEnsino;
    }

    public EqvTRequisicaoDTO getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(EqvTRequisicaoDTO requisicao) {
        this.requisicao = requisicao;
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

    public BigDecimal getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(BigDecimal anoInicio) {
        this.anoInicio = anoInicio;
    }

    public BigDecimal getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(BigDecimal anoFim) {
        this.anoFim = anoFim;
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

    public Integer getDespacho() {
        return despacho;
    }

    public void setDespacho(Integer despacho) {
        this.despacho = despacho;
    }

    public String getNumDeclaracao() {
        return numDeclaracao;
    }

    public void setNumDeclaracao(String numDeclaracao) {
        this.numDeclaracao = numDeclaracao;
    }

    public LocalDate getDataDespacho() {
        return dataDespacho;
    }

    public void setDataDespacho(LocalDate dataDespacho) {
        this.dataDespacho = dataDespacho;
    }

    private BigDecimal anoFim;
    private Integer nivel;
    private String familia;
    private Integer despacho;
    private String numDeclaracao;
    private LocalDate dataDespacho;


}
