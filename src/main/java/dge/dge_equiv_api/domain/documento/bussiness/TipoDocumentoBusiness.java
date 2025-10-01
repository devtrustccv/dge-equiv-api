package dge.dge_equiv_api.domain.documento.bussiness;

import dge.dge_equiv_api.application.pedido.dto.TipoDocumentoDTO;

public interface TipoDocumentoBusiness {

    TipoDocumentoDTO buscarPorId(Integer id);
}
