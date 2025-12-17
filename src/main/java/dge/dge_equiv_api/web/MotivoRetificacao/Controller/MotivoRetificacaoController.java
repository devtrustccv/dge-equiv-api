package dge.dge_equiv_api.web.MotivoRetificacao.Controller;

import dge.dge_equiv_api.application.motivo_retidicado.dto.MotivoRetificacaoResponseDTO;
import dge.dge_equiv_api.application.motivo_retidicado.service.MotivoRetificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/motivo-retificacao")
@Tag(name = "Motivo de Retificação", description = "API para consulta de motivos de retificação de pedidos")
public class MotivoRetificacaoController {

    private final MotivoRetificacaoService motivoRetificacaoService;

    @GetMapping("/processo/{numeroProcesso}")
    @Operation(summary = "Buscar motivo de retificação por número do processo",
            description = "Retorna todos os motivos de retificação associados a um processo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum resultado encontrado"),
            @ApiResponse(responseCode = "404", description = "Processo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<MotivoRetificacaoResponseDTO>> getMotivoRetificacaoPorProcesso(
            @Parameter(description = "Número do processo", example = "12345", required = true)
            @PathVariable Integer numeroProcesso) {

        log.info("Requisição recebida - Buscar motivo de retificação: Processo {}", numeroProcesso);

        try {
            List<MotivoRetificacaoResponseDTO> resultados =
                    motivoRetificacaoService.buscarMotivoRetificacaoPorProcesso(numeroProcesso);

            if (resultados.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(resultados);

        } catch (Exception e) {
            log.error("Erro ao processar requisição para processo {}: {}",
                    numeroProcesso, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}