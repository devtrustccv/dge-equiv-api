
package dge.dge_equiv_api.application.pedidov01.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dge.dge_equiv_api.application.motivo_retidicado.dto.MotivoRetificacaoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessoPedidosDocumentosDTO {

    private Integer idRequerente;
    private Integer nif;
    private String nome;
    private String docNumero;
    private LocalDate dataNascimento;
    private String nacionalidade;
    private String sexo;
    private String habilitacao;
    private String docIdentificacao;
    private LocalDate dataEmissaoDoc;
    private LocalDate dataValidadeDoc;
    private String email;
    private Integer contato;
    private Integer userCreate;
    private Integer userUpdate;
    private LocalDate dateCreate;
    private LocalDate dataUpdate;
    private Integer idPessoa;
    private String numeroProcesso;
    private List<MotivoRetificacaoResponseDTO> motivosRetificacao;
    private List<PedidoDocumentosDTO> pedidos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PedidoDocumentosDTO {
        private Integer id;
        private String formacaoProf;
        private Integer instituicaoEnsino;
        private String instituicaoEnsinoNome;
        private Integer carga;
        private BigDecimal anoInicio;
        private BigDecimal anoFim;
        private String paisInstituicao;
        private String paisNome;
        private List<DocumentoInfoDTO> documentos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DocumentoInfoDTO {
        private Long id;
        private String fileName;
        private String path;
        private String tipoRelacao;
        private Integer idRelacao;
        private String idTpDoc;
        private String estado;
        private String appCode;
        private String previewUrl;
        private String dataCriacao;

    }
}