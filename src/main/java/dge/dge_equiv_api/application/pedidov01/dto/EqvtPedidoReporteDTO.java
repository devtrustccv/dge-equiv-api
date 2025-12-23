package dge.dge_equiv_api.application.pedidov01.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.logs.dto.ParecerCnepHistoricoDTO;
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
public class EqvtPedidoReporteDTO {
    private Integer id;

    private EqvTRequerenteDTO requerente;

    private EqvTInstEnsinoDTO instEnsino;

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

    private String Etapa;

    private List<DocumentoDTO> documentos1;
    private List<EqvtTDecisaoVpDTO> decisoesVp;
    private List<EqvtTDecisaoApDTO> decisoesAp;
    private List<DocRelacaoDTO> documentos;
    private List<ParecerCnepHistoricoDTO> historicoDT;





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
