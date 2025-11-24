//package dge.dge_equiv_api.domain.documento.bussiness;
//
//import dge.dge_equiv_api.application.pedido.dto.TipoDocumentoDTO;
//import dge.dge_equiv_api.application.pedidov01.mapper.DocumentoMapper;
//import dge.dge_equiv_api.application.pedidov01.service.EqvTTipoDocumentoService;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TipoDocumentoBusinessImpl implements TipoDocumentoBusiness {
//
//    private final EqvTTipoDocumentoService service;
//    private final DocumentoMapper mapper;
//
//    public TipoDocumentoBusinessImpl(EqvTTipoDocumentoService service, DocumentoMapper mapper) {
//        this.service = service;
//        this.mapper = mapper;
//    }
//
//    @Override
//    public TipoDocumentoDTO buscarPorId(Integer id) {
//        return mapper.toDto(service.buscarPorId(id));
//    }
//}
//
