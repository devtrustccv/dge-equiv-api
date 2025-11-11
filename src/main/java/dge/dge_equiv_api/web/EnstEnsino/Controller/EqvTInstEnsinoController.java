package dge.dge_equiv_api.web.EnstEnsino.Controller;

import dge.dge_equiv_api.application.InstEnsino.dto.InstituicaoPaisDTO;
import dge.dge_equiv_api.application.InstEnsino.service.EqvTInstEnsinoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instituicoes")
public class EqvTInstEnsinoController {

    private final EqvTInstEnsinoService service;

    public EqvTInstEnsinoController(EqvTInstEnsinoService service) {
        this.service = service;
    }

    // GET /instituicoes/{id}/pais
    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoPaisDTO> getPaisById(@PathVariable Integer id) {
        try {
            InstituicaoPaisDTO dto = service.getPaisById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
