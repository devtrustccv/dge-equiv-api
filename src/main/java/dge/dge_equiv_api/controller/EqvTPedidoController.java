package dge.dge_equiv_api.controller;

import dge.dge_equiv_api.Utils.AESUtil;
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


    @GetMapping("/{encryptedId}")
    public ResponseEntity<?> getPedido(@PathVariable String encryptedId) {
        try {
            // Descriptografa o ID criptografado (ex: "NcO49BG4qV_f5V01IYrIbQ==")
            String decryptedId = AESUtil.decrypt(encryptedId);
            Integer id = Integer.valueOf(decryptedId);

            System.out.println(id);

            EqvtPedidoDTO dto = pedidoService.getPedidoDTOById(id);
            if (dto == null) return ResponseEntity.notFound().build();

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}


