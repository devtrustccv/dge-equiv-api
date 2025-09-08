package dge.dge_equiv_api.application.document.service;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;

import java.util.List;

public interface DocumentService {

    public String save(DocRelacaoDTO dto);


    List<DocumentoDTO> getDocumentosPorRelacao(Integer idRelacao, String tipoRelacao, String appCode);
}
