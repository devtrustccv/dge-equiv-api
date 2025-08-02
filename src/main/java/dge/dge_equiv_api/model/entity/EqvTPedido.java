package dge.dge_equiv_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eqv_t_pedido")
public class EqvTPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requerente")
    private EqvTRequerente requerente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inst_ensino")
    private EqvTInstEnsino instEnsino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requisicao")
    private EqvTRequisicao requisicao;

    @Column(name = "formacao_prof", length = 255)
    private String formacaoProf;

    private Integer carga;

    @Column(name = "ano_inicio", precision = 10, scale = 2)
    private BigDecimal anoInicio;

    @Column(name = "ano_fim", precision = 10, scale = 2)
    private BigDecimal anoFim;

    private Integer Status;

    private String Etapa;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EqvtTDecisaoVp> decisoesVp;


    @OneToMany(mappedBy = "idPedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EqvtTDecisaoAp> decisoesAp;


    public List<EqvtTDecisaoAp> getDecisoesAp() {
        return decisoesAp;
    }

    public void setDecisoesAp(List<EqvtTDecisaoAp> decisoesAp) {
        this.decisoesAp = decisoesAp;
    }

    public List<EqvtTDecisaoVp> getDecisoesVp() {
        return decisoesVp;
    }

    public void setDecisoesVp(List<EqvtTDecisaoVp> decisoesVp) {
        this.decisoesVp = decisoesVp;
    }

    public String getEtapa() {
        return Etapa;
    }

    public void setEtapa(String etapa) {
        Etapa = etapa;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EqvTRequerente getRequerente() {
        return requerente;
    }

    public void setRequerente(EqvTRequerente requerente) {
        this.requerente = requerente;
    }

    public EqvTInstEnsino getInstEnsino() {
        return instEnsino;
    }

    public void setInstEnsino(EqvTInstEnsino instEnsino) {
        this.instEnsino = instEnsino;
    }

    public EqvTRequisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(EqvTRequisicao requisicao) {
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

    private Integer nivel;

    private String familia;

    private Integer despacho;

    @Column(name = "num_declaracao", length = 255)
    private String numDeclaracao;

    @Column(name = "data_despacho")
    private LocalDate dataDespacho;


}
