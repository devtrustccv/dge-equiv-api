package dge.dge_equiv_api.model.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "cnq_t_familia_profissional")
public class FamiliaProfissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String denominacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(String denominacao) {
        this.denominacao = denominacao;
    }
}
