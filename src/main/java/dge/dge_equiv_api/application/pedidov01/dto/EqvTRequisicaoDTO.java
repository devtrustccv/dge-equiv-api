package dge.dge_equiv_api.application.pedidov01.dto;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EqvTRequisicaoDTO {

    private Integer id;

    @JsonProperty("nProcesso")
    private Integer nProcesso;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataCreate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate   dataUpdate;
    private Integer status;
    private Integer etapa;

    private Integer userCreate;
    private Integer userUpdate;
    private  Integer pessoaId;
    private Integer idPessoa;
    List<EqvTPedido> Pedidos;





}
