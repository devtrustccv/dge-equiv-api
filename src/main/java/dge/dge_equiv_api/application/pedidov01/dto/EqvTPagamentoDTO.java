package dge.dge_equiv_api.application.pedidov01.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvTPagamentoDTO {

    private Integer id;
    private Integer idPedido;
    private Integer idRequisicao;
    private BigDecimal total;
    private Integer estado;
    private Integer nrProcesso;
    private String nuCheque;
    private Integer bancoId;
    private Integer idTask;
    private String tipoPagamento;
    private String linkDuc;
    private LocalDate dataPagamento;
    private Integer userRegistro;
    private LocalDate dataRegistro;
    private Integer idTaxa;
    private BigDecimal nuDuc;
    private BigDecimal referencia;
    private String entidade;
    private LocalDate dataPrevPag;
    private BigDecimal ducAgregado;
    private LocalDate dtUpdate;
    private BigDecimal userPagId;
    private String dmTipoPagamentoLegacy;
    private String dmTipoPagamento;
    private LocalDate dataChecked;


}
