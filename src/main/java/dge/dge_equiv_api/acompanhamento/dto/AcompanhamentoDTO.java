package dge.dge_equiv_api.acompanhamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AcompanhamentoDTO {

    @JsonProperty("numero")
    private String numero;

    @JsonProperty("app_dad")
    private String appDad;

    @JsonProperty("pessoa_id")
    private Integer pessoaId;

    @JsonProperty("entidade_nif")
    private String entidadeNif;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("percentagem")
    private Integer percentagem;

    @JsonProperty("entidade")
    private String entidade;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("data_fim")
    private LocalDateTime dataFim;

    @JsonProperty("data_inicio")
    private LocalDateTime dataInicio;

    @JsonProperty("data_fim_previsto")
    private LocalDate dataFimPrevisto;

    @JsonProperty("etapa_atual")
    private String etapaAtual;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("estado_desc")
    private String estadoDesc;

    @JsonProperty("detalhes")
    private Map<String, String> detalhes;

    @JsonProperty("comunicacoes")
    private List<Comunicacao> comunicacoes;

    @JsonProperty("eventos")
    private List<Evento> eventos;

    @JsonProperty("outputs")
    private List<Output> outputs;

    // Getters e Setters

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAppDad() {
        return appDad;
    }

    public void setAppDad(String appDad) {
        this.appDad = appDad;
    }

    public Integer getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Integer pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getEntidadeNif() {
        return entidadeNif;
    }

    public void setEntidadeNif(String entidadeNif) {
        this.entidadeNif = entidadeNif;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getPercentagem() {
        return percentagem;
    }

    public void setPercentagem(Integer percentagem) {
        this.percentagem = percentagem;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFimPrevisto() {
        return dataFimPrevisto;
    }

    public void setDataFimPrevisto(LocalDate dataFimPrevisto) {
        this.dataFimPrevisto = dataFimPrevisto;
    }

    public String getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(String etapaAtual) {
        this.etapaAtual = etapaAtual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoDesc() {
        return estadoDesc;
    }

    public void setEstadoDesc(String estadoDesc) {
        this.estadoDesc = estadoDesc;
    }

    public Map<String, String> getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(Map<String, String> detalhes) {
        this.detalhes = detalhes;
    }

    public List<Comunicacao> getComunicacoes() {
        return comunicacoes;
    }

    public void setComunicacoes(List<Comunicacao> comunicacoes) {
        this.comunicacoes = comunicacoes;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    // Subclasse Comunicacao
    public static class Comunicacao {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("descricao")  // JSON usa "descricao" para essa propriedade
        private String descricao;

        @JsonProperty("datetime")
        private LocalDateTime datetime;

        @JsonProperty("items")
        private Map<String, String> items;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public LocalDateTime getDatetime() {
            return datetime;
        }

        public void setDatetime(LocalDateTime datetime) {
            this.datetime = datetime;
        }

        public Map<String, String> getItems() {
            return items;
        }

        public void setItems(Map<String, String> items) {
            this.items = items;
        }
    }

    // Subclasse Evento
    public static class Evento {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("descricao")
        private String descricao;

        @JsonProperty("data")
        private LocalDateTime data;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public LocalDateTime getData() {
            return data;
        }

        public void setData(LocalDateTime data) {
            this.data = data;
        }
    }

    // Subclasse Output
    public static class Output {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("url")
        private String url;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
