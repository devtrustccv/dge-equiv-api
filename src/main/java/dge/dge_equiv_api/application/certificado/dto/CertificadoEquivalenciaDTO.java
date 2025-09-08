package dge.dge_equiv_api.application.certificado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CertificadoEquivalenciaDTO {
    private String formacaoOriginal;
    private String entidadeOriginal;
    private String equivalencia;
    private String nivelQualificacao;
    private String url;
    private Integer despacho;

    @JsonFormat(pattern = "dd MMM yyyy")
    private LocalDate dataEmissao;
    private String entidadeEmissora;
    private String numeroProcesso;
    private String paisOrigem;

}
