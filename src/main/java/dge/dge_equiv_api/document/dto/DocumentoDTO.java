package dge.dge_equiv_api.document.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class DocumentoDTO {
        private Integer id;
        private String nome;
        private Integer idTpDoc;
    private String urlArquivo;
    private MultipartFile file;

    public String getUrlArquivo() {
        return urlArquivo;
    }

    public void setUrlArquivo(String urlArquivo) {
        this.urlArquivo = urlArquivo;
    }

    public Integer getIdTpDoc() {
        return idTpDoc;
    }

    public void setIdTpDoc(Integer idTpDoc) {
        this.idTpDoc = idTpDoc;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }




    }


