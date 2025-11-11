package dge.dge_equiv_api.application.InstEnsino.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstituicaoPaisDTO {
    private Integer id;
    private String pais;
}

