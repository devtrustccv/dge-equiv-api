package dge.dge_equiv_api.document.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoRespostaDTO {
    private String nome;
    private String mimetype;
    private String url;

    private String idTpDoc;

    public String getIdTpDoc() {
        return idTpDoc;
    }

    public void setIdTpDoc(String idTpDoc) {
        this.idTpDoc = idTpDoc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
