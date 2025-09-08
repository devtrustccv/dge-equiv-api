package dge.dge_equiv_api.application.pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PortalPedidosRespostaDTO {

    private EqvTRequisicaoDTO requisicao;
    private EqvTRequerenteDTO requerente;
    private List<EqvtPedidoDTO> pedidos;




}
