package dge.dge_equiv_api.infrastructure.primary;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "eqv_t_tipo_documento", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvTTipoDocumento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    private String status;

    @Column(name = "obrigatorio")
    private Integer obrigatorio;

    @Column(name = "processo")
    private String processo;

    @Column(name = "etapa")
    private String etapa;

}
