package dge.dge_equiv_api.document.service;
import dge.dge_equiv_api.document.dto.DocumentoDTO;
import dge.dge_equiv_api.model.dto.DocRelacaoDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DocumentService {

    public String save(DocRelacaoDTO dto);


    List<DocumentoDTO> getDocumentosPorRelacao(Integer idRelacao, String tipoRelacao, String appCode);
}
