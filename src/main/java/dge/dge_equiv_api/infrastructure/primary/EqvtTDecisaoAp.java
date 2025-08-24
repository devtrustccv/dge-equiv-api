package dge.dge_equiv_api.infrastructure.primary;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "eqvt_t_decisao_ap", schema = "public")
@Getter
@Setter
public class EqvtTDecisaoAp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_pedido", foreignKey = @ForeignKey(name = "eqvt_t_decisao_ap_id_pedido_fkey"))
    private EqvTPedido idPedido;
    @Column(name = "decisao")
    private Integer decisao;
    @Column(name = "obs")
    private String obs;
    @Column(name = "user_create")
    private Integer userCreate;
    @Column(name = "user_update")
    private Integer userUpdate;
    @Column(name = "data_create")
    private LocalDate dataCreate;
    @Column(name = "data_update")
    private LocalDate dataUpdate;
    @Size(max = 255)
    @Column(name = "familia")
    private String familia;
    @Column(name = "parecer_cnep")
    private String parecerCnep;
    @Column(name = "ata")
    private String ata;
    @Column(name = "nivel")
    private Integer nivel;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EqvTPedido getIdPedido() {
        return this.idPedido;
    }

    public void setIdPedido(EqvTPedido idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getDecisao() {
        return this.decisao;
    }

    public void setDecisao(Integer decisao) {
        this.decisao = decisao;
    }

    public String getObs() {
        return this.obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Integer getUserCreate() {
        return this.userCreate;
    }

    public void setUserCreate(Integer userCreate) {
        this.userCreate = userCreate;
    }

    public Integer getUserUpdate() {
        return this.userUpdate;
    }

    public void setUserUpdate(Integer userUpdate) {
        this.userUpdate = userUpdate;
    }

    public LocalDate getDataCreate() {
        return this.dataCreate;
    }

    public void setDataCreate(LocalDate dataCreate) {
        this.dataCreate = dataCreate;
    }

    public LocalDate getDataUpdate() {
        return this.dataUpdate;
    }

    public void setDataUpdate(LocalDate dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public String getFamilia() {
        return this.familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getParecerCnep() {
        return this.parecerCnep;
    }

    public void setParecerCnep(String parecerCnep) {
        this.parecerCnep = parecerCnep;
    }

    public String getAta() {
        return this.ata;
    }

    public void setAta(String ata) {
        this.ata = ata;
    }

    public Integer getNivel() {
        return this.nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}

