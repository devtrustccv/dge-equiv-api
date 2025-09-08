package dge.dge_equiv_api.infrastructure.primary;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "eqv_t_param_report")
public class EqvTParamReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Size(max = 255)
    @Column(name = "caixa_postal")
    private String caixaPostal;
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "republica")
    private String republica;
    @Size(max = 255)
    @Column(name = "rua")
    private String rua;
    @Size(max = 255)
    @Column(name = "telefone")
    private String telefone;
}