package dge.dge_equiv_api.certificado;


import dge.dge_equiv_api.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.model.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.service.EqvTPedidoCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificado")
public class CertificadoController {

    private final EqvTPedidoCrudService crudService;

    public CertificadoController(EqvTPedidoCrudService crudService) {
        this.crudService = crudService;
    }


    @GetMapping("/certificado/{id}")

    public ResponseEntity<CertificadoEquivalenciaDTO> getCertificado(@PathVariable Integer id) {
        EqvtPedidoDTO pedido = crudService.findById(id); // seu método real aqui
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }

        CertificadoEquivalenciaDTO dto = crudService.montarCertificado(pedido);
        return ResponseEntity.ok(dto);
    }

}
