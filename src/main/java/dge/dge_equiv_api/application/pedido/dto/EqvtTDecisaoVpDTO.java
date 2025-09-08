package dge.dge_equiv_api.application.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EqvtTDecisaoVpDTO {
    private Integer id;
    private EqvtPedidoDTO idPedido;
    private Integer nivel;
    private String familia;
    private Integer decisao;
    private String obsVp;

}

