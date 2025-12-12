package dge.dge_equiv_api.application.reclamacao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dge.dge_equiv_api.application.reclamacao.dto.EqvTReclamacaoDTO;
import dge.dge_equiv_api.application.reclamacao.service.EqvTReclamacaoService;
import dge.dge_equiv_api.infrastructure.primary.EqvTReclamacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reclamacao")
@RequiredArgsConstructor
public class EqvTReclamacaoController {

    private final EqvTReclamacaoService reclamacaoService;
    private final ObjectMapper objectMapper; // Para converter JSON
    private static final Logger logger = LoggerFactory.getLogger(EqvTReclamacaoController.class);

    /**
     * Cria reclamação com documentos (multipart/form-data)
     * Frontend envia:
     * - reclamacaoDTO: JSON do DTO
     * - nProcesso: número do processo
     * - file: arquivo (opcional)
     */
    @PostMapping(value = "/portal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReclamacao(
            @RequestParam("reclamacaoDTO") String reclamacaoJson,
            @RequestParam("nProcesso") String nProcesso,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            // Converte JSON para DTO
            EqvTReclamacaoDTO dto = objectMapper.readValue(reclamacaoJson, EqvTReclamacaoDTO.class);


            // Salva a reclamação usando o número do processo
            EqvTReclamacao created = reclamacaoService.savePorNumeroProcesso(nProcesso, dto);

            logger.info("Reclamação criada com sucesso: {}", created.getId());
            return ResponseEntity.ok(created);

        } catch (Exception e) {
            logger.error("Erro ao criar reclamação", e);
            return ResponseEntity.status(500).body("Erro interno ao criar a reclamação: " + e.getMessage());
        }
    }
}


