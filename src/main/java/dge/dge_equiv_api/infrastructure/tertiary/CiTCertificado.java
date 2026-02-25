package dge.dge_equiv_api.infrastructure.tertiary;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ci_t_certificado", schema = "public")
public class CiTCertificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pessoa_id")
    private Long pessoaId;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Column(name = "app")
    private String app;

    @Column(name = "nome_formacao")
    private String nomeFormacao;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // getters/setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getPessoaId() { return pessoaId; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }

    public String getNomeFormacao() { return nomeFormacao; }
    public void setNomeFormacao(String nomeFormacao) { this.nomeFormacao = nomeFormacao; }

    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}