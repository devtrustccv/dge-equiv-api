package dge.dge_equiv_api.web.reclamacao.controller;

import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.reclamacao.dto.EqvTReclamacaoDTO;
import dge.dge_equiv_api.application.reclamacao.service.EqvTReclamacaoService;
import dge.dge_equiv_api.infrastructure.primary.EqvTReclamacao;
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

    private final EqvTReclamacaoService service;
    private static final Logger logger = LoggerFactory.getLogger(EqvTReclamacaoController.class);

    @PostMapping(value = "/{nProcesso}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarReclamacao(
            @PathVariable String nProcesso,
            @RequestParam("observacao") String observacao,
            @RequestParam("decisao") Integer decisao,
            @RequestParam(value = "documento", required = false) MultipartFile documento) {

        EqvTReclamacaoDTO dto = new EqvTReclamacaoDTO();
        dto.setObservacao(observacao);
        dto.setDecisao(decisao);

        // Se houver arquivo, adiciona no DTO
        if (documento != null && !documento.isEmpty()) {
            DocumentoDTO docs = new DocumentoDTO();
            docs.setNome("Ata");
            docs.setFile(documento); // aqui você passa o MultipartFile diretamente
            dto.setDocumentos(docs);
        }

        EqvTReclamacao salvo = service.savePorNumeroProcesso(nProcesso, dto);
        return ResponseEntity.ok(salvo);
    }

}
