package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "eqv_t_tipo_documento")
public class TipoDocumentoEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "status")
    private String status;
    @Column(name = "obrigatorio")
    private Integer obrigatorio;
    @Column(name = "processo")
    private String processo;
    @Column(name = "etapa")
    private String etapa;
    @Column(name = "user_create")
    private Integer userCreate;
    @Column(name = "user_update")
    private Integer userUpdate;
    @Column(name = "date_create")
    private LocalDate dateCreate;
    @Column(name = "date_update")
    private LocalDate dateUpdate;

}
