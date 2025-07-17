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

    @Column(name = "idtpdoc")
    private Long idtpdoc;  // cuidado: esse campo pode ser redundante com id_tp_doc

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

    @Column(name = "ano_escolar")
    private String anoEscolar;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "app_code")
    private String appCode;

    @Column(name = "name ")
    private String name; // Note que tem espaço no nome da coluna original — evite usar esse nome no banco!

}

