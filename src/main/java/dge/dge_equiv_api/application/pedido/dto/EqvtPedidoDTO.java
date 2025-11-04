package dge.dge_equiv_api.application.pedido.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
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
public class EqvtPedidoDTO {
    private Integer id;
    @JsonIgnore
    private EqvTRequerenteDTO requerente;

    private EqvTInstEnsinoDTO instEnsino;
    @JsonIgnore
    private EqvTRequisicaoDTO requisicao;
    private String formacaoProf;
    private Integer carga;
    private BigDecimal anoInicio;
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
   private  String entidade;
   private  String referencia;
   private  String verduc;
   private  String ValorDuc;


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
