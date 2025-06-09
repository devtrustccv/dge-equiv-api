package dge.dge_equiv_api.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
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

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getDomainType() {
        return domainType;
    }

    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    @Column(name = "valor")
    private String valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @ManyToOne
    @JoinColumn(name = "env_fk")
    private TblEnv env;




}
