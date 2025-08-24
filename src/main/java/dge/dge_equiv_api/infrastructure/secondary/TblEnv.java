package dge.dge_equiv_api.infrastructure.secondary;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_env")
public class TblEnv {

    @Id
    private Integer id;

    @Column(name = "dad", nullable = false, unique = true)
    private String dad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDad() {
        return dad;
    }

    public void setDad(String dad) {
        this.dad = dad;
    }
}
