package dge.dge_equiv_api.application.pedido.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EqvtPedidoDTO {
    private Integer id;
    @JsonIgnore

    private EqvTRequerenteDTO requerente;
    @NotNull(message = "Instituição é obrigatório")
    private EqvTInstEnsinoDTO instEnsino;
    @JsonIgnore
    private EqvTRequisicaoDTO requisicao;
    @NotNull(message = "Formação Profissional é obrigatório")
    private String formacaoProf;
    @NotNull(message = "Carga horária é obrigatório")
    private Integer carga;
    @NotNull(message = "Ano de início é obrigatório")
    private BigDecimal anoInicio;
    @NotNull(message = "Ano de fim é obrigatório")
    private BigDecimal anoFim;
    private Integer nivel;
    private String familia;
    private String despacho;
    private String numDeclaracao;
    private Integer Status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataDespacho;
    private String urlDucPagamento;
    private String nuDuc; // getter e setter
    private String entidade;
    private String referencia;
    private String verduc;
    private String ValorDuc;

    private String Etapa;

    private List<DocumentoDTO> documentos;
    private List<DocumentoResponseDTO> documentosresp;

    @JsonProperty("id")
    public String getEncryptedId() {
        if (this.id == null) return null;
        try {
            return AESUtil.encrypt(String.valueOf(this.id));
        } catch (Exception e) {
            return null;
        }
    }


}
