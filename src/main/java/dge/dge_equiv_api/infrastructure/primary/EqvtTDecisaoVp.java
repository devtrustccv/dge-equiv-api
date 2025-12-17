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

    @Column(name = "motivo_retificado")
    private String motivoRetificado;

}

