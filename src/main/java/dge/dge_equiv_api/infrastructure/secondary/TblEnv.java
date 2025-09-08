package dge.dge_equiv_api.infrastructure.secondary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tbl_env")
public class TblEnv {

    @Id
    private Integer id;

    @Column(name = "dad", nullable = false, unique = true)
    private String dad;


}
