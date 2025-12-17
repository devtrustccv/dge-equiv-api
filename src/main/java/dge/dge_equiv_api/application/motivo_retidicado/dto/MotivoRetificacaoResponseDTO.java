package dge.dge_equiv_api.application.motivo_retidicado.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MotivoRetificacaoResponseDTO {

    @JsonProperty("numeroProcesso")
    private Integer numeroProcesso;

    @JsonProperty("motivoRetificacao")
    private String motivoRetificacao;


}