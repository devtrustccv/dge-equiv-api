package dge.dge_equiv_api.controller;

import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.document.service.DocumentServiceImpl;
import dge.dge_equiv_api.model.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.model.dto.PortalPedidosDTO;
import dge.dge_equiv_api.model.dto.PortalPedidosRespostaDTO;
import dge.dge_equiv_api.service.EqvTPedidoCrudService;
import dge.dge_equiv_api.service.EqvTPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final DocumentServiceImpl documentServiceImpl;

    public EqvTPedidoController(EqvTPedidoService pedidoService, EqvTPedidoCrudService crudService, DocumentServiceImpl documentServiceImpl) {
        this.pedidoService = pedidoService;
        this.crudService = crudService;
        this.documentServiceImpl = documentServiceImpl;
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
    public ResponseEntity<?> createLote(@ModelAttribute PortalPedidosDTO lotePedidosDTO) {
        try {
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
        } catch (Exception e) {
            logger.error("Erro ao criar pedidos: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao criar pedidos: " + e.getMessage());
        }
    }

    // ========================
    // PUT - Atualizar pedidos
    // ========================
    @Operation(
            summary = "Atualiza pedidos por ID da requisição",
            description = "Atualiza todos os pedidos associados à requisição especificada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedidos atualizados com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao atualizar os pedidos")
            }
    )
    @PutMapping(value = "/portal/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLote(@PathVariable String id, @ModelAttribute PortalPedidosDTO lotePedidosDTO) {
        try {
            Integer requisicaoId = Integer.valueOf(id);
            List<EqvtPedidoDTO> updated = crudService.updatePedidosByRequisicaoId(requisicaoId, lotePedidosDTO);

            if (updated.isEmpty()) {
                logger.warn("Nenhum pedido foi atualizado.");
                return ResponseEntity.badRequest().body("Não foi possível atualizar os pedidos.");
            }

            PortalPedidosRespostaDTO resposta = new PortalPedidosRespostaDTO();
            resposta.setRequisicao(updated.getFirst().getRequisicao());
            resposta.setRequerente(updated.getFirst().getRequerente());
            resposta.setPedidos(updated);

            logger.info("Pedidos atualizados com sucesso: {}", updated.size());
            return ResponseEntity.ok().body(resposta);
        } catch (Exception e) {
            logger.error("Erro ao atualizar pedidos: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao atualizar pedidos: " + e.getMessage());
        }
    }

    // ========================
    // GET - Buscar todos os pedidos
    // ========================
    @GetMapping
    public ResponseEntity<List<EqvtPedidoDTO>> getAll() {
        List<EqvtPedidoDTO> pedidos = crudService.findAll();
       // byte[] arquivo = documentServiceImpl.previewDocumento(496, "SOLICITACAO", "equiv", false);
        //logger.info("saida .......  "+arquivo.length);
        logger.info("Total de pedidos encontrados: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/portal/{id}")
    public ResponseEntity<List<EqvtPedidoDTO>> getPedidosComDocumentosPorRequisicao(@PathVariable Integer id) {
        List<EqvtPedidoDTO> pedidos = crudService.findPedidosComDocumentosByRequisicao(id);
        return ResponseEntity.ok(pedidos);
    }


    // ========================
    // GET - Buscar pedido por ID criptografado
    // ========================
    @GetMapping("/{encryptedId}")
    public ResponseEntity<?> getPedido(@PathVariable String encryptedId) {
        try {
            String decryptedId = AESUtil.decrypt(encryptedId);
            Integer id = Integer.valueOf(decryptedId);

            EqvtPedidoDTO dto = pedidoService.getPedidoDTOById(id);
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

    @GetMapping("/certificado/{id}")
    public ResponseEntity<CertificadoEquivalenciaDTO> getCertificado(@PathVariable Integer id) {
        EqvtPedidoDTO pedido = crudService.findById(id); // seu método real aqui
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }

        CertificadoEquivalenciaDTO dto = crudService.montarCertificado(pedido);
        return ResponseEntity.ok(dto);
    }


//    @Autowired
//    private RuntimeService runtimeService;
//
//    @PostMapping("/iniciar-processo")
//    public ResponseEntity<?> iniciarProcesso() {
//        String numero = "EQV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//
//        Map<String, Object> vars = new HashMap<>();
//        vars.put("numero", numero);
//
//        ProcessInstance instance = runtimeService.startProcessInstanceByKey("processo_equivalencia", vars);
//
//        return ResponseEntity.ok(Map.of(
//                "processInstanceId", instance.getId(),
//                "numero", numero
//        ));
//    }


    // ========================
    // DELETE - (Comentado por segurança, ativar se necessário)
    // ========================
    /*
    @DeleteMapping("/portal/{encryptedId}")
    public ResponseEntity<?> delete(@PathVariable String encryptedId) {
        try {
            String decryptedId = AESUtil.decrypt(encryptedId);
            Integer id = Integer.valueOf(decryptedId);
            crudService.deletePedido(id);

            logger.info("Pedido deletado com sucesso, ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar pedido: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    */
}
