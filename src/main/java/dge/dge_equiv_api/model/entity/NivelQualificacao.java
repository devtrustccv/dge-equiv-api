package dge.dge_equiv_api.model.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "cnq_t_nivel_qualificacao")
public class NivelQualificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number")
    private Integer number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}