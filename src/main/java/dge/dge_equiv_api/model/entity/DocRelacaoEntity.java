package dge.dge_equiv_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "doc_t_doc_relacao")
@Data
public class DocRelacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "estado")
    private String estado;

    @Column(name = "id_relacao")
    private Long idRelacao;

    @Column(name = "id_tp_doc")
    private Long idTpDoc;

    @Column(name = "mimetype")
    private String mimetype;

    @Column(name = "path")
    private String path;

    @Column(name = "tipo_relacao")
    private String tipoRelacao;

    @Column(name = "user_create")
    private String userCreate;


    @Column(name = "file_name")
    private String fileName;

    @Column(name = "app_code")
    private String appCode;

//    @Column(name = " ")
//    private String name; // Note que tem espaço no nome da coluna original — evite usar esse nome no banco!

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdRelacao() {
        return idRelacao;
    }

    public void setIdRelacao(Long idRelacao) {
        this.idRelacao = idRelacao;
    }



    public Long getIdTpDoc() {
        return idTpDoc;
    }

    public void setIdTpDoc(Long idTpDoc) {
        this.idTpDoc = idTpDoc;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTipoRelacao() {
        return tipoRelacao;
    }

    public void setTipoRelacao(String tipoRelacao) {
        this.tipoRelacao = tipoRelacao;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}

