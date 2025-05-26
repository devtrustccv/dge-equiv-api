package dge.dge_equiv_api.controller;

import dge.dge_equiv_api.model.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.service.EqvTPedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pedidos")

public class EqvTPedidoController {

    private final EqvTPedidoService  pedidoService;
    public EqvTPedidoController(EqvTPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EqvtPedidoDTO> getPedido(@PathVariable Integer id) {
        EqvtPedidoDTO dto = pedidoService.getPedidoDTOById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
