package dge.dge_equiv_api.domain.pedido.business;

import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.application.pedido.dto.EqvTInstEnsinoDTO;
import dge.dge_equiv_api.application.pedido.dto.EqvTRequerenteDTO;
import dge.dge_equiv_api.application.pedido.dto.EqvtPedidoDTO;

import dge.dge_equiv_api.infrastructure.primary.EqvTInstEnsino;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequerente;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import java.util.List;
import java.util.Map;

public interface EqvTPedidoBusiness {
    void validateInputs(List<EqvtPedidoDTO> pedidosDTO, EqvTRequerenteDTO requerenteDTO, Integer pessoaId);

    EqvTInstEnsino processarInstituicaoEnsino(EqvTInstEnsinoDTO dto);

    List<EqvTPedido> processarPedidos(List<EqvtPedidoDTO> pedidosDTO, EqvTRequerente requerente,
                                      EqvTRequisicao requisicao, Map<Integer, EqvTInstEnsino> instituicoes);

    void salvarDocumentosDoPedido(EqvtPedidoDTO dto, EqvTPedido pedido);

    AcompanhamentoDTO montarAcompanhamentoDTO(EqvTRequisicao requisicao, List<EqvTPedido> pedidos, Integer pessoaId);

    String iniciarProcessoComValidacao(EqvTRequerente requerente, List<EqvTPedido> pedidos);
}