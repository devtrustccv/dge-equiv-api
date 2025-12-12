package dge.dge_equiv_api.infrastructure.primary;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor

@Table(name = "eqv_t_reclamacao", schema = "public")
public class EqvTReclamacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id")
    @JsonIgnoreProperties({"requerente", "requisicao", "decisoesVp", "decisoesAp", "instEnsino"})
    private EqvTPedido idPedido;


    @ManyToOne
    @JoinColumn(name = "id_requisicao", referencedColumnName = "id")
    private EqvTRequisicao idRequisicao;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Column(name = "decisao")
    private Integer decisao;

    @Column(name = "anexo", length = 255)
    private String anexo;

    @Column(name = "user_create", length = 100)
    private Integer userCreate;

    @Column(name = "user_update", length = 100)
    private Integer userUpdate;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    // Construtores
    public EqvTReclamacao() {
    }


}