package dge.dge_equiv_api.application.pedido.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor

public class EqvTTaxaDTO {

    private Integer id;
    private String estado;
    private String etapa;
    private BigDecimal valor;
    private Integer userCreate;
    private Integer userUpdate;
    private LocalDate dataCreate;
    private LocalDate dataUpdate;

}

