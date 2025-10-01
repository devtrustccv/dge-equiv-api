package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "eqv_t_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvTPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_pedido", foreignKey = @ForeignKey(name = "eqv_t_pagamento_id_pedido_fkey"))
    private EqvTPedido idPedido;
    @ManyToOne
    @JoinColumn(name = "id_requisicao", foreignKey = @ForeignKey(name = "eqv_t_pagamento_id_requisicao_fkey"))
    private EqvTRequisicao idRequisicao;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "nr_processo")
    private Integer nrProcesso;

    @Column(name = "nu_cheque", length = 255)
    private String nuCheque;

    @Column(name = "banco_id")
    private Integer bancoId;

    @Column(name = "id_task")
    private Integer idTask;

    @Column(name = "tipo_pagamento", length = 255)
    private String tipoPagamento;

    @Column(name = "link_duc", length = 255)
    private String linkDuc;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "user_registro")
    private Integer userRegistro;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "id_taxa")
    private Integer idTaxa;

    @Column(name = "nu_duc")
    private BigDecimal nuDuc;

    @Column(name = "referencia")
    private BigDecimal referencia;

    @Column(name = "entidade")
    private String entidade;

    @Column(name = "data_prev_pag")
    private LocalDate dataPrevPag;

    @Column(name = "duc_agregado")
    private BigDecimal ducAgregado;

    @Column(name = "dt_update")
    private LocalDate dtUpdate;

    @Column(name = "user_pag_id")
    private BigDecimal userPagId;

    @Column(name = "dm_tipo_pagamento")
    private String dmTipoPagamento;

    @Column(name = "data_checked")
    private LocalDate dataChecked;
    @Column(name = "etapa")
    private  String etapa;


}