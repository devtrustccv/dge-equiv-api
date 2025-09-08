package dge.dge_equiv_api.application.pedido.dto;
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

    private EqvTRequisicaoDTO requisicao;
    private EqvTRequerenteDTO requerente;
    private List<EqvtPedidoDTO> pedidos;
    private Integer pessoaId;


    // campos para arquivos
    private MultipartFile file;                     // um arquivo
    private List<MultipartFile> documentosArquivos; // vários arquivos


}
