package dge.dge_equiv_api.application.pedido.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PortalPedidosDTO {
    @NotNull(message = "A requisição é obrigatória")
    @Valid
    private EqvTRequisicaoDTO requisicao;
    @NotNull(message = "O requerente é obrigatório")
    @Valid
    private EqvTRequerenteDTO requerente;
    @NotEmpty(message = "Deve existir pelo menos um pedido")
    @Valid
    private List<EqvtPedidoDTO> pedidos;
    @NotNull(message = "O campo pessoaId é obrigatório")
    private Integer pessoaId;


    // campos para arquivos
    private MultipartFile file;                     // um arquivo
    private List<MultipartFile> documentosArquivos; // vários arquivos


}
