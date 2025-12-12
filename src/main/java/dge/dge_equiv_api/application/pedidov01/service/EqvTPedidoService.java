package dge.dge_equiv_api.application.pedidov01.service;

import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.application.pedidov01.dto.*;

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
            PortalPedidosDTO portalPedidosDTO, String numeroProcesso);

    /**
     * Retorna os pedidos de uma requisição, incluindo documentos.
     */
    List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId);



    /**
     * Retorna pedidos com documentos por número de processo
     */
    ProcessoPedidosDocumentosDTO getPedidosComDocumentosPorProcesso(String numeroProcesso);






}