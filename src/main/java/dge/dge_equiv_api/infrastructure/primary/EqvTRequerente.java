package dge.dge_equiv_api.infrastructure.primary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eqv_t_requerente")
public class EqvTRequerente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String nome;


    private Integer nif;

    @Column(name = "doc_numero")
    private String docNumero;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "data_emissao_doc")
    private LocalDate dataEmissaoDoc;

    @Column(name = "data_validade_doc")
    private LocalDate dataValidadeDoc;

    private String nacionalidade;

    private String sexo;

    private String email;

    private Integer contato;

    private Integer habilitacao;

    @Column(name = "doc_identificacao")
    private String docIdentificacao;

    @Column(name = "user_create")
    private Integer userCreate;

    @Column(name = "user_update")
    private Integer userUpdate;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "data_update")
    private LocalDate dataUpdate;

}
