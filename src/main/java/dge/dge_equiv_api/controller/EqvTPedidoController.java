package dge.dge_equiv_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.document.dto.DocumentoDTO;
import dge.dge_equiv_api.model.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.model.dto.PortalPedidosDTO;
import dge.dge_equiv_api.model.dto.PortalPedidosRespostaDTO;
import dge.dge_equiv_api.service.EqvTPedidoCrudService;
import dge.dge_equiv_api.service.EqvTPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/pedidos")
public class EqvTPedidoController {

    private static final Logger logger = LoggerFactory.getLogger(EqvTPedidoController.class);

    private final EqvTPedidoService pedidoService;
    private final EqvTPedidoCrudService crudService;

    public EqvTPedidoController(EqvTPedidoService pedidoService, EqvTPedidoCrudService crudService) {
        this.pedidoService = pedidoService;
        this.crudService = crudService;
    }

    // ----- CRUD via CrudService -----

//    @PostMapping
//    public ResponseEntity<EqvtPedidoDTO> create(@Valid @RequestBody EqvtPedidoDTO pedidoDTO) {
//        EqvtPedidoDTO created = crudService.createPedido(pedidoDTO);
//        return ResponseEntity.ok(created);
//    }

    // NOVO ENDPOINT para criar múltiplos pedidos de uma só vez
    @Operation(
            summary = "Cria um novo pedido",
            description = "Cria um pedido de equivalência profissional com dados completos do requerente, requisição e documentos." +
                    "obs podes criar varios pedidos de uma vez se quiseres  para o mesmo  requerente e requesicao",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação ou dados inválidos")
            }
    )

    @PostMapping(value = "/portal" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PortalPedidosRespostaDTO> createLote(@ModelAttribute PortalPedidosDTO lotePedidosDTO) {
        List<EqvtPedidoDTO> created = crudService.createLotePedidosComRequisicaoERequerenteUnicos(
                lotePedidosDTO.getPedidos(),
                lotePedidosDTO.getRequisicao(),
                lotePedidosDTO.getRequerente()
        );

        var resposta = new PortalPedidosRespostaDTO();

        if (!created.isEmpty()) {
            resposta.setRequisicao(created.getFirst().getRequisicao());
            resposta.setRequerente(created.getFirst().getRequerente());
            resposta.setPedidos(created);
        }

        return ResponseEntity.ok(resposta);
    }

    @Operation(
            summary = "Editar um novo pedido",
            description = "Permite editar um pedido de equivalência profissional com dados completos do requerente, requisição e documentos." +
                    "obs a ediacao e atraves do id do da requesicao",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido updated com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação ou dados inválidos")
            }
    )

    @GetMapping("/portal/{id}")
    public ResponseEntity<?> getById(@PathVariable String encryptedId) {
        try {
            String decryptedId = AESUtil.decrypt(encryptedId);
            Integer id = Integer.valueOf(decryptedId);
            EqvtPedidoDTO dto = crudService.findById(id);
            if (dto == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<EqvtPedidoDTO>> getAll() {
        List<EqvtPedidoDTO> pedidos = crudService.findAll();
        return ResponseEntity.ok(pedidos);
    }




    @PutMapping(value = "/portal/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLote(@PathVariable String id, @ModelAttribute PortalPedidosDTO lotePedidosDTO) {
        try {

            Integer requisicaoId = Integer.valueOf(id);

            List<EqvtPedidoDTO> updated = crudService.updatePedidosByRequisicaoId(requisicaoId, lotePedidosDTO);

            PortalPedidosRespostaDTO resposta = new PortalPedidosRespostaDTO();

            if (!updated.isEmpty()) {
                resposta.setRequisicao(updated.getFirst().getRequisicao());
                resposta.setRequerente(updated.getFirst().getRequerente());
                resposta.setPedidos(updated);
            }

            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar lote: " + e.getMessage());
        }
    }


//    @DeleteMapping("/portal/{encryptedId}")
//    public ResponseEntity<?> delete(@PathVariable String encryptedId) {
//        try {
//            String decryptedId = AESUtil.decrypt(encryptedId);
//            Integer id = Integer.valueOf(decryptedId);
//            crudService.deletePedido(id);
//            return ResponseEntity.noContent().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
//        }
//    }

    @GetMapping("/{encryptedId}")
    public ResponseEntity<?> getPedido(@PathVariable String encryptedId) {
        try {
            // Descriptografa o ID criptografado (ex: "NcO49BG4qV_f5V01IYrIbQ==")
            String decryptedId = AESUtil.decrypt(encryptedId);
            Integer id = Integer.valueOf(decryptedId);

            EqvtPedidoDTO dto = pedidoService.getPedidoDTOById(id);
            if (dto == null) return ResponseEntity.notFound().build();

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
