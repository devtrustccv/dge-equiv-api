package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "eqvt_t_decisao_despacho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvtTDecisaoDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private EqvTPedido idPedido;

    private Integer decisao;

    @Column(name = "obs_despacho")
    private String obsDespacho;

    @Column(name = "user_create")
    private Integer userCreate;

    @Column(name = "user_update")
    private Integer userUpdate;

    @Column(name = "data_create")
    private LocalDate dataCreate;

    @Column(name = "data_update")
    private LocalDate dataUpdate;
}
