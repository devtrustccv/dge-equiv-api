package dge.dge_equiv_api.application.pedidov01.service;

import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.application.pedido.dto.*;

import java.util.List;

public interface EqvTPedidoService {

    /**
     * Cria o lote de pedidos com requisição e requerente únicos.
     */
    List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId);

    /**
     * Atualiza o lote de pedidos com requisição e requerente únicos.
     */
    List<EqvtPedidoDTO> updateLotePedidosComRequisicao(
            Integer requisicaoId,
            PortalPedidosDTO portalPedidosDTO,String numeroProcesso);

    /**
     * Retorna os pedidos de uma requisição, incluindo documentos.
     */
    List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId);

    /**
     * Retorna os pedidos de uma requisição sem documentos.
     */
    List<EqvtPedidoDTO> findPedidosByRequisicaoId(Integer idRequisicao);

    /**
     * Monta o DTO do certificado de equivalência para um pedido.
     */
    CertificadoEquivalenciaDTO montarCertificado(EqvtPedidoDTO pedidoDTO);

    /**
     * Monta os certificados de equivalência para todos os pedidos de uma requisição.
     */
    List<CertificadoEquivalenciaDTO> montarCertificadosPorRequisicao(Integer idRequisicao);

    /**
     * Converte um pedido completo para DTO simples.
     */
    PedidoSimplesDTO convertToSimplesDTO(EqvtPedidoDTO pedido);
}