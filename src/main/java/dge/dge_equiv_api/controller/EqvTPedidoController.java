package dge.dge_equiv_api.controller;

import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.model.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.service.EqvTPedidoService;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/pedidos")

public class EqvTPedidoController {

    private final EqvTPedidoService  pedidoService;
    public EqvTPedidoController(EqvTPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPedido(@PathVariable Integer id) {
        EqvtPedidoDTO dto = pedidoService.getPedidoDTOById(id);
        if (dto == null) return ResponseEntity.notFound().build();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("pedido", dto);

        return ResponseEntity.ok(response);
    }




}
