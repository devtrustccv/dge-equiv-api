package dge.dge_equiv_api.application.pedidov01.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VerificacaoEtapaResponseDTO {
    private boolean temAlteracao;
    private int totalPedidos;
    private int pedidosEmAlteracao;
    private String numeroProcesso;
    private List<PedidoEtapaDTO> pedidosDetalhados;

    @Data
    @Builder
    public static class PedidoEtapaDTO {
        private Integer id;
        private String formacaoProf;
        private String etapa;
        private String instituicao;
        private Integer status;
    }
}