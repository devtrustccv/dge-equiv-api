package dge.dge_equiv_api.application.pedido.service;


import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;

import java.util.List;

public interface PedidoService {

    // Create operations
    List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId);

    // Read operations
    List<EqvtPedidoDTO> findAll();
    EqvtPedidoDTO findById(Integer id);
    List<EqvtPedidoDTO> findPedidosByRequisicaoId(Integer idRequisicao);
    List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId);

    // Certificate operations
    CertificadoEquivalenciaDTO montarCertificado(EqvtPedidoDTO pedidoDTO);
    List<CertificadoEquivalenciaDTO> montarCertificadosPorRequisicao(Integer idRequisicao);

    // Utility operations
    PedidoSimplesDTO convertToSimplesDTO(EqvtPedidoDTO pedido);
}
