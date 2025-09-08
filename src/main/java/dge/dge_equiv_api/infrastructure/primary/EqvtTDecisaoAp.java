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

}

