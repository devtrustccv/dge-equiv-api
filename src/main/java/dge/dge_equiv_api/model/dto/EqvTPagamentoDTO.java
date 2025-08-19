package dge.dge_equiv_api.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvTPagamentoDTO {

    private Integer id;
    private Integer idPedido;
    private Integer idRequisicao;
    private BigDecimal total;
    private Integer estado;
    private Integer nrProcesso;
    private String nuCheque;
    private Integer bancoId;
    private Integer idTask;
    private String tipoPagamento;
    private String linkDuc;
    private LocalDate dataPagamento;
    private Integer userRegistro;
    private LocalDate dataRegistro;
    private Integer idTaxa;
    private BigDecimal nuDuc;
    private BigDecimal referencia;
    private String entidade;
    private LocalDate dataPrevPag;
    private BigDecimal ducAgregado;
    private LocalDate dtUpdate;
    private BigDecimal userPagId;
    private String dmTipoPagamentoLegacy;
    private String dmTipoPagamento;
    private LocalDate dataChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getIdRequisicao() {
        return idRequisicao;
    }

    public void setIdRequisicao(Integer idRequisicao) {
        this.idRequisicao = idRequisicao;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getNrProcesso() {
        return nrProcesso;
    }

    public void setNrProcesso(Integer nrProcesso) {
        this.nrProcesso = nrProcesso;
    }

    public String getNuCheque() {
        return nuCheque;
    }

    public void setNuCheque(String nuCheque) {
        this.nuCheque = nuCheque;
    }

    public Integer getBancoId() {
        return bancoId;
    }

    public void setBancoId(Integer bancoId) {
        this.bancoId = bancoId;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getLinkDuc() {
        return linkDuc;
    }

    public void setLinkDuc(String linkDuc) {
        this.linkDuc = linkDuc;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getUserRegistro() {
        return userRegistro;
    }

    public void setUserRegistro(Integer userRegistro) {
        this.userRegistro = userRegistro;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getIdTaxa() {
        return idTaxa;
    }

    public void setIdTaxa(Integer idTaxa) {
        this.idTaxa = idTaxa;
    }

    public BigDecimal getNuDuc() {
        return nuDuc;
    }

    public void setNuDuc(BigDecimal nuDuc) {
        this.nuDuc = nuDuc;
    }

    public BigDecimal getReferencia() {
        return referencia;
    }

    public void setReferencia(BigDecimal referencia) {
        this.referencia = referencia;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public LocalDate getDataPrevPag() {
        return dataPrevPag;
    }

    public void setDataPrevPag(LocalDate dataPrevPag) {
        this.dataPrevPag = dataPrevPag;
    }

    public BigDecimal getDucAgregado() {
        return ducAgregado;
    }

    public void setDucAgregado(BigDecimal ducAgregado) {
        this.ducAgregado = ducAgregado;
    }

    public LocalDate getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDate dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public BigDecimal getUserPagId() {
        return userPagId;
    }

    public void setUserPagId(BigDecimal userPagId) {
        this.userPagId = userPagId;
    }

    public String getDmTipoPagamentoLegacy() {
        return dmTipoPagamentoLegacy;
    }

    public void setDmTipoPagamentoLegacy(String dmTipoPagamentoLegacy) {
        this.dmTipoPagamentoLegacy = dmTipoPagamentoLegacy;
    }

    public String getDmTipoPagamento() {
        return dmTipoPagamento;
    }

    public void setDmTipoPagamento(String dmTipoPagamento) {
        this.dmTipoPagamento = dmTipoPagamento;
    }

    public LocalDate getDataChecked() {
        return dataChecked;
    }

    public void setDataChecked(LocalDate dataChecked) {
        this.dataChecked = dataChecked;
    }
}
