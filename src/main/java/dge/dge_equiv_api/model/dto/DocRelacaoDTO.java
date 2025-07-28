package dge.dge_equiv_api.model.dto;

import org.springframework.web.multipart.MultipartFile;


public class DocRelacaoDTO {
    private Integer idRelacao;
    private String tipoRelacao;
    private String estado;
    private String idTpDoc;
    private String fileName;
    private String path;
    private String appCode;
    private MultipartFile file;
    private String nProcesso; // <-- só para usar no path do arquivo

    public String getNProcesso() {
        return nProcesso;
    }

    public void setNProcesso(String nProcesso) {
        this.nProcesso = nProcesso;
    }

    // Construtor padrão
    public DocRelacaoDTO() {}

    // Construtor com todos campos (opcional)
    public DocRelacaoDTO(Integer idRelacao, String tipoRelacao, String estado, String idTpDoc,
                         String fileName, String path, String appCode, MultipartFile file) {
        this.idRelacao = idRelacao;
        this.tipoRelacao = tipoRelacao;
        this.estado = estado;
        this.idTpDoc = idTpDoc;
        this.fileName = fileName;
        this.path = path;
        this.appCode = appCode;
        this.file = file;
    }

    // Getters e setters
    public Integer getIdRelacao() {
        return idRelacao;
    }

    public void setIdRelacao(Integer idRelacao) {
        this.idRelacao = idRelacao;
    }

    public String getTipoRelacao() {
        return tipoRelacao;
    }

    public void setTipoRelacao(String tipoRelacao) {
        this.tipoRelacao = tipoRelacao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdTpDoc() {
        return idTpDoc;
    }

    public void setIdTpDoc(String idTpDoc) {
        this.idTpDoc = idTpDoc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    // Builder manual
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer idRelacao;
        private String tipoRelacao;
        private String estado;
        private String idTpDoc;
        private String fileName;
        private String path;
        private String appCode;
        private MultipartFile file;
        private String nProcesso;

        public Builder nProcesso(String nProcesso) {
            this.nProcesso = nProcesso;
            return this;
        }

        public Builder idRelacao(Integer idRelacao) {
            this.idRelacao = idRelacao;
            return this;
        }

        public Builder tipoRelacao(String tipoRelacao) {
            this.tipoRelacao = tipoRelacao;
            return this;
        }

        public Builder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public Builder idTpDoc(String idTpDoc) {
            this.idTpDoc = idTpDoc;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder appCode(String appCode) {
            this.appCode = appCode;
            return this;
        }

        public Builder file(MultipartFile file) {
            this.file = file;
            return this;
        }

        public DocRelacaoDTO build() {
            DocRelacaoDTO dto = new DocRelacaoDTO();
            dto.setIdRelacao(this.idRelacao);
            dto.setTipoRelacao(this.tipoRelacao);
            dto.setEstado(this.estado);
            dto.setIdTpDoc(this.idTpDoc);
            dto.setFileName(this.fileName);
            dto.setPath(this.path);
            dto.setAppCode(this.appCode);
            dto.setFile(this.file);
            return dto;
        }
    }
}
