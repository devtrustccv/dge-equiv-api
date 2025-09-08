package dge.dge_equiv_api.application.document.service;



import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.pedido.service.EqvTTipoDocumentoService;
import dge.dge_equiv_api.infrastructure.tertiary.DocRelacaoEntity;
import dge.dge_equiv_api.infrastructure.tertiary.repository.DocRelacaoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTTipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocRelacaoService {

    private final DocRelacaoRepository docRelacaoRepository;
    private final EqvTTipoDocumentoRepository tipoDocumentoRepository;
    private final EqvTTipoDocumentoService eqvTTipoDocumentoService;

    public DocRelacaoService(DocRelacaoRepository docRelacaoRepository, EqvTTipoDocumentoRepository tipoDocumentoRepository, EqvTTipoDocumentoService eqvTTipoDocumentoService) {
        this.docRelacaoRepository = docRelacaoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.eqvTTipoDocumentoService = eqvTTipoDocumentoService;
    }

    public List<DocRelacaoDTO> buscarDocsComNomeTipoPorIdRelacao(Integer idRelacao) {
        List<DocRelacaoEntity> docs = docRelacaoRepository.findByIdRelacao(idRelacao);

        // Montar a lista de DTOs
        List<DocRelacaoDTO> dtos = new ArrayList<>();
        for (DocRelacaoEntity doc : docs) {
            DocRelacaoDTO dto = new DocRelacaoDTO();
            dto.setIdRelacao(Math.toIntExact(doc.getIdRelacao()));
            dto.setTipoRelacao(doc.getTipoRelacao());
            dto.setIdTpDoc(String.valueOf(doc.getIdTpDoc()));
            dto.setEstado(doc.getEstado());
            dto.setPath(doc.getPath());
            dto.setFileName(doc.getFileName());
            dto.setAppCode(doc.getAppCode());
            String idTpDoc = dto.getIdTpDoc();

            if (idTpDoc != null && !idTpDoc.equalsIgnoreCase("null") && !idTpDoc.isBlank()) {
                dto.setFileName(eqvTTipoDocumentoService.buscarNomePorId(Integer.parseInt(idTpDoc)));
            }


            dtos.add(dto);
        }

        return dtos;
    }
}

