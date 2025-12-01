package dge.dge_equiv_api.application.pedidov01.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EqvTInstEnsinoDTO {
    private Integer id;
    private String nome;
    private LocalDate dateCreate;
    private Integer UserCreate;
    private String Status;
    private String pais;

}
