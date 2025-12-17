package dge.dge_equiv_api.application.pedidov01.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqvtTDecisaoDespachoDTO {

    private Integer id;
    private Integer idPedido;
    private Integer decisao;
    private String obsDespacho;
    private Integer userCreate;
    private Integer userUpdate;
    private LocalDate dataCreate;
    private LocalDate dataUpdate;
}
