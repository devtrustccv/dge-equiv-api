package dge.dge_equiv_api.application.reporter.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for EqvTParamReport
 * Author: Nositeste
 * Date: 2025-05-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvTParamReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String caixaPostal;
    private String email;
    private String logotipoUrl;
    private String republica;
    private String rua;
    private String telefone;

}
