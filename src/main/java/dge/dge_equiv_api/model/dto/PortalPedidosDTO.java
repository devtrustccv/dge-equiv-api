package dge.dge_equiv_api.model.dto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class PortalPedidosDTO {

    private EqvTRequisicaoDTO requisicao;
    private EqvTRequerenteDTO requerente;
    private List<EqvtPedidoDTO> pedidos;

    // campos para arquivos
    private MultipartFile file;                     // um arquivo
    private List<MultipartFile> documentosArquivos; // vários arquivos

    // getters e setters

    public EqvTRequisicaoDTO getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(EqvTRequisicaoDTO requisicao) {
        this.requisicao = requisicao;
    }

    public EqvTRequerenteDTO getRequerente() {
        return requerente;
    }

    public void setRequerente(EqvTRequerenteDTO requerente) {
        this.requerente = requerente;
    }

    public List<EqvtPedidoDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<EqvtPedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<MultipartFile> getDocumentosArquivos() {
        return documentosArquivos;
    }

    public void setDocumentosArquivos(List<MultipartFile> documentosArquivos) {
        this.documentosArquivos = documentosArquivos;
    }
}
