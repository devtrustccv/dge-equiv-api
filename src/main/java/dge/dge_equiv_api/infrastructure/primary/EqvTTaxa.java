package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "eqv_t_taxa")
public class EqvTTaxa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String estado;

    @Column(length = 100)
    private String etapa;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "user_create")
    private Integer userCreate;

    @Column(name = "user_update")
    private Integer userUpdate;

    @Column(name = "data_create")
    private LocalDate dataCreate;

    @Column(name = "data_update")
    private LocalDate dataUpdate;
}

