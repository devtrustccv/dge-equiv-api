package dge.dge_equiv_api.infrastructure.primary;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "eqvt_t_decisao_vp", schema = "public")
@Getter
@Setter
public class EqvtTDecisaoVp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false,
            foreignKey = @ForeignKey(name = "eqvt_t_decisao_vp_id_pedido_fkey"))
    private EqvTPedido pedido;

    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "familia", length = 255)
    private String familia;

    @Column(name = "decisao")
    private Integer decisao;

    @Column(name = "data_create")
    private LocalDate dataCreate;

    @Column(name = "user_create")
    private Integer userCreate;

    @Column(name = "data_update")
    private LocalDate dataUpdate;

    @Column(name = "user_update")
    private Integer userUpdate;

    @Column(name = "obs_vp")
    private String obsVp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EqvTPedido getPedido() {
        return pedido;
    }

    public void setPedido(EqvTPedido pedido) {
        this.pedido = pedido;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getDecisao() {
        return decisao;
    }

    public void setDecisao(Integer decisao) {
        this.decisao = decisao;
    }

    public LocalDate getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(LocalDate dataCreate) {
        this.dataCreate = dataCreate;
    }

    public Integer getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Integer userCreate) {
        this.userCreate = userCreate;
    }

    public LocalDate getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(LocalDate dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public Integer getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(Integer userUpdate) {
        this.userUpdate = userUpdate;
    }

    public String getObsVp() {
        return obsVp;
    }

    public void setObsVp(String obsVp) {
        this.obsVp = obsVp;
    }
}

