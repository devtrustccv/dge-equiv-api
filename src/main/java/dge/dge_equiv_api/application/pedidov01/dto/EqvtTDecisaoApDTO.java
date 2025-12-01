package dge.dge_equiv_api.application.pedidov01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqvtTDecisaoApDTO {

    private Integer id;
    private EqvtPedidoDTO idPedido;         // id_pedido
    private Integer decisao;
    private String obs;
    private String familia;
    private String parecerCnep;
    private Integer nivel;


}
