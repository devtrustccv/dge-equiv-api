package dge.dge_equiv_api.service;



import dge.dge_equiv_api.model.dto.DocRelacaoDTO;
import dge.dge_equiv_api.model.dto.TipoDocumentoDTO;
import dge.dge_equiv_api.model.entity.DocRelacaoEntity;
import dge.dge_equiv_api.model.entity.EqvTTipoDocumento;
import dge.dge_equiv_api.repository.DocRelacaoRepository;
import dge.dge_equiv_api.repository.EqvTTipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            dto.setFileName(eqvTTipoDocumentoService.buscarNomePorId(Integer.parseInt(dto.getIdTpDoc())));

            dtos.add(dto);
        }

        return dtos;
    }
}

