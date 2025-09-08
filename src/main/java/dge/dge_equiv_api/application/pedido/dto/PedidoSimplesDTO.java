package dge.dge_equiv_api.application.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PedidoSimplesDTO {
    private Integer id;
    private String formacaoProf;
    private Integer carga;
    private BigDecimal anoInicio;
    private BigDecimal anoFim;
    private Integer nivel;
    private String familia;
    private String despacho;
    private String numDeclaracao;
    private LocalDate dataDespacho;

}
