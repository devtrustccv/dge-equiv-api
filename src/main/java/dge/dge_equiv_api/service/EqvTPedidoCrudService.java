package dge.dge_equiv_api.service;

import dge.dge_equiv_api.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.document.dto.DocumentoDTO;
import dge.dge_equiv_api.document.service.DocumentService;
import dge.dge_equiv_api.document.service.DocumentServiceImpl;
import dge.dge_equiv_api.model.dto.*;
import dge.dge_equiv_api.model.entity.*;
import dge.dge_equiv_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EqvTPedidoCrudService {

    private static final Logger log = LoggerFactory.getLogger(EqvTPedidoCrudService.class);

    @Autowired private EqvTPedidoRepository pedidoRepository;
    @Autowired private EqvTRequerenteRepository requerenteRepository;
    @Autowired private EqvTInstEnsinoRepository instEnsinoRepository;
    @Autowired private EqvTRequisicaoRepository requisicaoRepository;
    @Autowired private DocumentService documentService;
    @Autowired private AcompanhamentoService acompanhamentoService;
    @Autowired
    private DocumentServiceImpl documentServiceImpl;

    public List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO) {

        EqvTRequerente requerente = new EqvTRequerente();
        copyRequerenteFields(requerente, requerenteDTO);
        requerente = requerenteRepository.save(requerente);

        EqvTRequisicao requisicao = new EqvTRequisicao();
        copyRequisicaoFields(requisicao, requisicaoDTO);
        requisicao = requisicaoRepository.save(requisicao);

        List<EqvtPedidoDTO> result = new ArrayList<>();
        List<EqvTPedido> pedidosSalvos = new ArrayList<>();

        for (EqvtPedidoDTO dto : pedidosDTO) {
            EqvTPedido pedido = new EqvTPedido();
            copyPedidoFields(pedido, dto);
            pedido.setRequerente(requerente);
            pedido.setRequisicao(requisicao);

            // inst ensino

            if (dto.getInstEnsino() != null) {
                EqvTInstEnsino inst = instEnsinoRepository
                        .findById(dto.getInstEnsino().getId())
                        .orElseGet(() -> {
                            EqvTInstEnsino novo = new EqvTInstEnsino();
                            copyInstEnsinoFields(novo, dto.getInstEnsino());
                            return instEnsinoRepository.save(novo);
                        });
                pedido.setInstEnsino(inst);
            }

            pedido = pedidoRepository.save(pedido);
            pedidosSalvos.add(pedido); // guardamos os pedidos com ID

            salvarDocumentosDoPedido(dto, pedido);
            result.add(convertToDTO(pedido));
        }

        // Agora sim: criar um único acompanhamento após todos os pedidos estarem salvos
        AcompanhamentoDTO acompanhamento = montarAcompanhamentoDTO(requisicao, pedidosSalvos);
        if (acompanhamento != null) {
            acompanhamentoService.criarAcompanhamento(acompanhamento);
            log.info("Acompanhamento criado com sucesso para requisição {}", requisicao.getId());
        }

        return result;
    }


    public List<EqvtPedidoDTO> updatePedidosByRequisicaoId(Integer requisicaoId, PortalPedidosDTO dto) {
        EqvTRequisicao requisicao = requisicaoRepository.findById(requisicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));

        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

        if (dto.getRequisicao() != null) {
            copyRequisicaoFields(requisicao, dto.getRequisicao());
            requisicaoRepository.save(requisicao);
        }

        EqvTRequerente requerente = null;
        if (dto.getRequerente() != null) {
            requerente = dto.getRequerente().getId() != null ?
                    requerenteRepository.findById(dto.getRequerente().getId()).orElse(new EqvTRequerente()) :
                    new EqvTRequerente();

            copyRequerenteFields(requerente, dto.getRequerente());
            requerente = requerenteRepository.save(requerente);
        }

        List<EqvtPedidoDTO> result = new ArrayList<>();
        List<EqvtPedidoDTO> pedidosDTO = dto.getPedidos();

        for (int i = 0; i < pedidos.size(); i++) {
            EqvTPedido pedido = pedidos.get(i);
            EqvtPedidoDTO novo = pedidosDTO.get(i);

            copyPedidoFields(pedido, novo);
            pedido.setRequerente(requerente);
            pedido.setRequisicao(requisicao);
            pedido = pedidoRepository.save(pedido);

            salvarDocumentosDoPedido(novo, pedido);
            result.add(convertToDTO(pedido));
        }

        return result;
    }

//    public void deletePedido(Integer id) {
//        if (!pedidoRepository.existsById(id)) {
//            throw new EntityNotFoundException("Pedido não encontrado com ID: " + id);
//        }
//        pedidoRepository.deleteById(id);
//        log.info("Pedido {} deletado com sucesso.", id);
//    }

    public List<EqvtPedidoDTO> findAll() {
        return pedidoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public EqvtPedidoDTO findById(Integer id) {
        EqvTPedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        return convertToDTO(pedido);
    }

    private void salvarDocumentosDoPedido(EqvtPedidoDTO dto, EqvTPedido pedido) {
        List<DocumentoDTO> docs = dto.getDocumentos();

        if (docs == null || docs.isEmpty()) {
            log.warn("Nenhum documento recebido para o pedido {}", pedido.getId());
            return;
        }

        log.info("Salvando {} documentos para o pedido {}", docs.size(), pedido.getId());

        for (DocumentoDTO doc : docs) {
            if (doc.getFile() == null) {
                log.warn("Documento {} sem arquivo", doc.getNome());
                continue;
            }

            try {
                documentService.save(DocRelacaoDTO.builder()
                        .idRelacao(pedido.getId())
                        .tipoRelacao("SOLICITACAO")
                        .estado("A")
                        .appCode("equiv")
                        .idTpDoc(String.valueOf(doc.getIdTpDoc()))
                        .fileName(doc.getNome())
                        .file(doc.getFile())
                        .build()
                );
                log.info("Documento {} salvo com sucesso", doc.getNome());
            } catch (Exception e) {
                log.error("Erro ao salvar documento {}", doc.getNome(), e);
            }
        }
    }

// criar  acompanhamento para cada  pedido
private AcompanhamentoDTO montarAcompanhamentoDTO(EqvTRequisicao requisicao, List<EqvTPedido> pedidos) {
    try {
        AcompanhamentoDTO acomp = new AcompanhamentoDTO();
        acomp.setNumero(String.valueOf(requisicao.getnProcesso()));
        acomp.setAppDad("EQUIV");
        acomp.setPessoaId(12);
        acomp.setEntidadeNif(null);
        acomp.setTipo("PEDIDO_RVCC");

        // Combinações dos títulos
        String titulos = pedidos.stream()
                .map(EqvTPedido::getFormacaoProf)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" | "));
        acomp.setTitulo("Pedido(s): " + titulos);
        acomp.setDescricao("Equivalência para " + titulos);

        // Nome da instituição (se todas forem iguais, ou pegar da primeira)
        String entidade = pedidos.stream()
                .map(p -> p.getInstEnsino() != null ? p.getInstEnsino().getNome() : null)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
        acomp.setEntidade(entidade);

        acomp.setPercentagem(10);
        acomp.setDataInicio(LocalDateTime.now());
        acomp.setDataFim(LocalDateTime.now());
        acomp.setDataFimPrevisto(LocalDate.now().plusDays(14));
        acomp.setEtapaAtual("Solicitacao");
        acomp.setEstado("EM_PROGRESSO");
        acomp.setEstadoDesc("Em Progresso");

        // Adiciona um mapa com todas as formações
        Map<String, String> detalhes = new LinkedHashMap<>();
        for (EqvTPedido p : pedidos) {
            detalhes.put("Formação " + p.getId(), p.getFormacaoProf());
        }
        acomp.setDetalhes(detalhes);

        acomp.setEventos(List.of(
                new AcompanhamentoDTO.Evento("Etapa 1", "Iniciado", LocalDateTime.now())
        ));
        acomp.setComunicacoes(List.of(
                new AcompanhamentoDTO.Comunicacao("Notificação", "Faltam documentos", LocalDateTime.now(),
                        Map.of("Proximo_Passo", "Análise"))
        ));
        acomp.setOutputs(List.of());

        return acomp;
    } catch (Exception e) {
        log.error("Erro ao montar AcompanhamentoDTO", e);
        return null;
    }
}


    //pegar todos od pedidos e os seus documentos associoados
    public List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId) {
        EqvTRequisicao requisicao = requisicaoRepository.findById(requisicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));
        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);
        return pedidos.stream().map(pedido -> {
            EqvtPedidoDTO dto = convertToDTO(pedido);
            dto.setDocumentos(documentService.getDocumentosPorRelacao(pedido.getId(), "SOLICITACAO", "equiv"));
            return dto;
        }).collect(Collectors.toList());
    }


    // ======== Helper methods para copiar campos ========
    private void copyPedidoFields(EqvTPedido pedido, EqvtPedidoDTO dto) {
        pedido.setFormacaoProf(dto.getFormacaoProf());
        pedido.setCarga(dto.getCarga());
        pedido.setAnoInicio(dto.getAnoInicio());
        pedido.setAnoFim(dto.getAnoFim());
        pedido.setNivel(dto.getNivel());
        pedido.setFamilia(dto.getFamilia());
        pedido.setNumDeclaracao(dto.getNumDeclaracao());
        pedido.setDataDespacho(dto.getDataDespacho());
    }

    private void copyRequerenteFields(EqvTRequerente requerente, EqvTRequerenteDTO dto) {
        requerente.setNome(dto.getNome());
        requerente.setDocNumero(dto.getDocNumero());
        requerente.setNif(dto.getNif());
        requerente.setDataEmissaoDoc(dto.getDataEmissaoDoc());
        requerente.setDataValidadeDoc(dto.getDataValidadeDoc());
        requerente.setDataNascimento(dto.getDataNascimento());
        requerente.setNacionalidade(dto.getNacionalidade());
        requerente.setSexo(dto.getSexo());
        requerente.setHabilitacao(Integer.valueOf(dto.getHabilitacao()));
        requerente.setDocIdentificacao(dto.getDocIdentificacao());
        requerente.setDateCreate(LocalDate.now());
        requerente.setDataUpdate(dto.getDataUpdate());
    }

    private void copyInstEnsinoFields(EqvTInstEnsino instEnsino, EqvTInstEnsinoDTO dto) {
        instEnsino.setNome(dto.getNome());
        instEnsino.setPais(dto.getPais());
    }

    private void copyRequisicaoFields(EqvTRequisicao requisicao, EqvTRequisicaoDTO dto) {
        requisicao.setnProcesso(dto.getnProcesso());
        requisicao.setStatus(1);
        requisicao.setEtapa(1);
        requisicao.setDataCreate(LocalDate.now());
        requisicao.setDataUpdate(dto.getDataUpdate());
        requisicao.setUserCreate(dto.getUserCreate());
        requisicao.setUserUpdate(dto.getUserUpdate());
    }

    // ======== Conversão Entity -> DTO ========
    private EqvtPedidoDTO convertToDTO(EqvTPedido pedido) {
        EqvtPedidoDTO dto = new EqvtPedidoDTO();
        dto.setId(pedido.getId());
        dto.setFormacaoProf(pedido.getFormacaoProf());
        dto.setCarga(pedido.getCarga());
        dto.setAnoInicio(pedido.getAnoInicio());
        dto.setAnoFim(pedido.getAnoFim());
        dto.setNivel(pedido.getNivel());
        dto.setFamilia(pedido.getFamilia());

        List<DocumentoDTO> docs = new ArrayList<>();


//        if (pedido.getId() != null) {
//            DocumentoDTO doc = new DocumentoDTO();
//            doc.setNome("Documento 1");
//            doc.setUrlArquivo("http://localhost:8083/api/documentos/preview-by-tipo-rel"
//                    + "?idRelacao=" + pedido.getId()
//                    + "&tipoRelacao=SOLICITACAO"
//                    + "&appCode=equiv");
//            docs.add(doc);
//        }

        dto.setDocumentos(docs);


        if (pedido.getRequerente() != null) {
            EqvTRequerenteDTO requerenteDTO = new EqvTRequerenteDTO();
            requerenteDTO.setId(pedido.getRequerente().getId());
            requerenteDTO.setNome(pedido.getRequerente().getNome());
            requerenteDTO.setNif(pedido.getRequerente().getNif());
            requerenteDTO.setDocIdentificacao(pedido.getRequerente().getDocIdentificacao());
            requerenteDTO.setDataEmissaoDoc(pedido.getRequerente().getDataEmissaoDoc());
            requerenteDTO.setDataValidadeDoc(pedido.getRequerente().getDataValidadeDoc());
            requerenteDTO.setDocNumero(pedido.getRequerente().getDocNumero());
            requerenteDTO.setDataNascimento(pedido.getRequerente().getDataNascimento());
            requerenteDTO.setNacionalidade(pedido.getRequerente().getNacionalidade());
            requerenteDTO.setSexo(pedido.getRequerente().getSexo());
            requerenteDTO.setHabilitacao(String.valueOf(pedido.getRequerente().getHabilitacao()));
            requerenteDTO.setDocIdentificacao(pedido.getRequerente().getDocIdentificacao());
            requerenteDTO.setDateCreate(pedido.getRequerente().getDateCreate());
            requerenteDTO.setDataUpdate(pedido.getRequerente().getDataUpdate());
            dto.setRequerente(requerenteDTO);
        }

        if (pedido.getInstEnsino() != null) {
            EqvTInstEnsinoDTO instDTO = new EqvTInstEnsinoDTO();
            instDTO.setId(pedido.getInstEnsino().getId());
            instDTO.setNome(pedido.getInstEnsino().getNome());
            instDTO.setPais(pedido.getInstEnsino().getPais());
            dto.setInstEnsino(instDTO);
        }

        if (pedido.getRequisicao() != null) {
            EqvTRequisicaoDTO reqDTO = new EqvTRequisicaoDTO();
            reqDTO.setId(pedido.getRequisicao().getId());
            reqDTO.setnProcesso(pedido.getRequisicao().getnProcesso());
            reqDTO.setStatus(pedido.getRequisicao().getStatus());
            reqDTO.setEtapa(pedido.getRequisicao().getEtapa());
            reqDTO.setDataCreate(pedido.getRequisicao().getDataCreate());
            reqDTO.setDataUpdate(pedido.getRequisicao().getDataUpdate());
            dto.setRequisicao(reqDTO);
        }

        return dto;
    }

    public PedidoSimplesDTO convertToSimplesDTO(EqvtPedidoDTO pedido) {
        PedidoSimplesDTO dto = new PedidoSimplesDTO();
        dto.setId(pedido.getId());
        dto.setFormacaoProf(pedido.getFormacaoProf());
        dto.setCarga(pedido.getCarga());
        dto.setAnoInicio(pedido.getAnoInicio());
        dto.setAnoFim(pedido.getAnoFim());
        dto.setNivel(pedido.getNivel());
        dto.setFamilia(pedido.getFamilia());
        dto.setDespacho(String.valueOf(pedido.getDespacho()));
        dto.setNumDeclaracao(pedido.getNumDeclaracao());
        dto.setDataDespacho(pedido.getDataDespacho());

        return dto;
    }


}
