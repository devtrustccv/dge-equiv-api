package dge.dge_equiv_api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.document.dto.DocumentoDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
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


    private String Etapa;

    private List<DocumentoDTO> documentos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEtapa() {
        return Etapa;
    }

    public void setEtapa(String etapa) {
        Etapa = etapa;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
    public Integer getStatus() {
        return Status;
    }

    public EqvTRequerenteDTO getRequerente() {
        return requerente;
    }

    public void setRequerente(EqvTRequerenteDTO requerente) {
        this.requerente = requerente;
    }

    public EqvTInstEnsinoDTO getInstEnsino() {
        return instEnsino;
    }

    public void setInstEnsino(EqvTInstEnsinoDTO instEnsino) {
        this.instEnsino = instEnsino;
    }

    public EqvTRequisicaoDTO getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(EqvTRequisicaoDTO requisicao) {
        this.requisicao = requisicao;
    }

    public String getFormacaoProf() {
        return formacaoProf;
    }

    public void setFormacaoProf(String formacaoProf) {
        this.formacaoProf = formacaoProf;
    }

    public Integer getCarga() {
        return carga;
    }

    public void setCarga(Integer carga) {
        this.carga = carga;
    }

    public BigDecimal getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(BigDecimal anoInicio) {
        this.anoInicio = anoInicio;
    }

    public BigDecimal getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(BigDecimal anoFim) {
        this.anoFim = anoFim;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    public String getNumDeclaracao() {
        return numDeclaracao;
    }

    public void setNumDeclaracao(String numDeclaracao) {
        this.numDeclaracao = numDeclaracao;
    }

    public LocalDate getDataDespacho() {
        return dataDespacho;
    }

    public void setDataDespacho(LocalDate dataDespacho) {
        this.dataDespacho = dataDespacho;
    }

    public List<DocumentoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoDTO> documentos) {
        this.documentos = documentos;
    }

    // Novo getter que será serializado no JSON como "id"




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
