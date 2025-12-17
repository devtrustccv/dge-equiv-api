package dge.dge_equiv_api.application.document.service;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;

import java.util.List;

public interface DocumentService {

    public String save(DocRelacaoDTO dto);
    public String saveReclamcao(DocRelacaoDTO dto);


    List<DocumentoResponseDTO> getDocumentosPorRelacao(Integer idRelacao, String tipoRelacao, String appCode);

    String gerarLinkPublico(String path);


}
