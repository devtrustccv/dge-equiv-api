package dge.dge_equiv_api.model.dto;

import java.util.List;

public class PortalPedidosRespostaDTO {

    private EqvTRequisicaoDTO requisicao;
    private EqvTRequerenteDTO requerente;
    private List<EqvtPedidoDTO> pedidos;


    public EqvTRequisicaoDTO getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(EqvTRequisicaoDTO requisicao) {
        this.requisicao = requisicao;
    }

    public EqvTRequerenteDTO getRequerente() {
        return requerente;
    }

    public void setRequerente(EqvTRequerenteDTO requerente) {
        this.requerente = requerente;
    }

    public List<EqvtPedidoDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<EqvtPedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }


}
