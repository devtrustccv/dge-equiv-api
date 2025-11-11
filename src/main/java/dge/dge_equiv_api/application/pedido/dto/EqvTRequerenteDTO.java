package dge.dge_equiv_api.application.pedido.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EqvTRequerenteDTO {

    private Integer id;

    @NotNull(message = "O NIF é obrigatório")
    @Min(value = 100000000, message = "O NIF deve ter pelo menos 9 dígitos")
    private Integer nif;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O número do documento é obrigatório")
    private String docNumero;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser anterior à data atual")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @NotBlank(message = "A nacionalidade é obrigatória")
    private String nacionalidade;

    @NotBlank(message = "O sexo é obrigatório")
    private String sexo;

    @NotBlank(message = "A habilitação é obrigatória")
    private String habilitacao;

    @NotBlank(message = "O tipo de documento de identificação é obrigatório")
    private String docIdentificacao;

    @NotNull(message = "A data de emissão do documento é obrigatória")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataEmissaoDoc;

    @NotNull(message = "A data de validade do documento é obrigatória")
    @Future(message = "A data de validade deve ser uma data futura")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataValidadeDoc;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email informado não é válido")
    private String email;

    @NotNull(message = "O contato é obrigatório")
    @Digits(integer = 9, fraction = 0, message = "O contato deve ter 9 dígitos")
    private Integer contato;

    private Integer userCreate;
    private Integer userUpdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateCreate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataUpdate;

    private Integer idPessoa;
}
