package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "eqv_t_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvTPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_pedido", foreignKey = @ForeignKey(name = "eqv_t_pagamento_id_pedido_fkey"))
    private EqvTPedido idPedido;
    @ManyToOne
    @JoinColumn(name = "id_requisicao", foreignKey = @ForeignKey(name = "eqv_t_pagamento_id_requisicao_fkey"))
    private EqvTRequisicao idRequisicao;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "nr_processo")
    private Integer nrProcesso;

    @Column(name = "nu_cheque", length = 255)
    private String nuCheque;

    @Column(name = "banco_id")
    private Integer bancoId;

    @Column(name = "id_task")
    private Integer idTask;

    @Column(name = "tipo_pagamento", length = 255)
    private String tipoPagamento;

    @Column(name = "link_duc", length = 255)
    private String linkDuc;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "user_registro")
    private Integer userRegistro;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "id_taxa")
    private Integer idTaxa;

    @Column(name = "nu_duc")
    private BigDecimal nuDuc;

    @Column(name = "referencia")
    private BigDecimal referencia;

    @Column(name = "entidade")
    private String entidade;

    @Column(name = "data_prev_pag")
    private LocalDate dataPrevPag;

    @Column(name = "duc_agregado")
    private BigDecimal ducAgregado;

    @Column(name = "dt_update")
    private LocalDate dtUpdate;

    @Column(name = "user_pag_id")
    private BigDecimal userPagId;

    @Column(name = "dmtipopagamento")
    private String dmTipoPagamentoLegacy; // existe duas colunas parecidas, diferenciei no nome

    @Column(name = "dm_tipo_pagamento")
    private String dmTipoPagamento;

    @Column(name = "data_checked")
    private LocalDate dataChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EqvTPedido getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(EqvTPedido idPedido) {
        this.idPedido = idPedido;
    }

    public EqvTRequisicao getIdRequisicao() {
        return idRequisicao;
    }

    public void setIdRequisicao(EqvTRequisicao idRequisicao) {
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

    public Integer getBancoId() {
        return bancoId;
    }

    public void setBancoId(Integer bancoId) {
        this.bancoId = bancoId;
    }

    public String getNuCheque() {
        return nuCheque;
    }

    public void setNuCheque(String nuCheque) {
        this.nuCheque = nuCheque;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public String getLinkDuc() {
        return linkDuc;
    }

    public void setLinkDuc(String linkDuc) {
        this.linkDuc = linkDuc;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getUserRegistro() {
        return userRegistro;
    }

    public void setUserRegistro(Integer userRegistro) {
        this.userRegistro = userRegistro;
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