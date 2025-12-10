package dge.dge_equiv_api.web.equiv.controller;

import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.application.document.service.DocumentServiceImpl;
import dge.dge_equiv_api.application.pedidov01.dto.*;
import dge.dge_equiv_api.application.pedidov01.service.EqvTPedidoService;
import dge.dge_equiv_api.application.pedidov01.service.EqvTPedidoServiceReporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/pedidos")
public class EqvTPedidoController {

    private static final Logger logger = LoggerFactory.getLogger(EqvTPedidoController.class);

    private final EqvTPedidoServiceReporter pedidoServiceReporter;
    private final EqvTPedidoService crudService;

    public EqvTPedidoController(EqvTPedidoServiceReporter pedidoServiceReporter, EqvTPedidoService crudService, DocumentServiceImpl documentServiceImpl) {
        this.pedidoServiceReporter = pedidoServiceReporter;
        this.crudService = crudService;
    }

    // ========================
    // POST - Criar pedidos
    // =======================
    @Operation(
            summary = "Cria novos pedidos",
            description = "Cria um ou mais pedidos de equivalência associados a um requerente e uma requisição. " +
                    "⚠️ É obrigatório informar o identificador da pessoa (`pessoaId`) no envio da requisição.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedidos criados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao processar os dados")
            }
    )

    @PostMapping(value = "/portal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createLote(@Valid @ModelAttribute PortalPedidosDTO lotePedidosDTO) {

            List<EqvtPedidoDTO> created = crudService.createLotePedidosComRequisicaoERequerenteUnicos(
                    lotePedidosDTO.getPedidos(),
                    lotePedidosDTO.getRequisicao(),
                    lotePedidosDTO.getRequerente(),
                    lotePedidosDTO.getPessoaId()
            );

            if (created.isEmpty()) {
                logger.warn("Nenhum pedido foi criado.");
                return ResponseEntity.badRequest().body("Não foi possível criar os pedidos.");
            }

            PortalPedidosRespostaDTO resposta = new PortalPedidosRespostaDTO();
            resposta.setRequisicao(created.getFirst().getRequisicao());
            resposta.setRequerente(created.getFirst().getRequerente());
            resposta.setPedidos(created);

            logger.info("Pedidos criados com sucesso: {}", created.size());
            return ResponseEntity.ok().body(resposta);

    }
    @GetMapping("/portal/{id}")
    public ResponseEntity<List<EqvtPedidoDTO>> getPedidosComDocumentosPorRequisicao(@PathVariable Integer id) {
        List<EqvtPedidoDTO> pedidos = crudService.findPedidosComDocumentosByRequisicao(id);
        return ResponseEntity.ok(pedidos);
    }



    @PutMapping(value = "/portal/requisicoes/{requisicaoId}/pedidos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<EqvtPedidoDTO>> updatePedidosByRequisicao(
            @PathVariable Integer requisicaoId,
            @ModelAttribute PortalPedidosDTO portalPedidosDTO,String numProcesso ) {

        List<EqvtPedidoDTO> pedidosAtualizados = crudService
                .updateLotePedidosComRequisicao(requisicaoId, portalPedidosDTO,numProcesso);

        return ResponseEntity.ok(pedidosAtualizados);
    }




    // ========================
    // GET - Buscar pedido por ID criptografado
    // ========================
    @GetMapping("/{encryptedId}")
    public ResponseEntity<?> getPedido(@PathVariable String encryptedId) {
        try {
            String decryptedId = AESUtil.decrypt(encryptedId);
            Integer id = Integer.valueOf(decryptedId);

            EqvtPedidoReporteDTO dto = pedidoServiceReporter.getPedidoDTOById(id);
            if (dto == null) {
                logger.warn("Pedido não encontrado para o ID descriptografado: {}", id);
                return ResponseEntity.status(404).body("Pedido não encontrado.");
            }

            logger.info("Pedido encontrado para ID {}: {}", id, dto);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Erro ao buscar pedido por ID criptografado: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("ID inválido ou erro ao descriptografar.");
        }
    }

    @GetMapping("/processo/{numeroProcesso}")
    public ResponseEntity<ProcessoPedidosDocumentosDTO> getPedidosComDocumentosPorProcesso(
            @PathVariable String numeroProcesso) {
        ProcessoPedidosDocumentosDTO resultado = crudService.getPedidosComDocumentosPorProcesso(numeroProcesso);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Verifica se algum pedido de um processo está em 'alter_solic'")
    @GetMapping("/processo/{numeroProcesso}/verificar-etapa-solic")
    public ResponseEntity<Map<String, Object>> verificarAlterSolic(
            @PathVariable String numeroProcesso) {

        boolean resultado = crudService.verificarPedidosEmAlterSolic(numeroProcesso);

        Map<String, Object> response = new HashMap<>();
        response.put("numeroProcesso", numeroProcesso);
        response.put("saida", resultado);
        response.put("mensagem", resultado ?
                "Este processo pode ser alterado" :
                "Este processo não pode ser alterado");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }



}
