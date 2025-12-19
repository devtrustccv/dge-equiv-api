package dge.dge_equiv_api.application.pedidov01.dto;

import dge.dge_equiv_api.application.logs.dto.LogDTO;
import dge.dge_equiv_api.application.logs.dto.ParecerCnepHistoricoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqvtTDecisaoApDTO {

    private Integer id;
    private EqvtPedidoDTO idPedido;         // id_pedido
    private Integer decisao;
    private String obs;
    private String familia;
    private String parecerCnep;
    private Integer nivel;
    private List<LogDTO> logs;
    private List<ParecerCnepHistoricoDTO> historicoParecer; // Histórico específico
    private LocalDate dataDecisao;


}
