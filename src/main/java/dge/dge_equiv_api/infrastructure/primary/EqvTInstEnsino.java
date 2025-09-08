package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eqv_t_inst_ensino")
public class EqvTInstEnsino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(length = 255)
    private String endereco;

    private String email;

    private Integer contato;

    private String pais;

    private String status;

    @Column(name = "date_create")
    private LocalDate dateCreate;


}
