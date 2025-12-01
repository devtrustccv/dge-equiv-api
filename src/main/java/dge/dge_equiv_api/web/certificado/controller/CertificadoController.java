//package dge.dge_equiv_api.web.certificado.controller;
//
//
//import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;
//import dge.dge_equiv_api.application.pedido.service.EqvTPedidoCrudService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/certificado")
//public class CertificadoController {
//
//    private final EqvTPedidoCrudService crudService;
//
//    public CertificadoController(EqvTPedidoCrudService crudService) {
//        this.crudService = crudService;
//    }
//
//
//    @GetMapping("/portal/{id}")
//
//    public ResponseEntity<List<CertificadoEquivalenciaDTO>> getCertificadosPorRequisicao(@PathVariable Integer id) {
//        List<CertificadoEquivalenciaDTO> certificados = crudService.montarCertificadosPorRequisicao(id);
//        if (certificados == null || certificados.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(certificados);
//    }
//
//}
