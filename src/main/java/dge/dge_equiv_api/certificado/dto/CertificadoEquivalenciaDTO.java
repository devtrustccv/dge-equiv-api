package dge.dge_equiv_api.certificado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;


public class CertificadoEquivalenciaDTO {
    private String formacaoOriginal;
    private String entidadeOriginal;
    private String equivalencia;
    private String nivelQualificacao;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonFormat(pattern = "dd MMM yyyy")
    private LocalDate dataEmissao;

    private String entidadeEmissora;
    private String numeroProcesso;
    private String paisOrigem;

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getFormacaoOriginal() {
        return formacaoOriginal;
    }

    public void setFormacaoOriginal(String formacaoOriginal) {
        this.formacaoOriginal = formacaoOriginal;
    }

    public String getEntidadeOriginal() {
        return entidadeOriginal;
    }

    public void setEntidadeOriginal(String entidadeOriginal) {
        this.entidadeOriginal = entidadeOriginal;
    }




//    public String getNumeroSerie() {
//        return numeroSerie;
//    }
//
//    public void setNumeroSerie(String numeroSerie) {
//        this.numeroSerie = numeroSerie;
//    }

    public String getNivelQualificacao() {
        return nivelQualificacao;
    }

    public void setNivelQualificacao(String nivelQualificacao) {
        this.nivelQualificacao = nivelQualificacao;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getEntidadeEmissora() {
        return entidadeEmissora;
    }

    public void setEntidadeEmissora(String entidadeEmissora) {
        this.entidadeEmissora = entidadeEmissora;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }
}
