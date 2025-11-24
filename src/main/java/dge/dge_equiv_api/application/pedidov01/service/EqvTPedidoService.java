package dge.dge_equiv_api.application.pedidov01.service;

import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;

import java.util.List;

public interface EqvTPedidoService {

    List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId);

    List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId);
    List<EqvtPedidoDTO> findPedidosByRequisicaoId(Integer idRequisicao);
    CertificadoEquivalenciaDTO montarCertificado(EqvtPedidoDTO pedidoDTO);
    List<CertificadoEquivalenciaDTO> montarCertificadosPorRequisicao(Integer idRequisicao);
    PedidoSimplesDTO convertToSimplesDTO(EqvtPedidoDTO pedido);
}