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
@Table(name = "eqv_t_requisicao")
public class EqvTRequisicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer status;

    private Integer etapa;

    @Column(name = "n_processo")
    private Integer nProcesso;

    @Column(name = "user_create")
    private Integer userCreate;


    @Column(name = "user_update")
    private Integer userUpdate;

    @Column(name = "data_create")
    private LocalDate dataCreate;

    @Column(name = "data_update")
    private LocalDate dataUpdate;
    @Column(name = "id_pessoa")
    private  Integer idPessoa;


}
