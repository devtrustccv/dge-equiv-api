package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eqv_t_pedido")
public class EqvTPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requerente")
    private EqvTRequerente requerente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inst_ensino")
    private EqvTInstEnsino instEnsino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requisicao")
    private EqvTRequisicao requisicao;

    @Column(name = "formacao_prof", length = 255)
    private String formacaoProf;

    private Integer carga;

    @Column(name = "ano_inicio", precision = 10, scale = 2)
    private BigDecimal anoInicio;

    @Column(name = "ano_fim", precision = 10, scale = 2)
    private BigDecimal anoFim;

    private Integer Status;

    private String Etapa;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EqvtTDecisaoVp> decisoesVp;

    @OneToMany(mappedBy = "idPedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EqvtTDecisaoAp> decisoesAp;

    private Integer nivel;

    private String familia;

    private Integer despacho;

    @Column(name = "num_declaracao", length = 255)
    private String numDeclaracao;

    @Column(name = "data_despacho")
    private LocalDate dataDespacho;




}
