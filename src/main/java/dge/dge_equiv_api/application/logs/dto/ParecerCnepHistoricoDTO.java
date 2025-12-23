package dge.dge_equiv_api.application.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParecerCnepHistoricoDTO {
    private String dataAlteracao;
    private String etapa;
    private String valorAnterior;
    private String valorNovo;
    private String campo;
}