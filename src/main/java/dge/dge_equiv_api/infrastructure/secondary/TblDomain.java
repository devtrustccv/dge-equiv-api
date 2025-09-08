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
@Table(name = "tbl_domain")
public class TblDomain {

    @Id
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "domain_type")
    private String domainType;

    @Column(name = "dominio")
    private String dominio;


    @Column(name = "valor")
    private String valor;

    @ManyToOne
    @JoinColumn(name = "env_fk")
    private TblEnv env;




}
