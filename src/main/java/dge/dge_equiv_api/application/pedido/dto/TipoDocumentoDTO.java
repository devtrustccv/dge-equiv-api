package dge.dge_equiv_api.application.pedido.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TipoDocumentoDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private String status;
    private Integer obrigatorio;
    private String processo;
    private String etapa;
}

