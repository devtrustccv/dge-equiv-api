package dge.dge_equiv_api.application.pedido.service;

import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.application.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.application.certificado.config.ReporterConfig;
import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.service.DocumentService;
import dge.dge_equiv_api.application.document.service.DocumentServiceImpl;
import dge.dge_equiv_api.application.duc.service.PagamentoService;
import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.*;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTInstEnsinoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTRequerenteRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTRequisicaoRepository;
import dge.dge_equiv_api.application.notification.dto.NotificationRequestDTO;
import dge.dge_equiv_api.application.notification.service.NotificationService;
import dge.dge_equiv_api.application.process.service.ProcessService;
import dge.dge_equiv_api.application.geografia.service.GlobalGeografiaService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private EqvTPedidoRepository pedidoRepository;
    @Autowired
    private EqvTRequerenteRepository requerenteRepository;
    @Autowired
    private EqvTInstEnsinoRepository instEnsinoRepository;
    @Autowired
    private EqvTRequisicaoRepository requisicaoRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AcompanhamentoService acompanhamentoService;
    @Autowired
    private DocumentServiceImpl documentServiceImpl;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private GlobalGeografiaService globalGeografiaService;

    @Autowired
    private ReporterConfig reporterConfig;
    @Autowired
    private PagamentoService pagamentoService;

    @Value("${link.mf.duc}")
    private String mfkink;

    @Value("${link.api.duc.check}")
    private String ducCheck;

    @Value("${link.report.integration.sigof.duc}")
    private String reporterDuc;


    @Transactional
    public List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId) {

        //validar  pessoa id para o registro do acompanhamento
        if (pessoaId == null) {
            throw new RuntimeException("O campo 'pessoaId' é obrigatório para o registro do acompanhamento. Por favor, forneça um valor válido.");
        }

        // 1. Validate inputs
        if (pedidosDTO == null || pedidosDTO.isEmpty()) {
            throw new IllegalArgumentException("Lista de pedidos não pode ser nula ou vazia");
        }
        if (requerenteDTO == null) {
            throw new IllegalArgumentException("RequerenteDTO não pode ser nulo");
        }

        // 2. Save requerente
        EqvTRequerente requerente = new EqvTRequerente();
        requerente.setDateCreate(LocalDate.now());
        copyRequerenteFields(requerente, requerenteDTO);
        requerente = requerenteRepository.save(requerente);

        // 3. Process institutions first
        Map<Integer, EqvTInstEnsino> instituicoesProcessadas = new HashMap<>();
        for (EqvtPedidoDTO dto : pedidosDTO) {
            if (dto.getInstEnsino() != null) {
                EqvTInstEnsino inst = processarInstituicaoEnsino(dto.getInstEnsino());
                instituicoesProcessadas.put(inst.getId(), inst);
                dto.getInstEnsino().setId(inst.getId());

            }
        }

        // 4. Create and save requisicao
        EqvTRequisicao requisicao = new EqvTRequisicao();
        copyRequisicaoFields(requisicao, requisicaoDTO);


        requisicao.setStatus(1);
        requisicao.setEtapa(1);
        requisicao.setIdPessoa(pessoaId);
        requisicao.setDataCreate(LocalDate.now());

        requisicao = requisicaoRepository.save(requisicao);


        // 5. Start process BEFORE saving pedidos
        String processInstanceId = iniciarProcessoComValidacao(requerente, pedidosDTO, instituicoesProcessadas);
        requisicao.setNProcesso(Integer.valueOf(processInstanceId));
        requisicao = requisicaoRepository.save(requisicao);
        //EqvTPagamento duc = pagamentoService.gerarDuc(null, requerenteDTO.getNif().toString(), requisicao.getNProcesso());
        // 6. Save pedidos and documents
        List<EqvtPedidoDTO> result = new ArrayList<>();
        List<EqvTPedido> pedidosSalvos = new ArrayList<>();

        for (EqvtPedidoDTO dto : pedidosDTO) {
            EqvTPedido pedido = new EqvTPedido();
            copyPedidoFields(pedido, dto);
            pedido.setRequerente(requerente);
            pedido.setStatus(1);
            pedido.setEtapa("Solicitação");
            pedido.setRequisicao(requisicao);

            // Set institution if exists
            if (dto.getInstEnsino() != null && dto.getInstEnsino().getId() != null) {
                pedido.setInstEnsino(instituicoesProcessadas.get(dto.getInstEnsino().getId()));
            }

            pedido = pedidoRepository.save(pedido);
            pedidosSalvos.add(pedido);
            salvarDocumentosDoPedido(dto, pedido);


            // 8. Send confirmation email
            result.add(convertToDTO(pedido));
        }


        // 7. Create acompanhamento
        criarAcompanhamento(requisicao, pedidosSalvos, pessoaId);
        //enviarEmailConfirmacao(requerente, requisicao, duc);


        return result;
    }

    // Helper methods
    private EqvTInstEnsino processarInstituicaoEnsino(EqvTInstEnsinoDTO dto) {
        if (dto.getId() != null) {
            return instEnsinoRepository.findById(dto.getId())
                    .orElseGet(() -> criarNovaInstituicao(dto));
        }
        return criarNovaInstituicao(dto);
    }

    private EqvTInstEnsino criarNovaInstituicao(EqvTInstEnsinoDTO dto) {

        // Se já foi selecionada uma instituição existente, não cria nada novo
        if (dto.getId() != null) {
            return instEnsinoRepository.findById(dto.getId())
                    .orElseThrow(() -> new BusinessException("Instituição existente não encontrada com ID: " + dto.getId()));
        }

        // Validações para criação de nova instituição
        if (dto.getNome() == null || dto.getPais() == null) {
            throw new BusinessException("Nome e país da instituição são obrigatórios.");
        }

        Optional<EqvTInstEnsino> existente = instEnsinoRepository.findByNomeIgnoreCaseAndPais(dto.getNome().trim(), dto.getPais().trim());

        if (existente.isPresent()) {
            throw new BusinessException("Já existe uma instituição com o nome '" + dto.getNome() + "' para o país '" + globalGeografiaService.buscarNomePorCodigoPais(dto.getPais()) + "'.");
        }

        // Criação
        EqvTInstEnsino novo = new EqvTInstEnsino();
        copyInstEnsinoFields(novo, dto);
        novo.setDateCreate(LocalDate.now());
        novo.setStatus("A");
        return instEnsinoRepository.save(novo);
    }


    private String iniciarProcessoComValidacao(EqvTRequerente requerente,
                                               List<EqvtPedidoDTO> pedidosDTO,
                                               Map<Integer, EqvTInstEnsino> instituicoes) {
        try {
            List<EqvTPedido> pedidosTemporarios = pedidosDTO.stream()
                    .map(dto -> {
                        EqvTPedido p = new EqvTPedido();
                        copyPedidoFields(p, dto);

                        // Ensure institution is set properly
                        if (dto.getInstEnsino() != null && dto.getInstEnsino().getId() != null) {
                            p.setInstEnsino(instituicoes.get(dto.getInstEnsino().getId()));
                        }

                        return p;
                    })
                    .collect(Collectors.toList());

            return processService.iniciarProcessoEquivalencia(requerente, pedidosTemporarios);
        } catch (Exception e) {
            log.error("Falha ao iniciar processo de equivalência", e);
            throw new RuntimeException("Não foi possível iniciar o processo de equivalência", e);
        }
    }

    private void criarAcompanhamento(EqvTRequisicao requisicao, List<EqvTPedido> pedidos, Integer pessoaId) {
        try {
            AcompanhamentoDTO acompanhamento = montarAcompanhamentoDTO(requisicao, pedidos, pessoaId);
            if (acompanhamento != null) {
                acompanhamentoService.criarAcompanhamento(acompanhamento);
                log.info("Acompanhamento criado para processo {}", requisicao.getNProcesso());
            }
        } catch (Exception e) {
            log.error("Falha ao criar acompanhamento", e);
        }
    }


//    public List<EqvtPedidoDTO> updatePedidosByRequisicaoId(Integer requisicaoId, PortalPedidosDTO dto) {
//        EqvTRequisicao requisicao = requisicaoRepository.findById(requisicaoId)
//                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));
//
//        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);
//
//        if (dto.getRequisicao() != null) {
//            copyRequisicaoFields(requisicao, dto.getRequisicao());
//            requisicaoRepository.save(requisicao);
//        }
//
//        EqvTRequerente requerente = null;
//        if (dto.getRequerente() != null) {
//            requerente = dto.getRequerente().getId() != null ?
//                    requerenteRepository.findById(dto.getRequerente().getId()).orElse(new EqvTRequerente()) :
//                    new EqvTRequerente();
//
//            copyRequerenteFields(requerente, dto.getRequerente());
//            requerente = requerenteRepository.save(requerente);
//        }
//
//        List<EqvtPedidoDTO> result = new ArrayList<>();
//        List<EqvtPedidoDTO> pedidosDTO = dto.getPedidos();
//
//        for (int i = 0; i < pedidos.size(); i++) {
//            EqvTPedido pedido = pedidos.get(i);
//            EqvtPedidoDTO novo = pedidosDTO.get(i);
//
//            copyPedidoFields(pedido, novo);
//            pedido.setRequerente(requerente);
//            pedido.setRequisicao(requisicao);
//            pedido = pedidoRepository.save(pedido);
//
//            salvarDocumentosDoPedido(novo, pedido);
//            result.add(convertToDTO(pedido));
//        }
//
//        return result;
//    }

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

        // Garante que a requisição e número de processo existem
        if (pedido.getRequisicao() == null || pedido.getRequisicao().getNProcesso() == null) {
            throw new IllegalStateException("Número de processo não disponível para salvar documentos");
        }

        String nProcesso = String.valueOf(pedido.getRequisicao().getNProcesso());

        for (DocumentoDTO doc : docs) {
            if (doc.getFile() == null) {
                log.warn("Documento {} sem arquivo", doc.getNome());
                continue;
            }

            try {
                documentService.save(DocRelacaoDTO.builder()
                        .idRelacao(pedido.getId())
                        .tipoRelacao("SOLITACAO")
                        .estado("A")
                        .appCode("equiv")
                        .idTpDoc(String.valueOf(doc.getIdTpDoc()))
                        .fileName(doc.getNome())
                        .file(doc.getFile())
                        .nProcesso(nProcesso) // Agora temos o número do processo
                        .build());

                log.info("Documento {} salvo com sucesso para processo {}", doc.getNome(), nProcesso);
            } catch (Exception e) {
                log.error("Erro ao salvar documento {}", doc.getNome(), e);
            }
        }
    }

    // criar  acompanhamento para cada  pedido
    private AcompanhamentoDTO montarAcompanhamentoDTO(EqvTRequisicao requisicao, List<EqvTPedido> pedidos, Integer pessoaId) {
        try {
            AcompanhamentoDTO acomp = new AcompanhamentoDTO();
            acomp.setNumero(String.valueOf(requisicao.getNProcesso()));
            acomp.setAppDad("EQUIV");
            acomp.setPessoaId(pessoaId);
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
            //acomp.setDataFimPrevisto(LocalDate.now().plusDays(14));
            acomp.setEtapaAtual("Solicitacao");
            acomp.setEstado("EM_PROGRESSO");
            acomp.setEstadoDesc("Em Progresso");

            // Adiciona um mapa com todas as formações
            Map<String, String> detalhes = new LinkedHashMap<>();
            for (EqvTPedido p : pedidos) {
                detalhes.put("Formação Profissional " + p.getId(), p.getFormacaoProf());
            }
            acomp.setDetalhes(detalhes);

            acomp.setEventos(List.of(
                    new AcompanhamentoDTO.Evento("Etapa 1", "Iniciado", LocalDateTime.now())
            ));
            acomp.setComunicacoes(List.of(
                    new AcompanhamentoDTO.Comunicacao("Notificação", "", LocalDateTime.now(),
                            Map.of("Proximo_Passo", "Análise"))
            ));
            acomp.setOutputs(List.of());

//            List<AcompanhamentoDTO.Anexo> anexos = List.of(
//                    new AcompanhamentoDTO.Anexo("Certificado", LocalDateTime.now(), "https://www.dasinal.cv", false),
//                    new AcompanhamentoDTO.Anexo("CNI", LocalDateTime.now(), "https://www.devtrust.cv", true)
//            );
//            acomp.setAnexos(anexos);


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
            dto.setDocumentos(documentService.getDocumentosPorRelacao(pedido.getId(), "SOLITACAO", "equiv"));
            log.info("Encontrados {} documentos para o pedido {}", dto.getDocumentos().size(), pedido.getId());
            return dto;
        }).collect(Collectors.toList());
    }


    // enviar email
    private void enviarEmailConfirmacao(EqvTRequerente requerente, EqvTRequisicao requisicao, EqvTPagamento pagamento) {
        String nomeRequerente = requerente.getNome() != null ? requerente.getNome() : "";
        String numeroPedido = requisicao.getId() != null && requisicao.getNProcesso() != null
                ? String.valueOf(requisicao.getNProcesso()) : "";


        String urlPagamento = mfkink + pagamento.getEntidade()
                + "&referencia=" + pagamento.getReferencia()
                + "&montante=" + pagamento.getTotal()
                + "&call_back_url=" + ducCheck + pagamento.getNuDuc();
        String linkPagamento = urlPagamento;

        String linkverduc = reporterDuc + pagamento.getNuDuc();

        String assuntoRequerente = "Confirmação de Recebimento - Pedido de Equivalência Profissional";

        String mensagemRequerente =
                "<p>Prezado(a)  <strong> " + nomeRequerente + "</strong>,</p>" +
                        "<p>O seu pedido de equivalência foi registado com sucesso sob o número  <strong> " + numeroPedido + " </strong>.</p>" +
                        "<p>Para dar seguimento ao processo, é necessário efetuar o pagamento da taxa correspondente através do Documento Único de Cobrança (DUC).</p>" +
                        "<p>Por favor, utilize o link abaixo para gerar e pagar o seu DUC:</p>" +
                        "<p><a href=\"" + linkPagamento + "\" style=\"color: blue; text-decoration: underline;\">" +
                        "Clique aqui para realizar o pagamento do DUC</a></p>" +
                        "<p><a href=\"" + linkverduc + "\" style=\"color: blue; text-decoration: underline;\">" +
                        "Clique aqui para ver o DUC gerado</a></p>" +
                        "<p>O processamento do seu pedido só será iniciado após a confirmação do pagamento.</p>" +
                        "<p>Agradecemos a sua atenção e colaboração.</p>" +
                        "<p>Com os melhores cumprimentos,</p>" +
                        "<p>UC-SNQ – Sistema de Equivalência Profissional</p>";


        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setAppName("equiv");
        dto.setSubject(assuntoRequerente);
        dto.setMessage(mensagemRequerente);
        dto.setEmail(requerente.getEmail());

        try {
            notificationService.enviarEmail(dto);
        } catch (Exception e) {
            log.error("Erro ao enviar email de confirmação", e);
        }
    }

    public List<EqvtPedidoDTO> findPedidosByRequisicaoId(Integer idRequisicao) {
        // Aqui você faz a consulta no banco, exemplo com Spring Data JPA:
        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicaoId(idRequisicao);

        // Converter lista de entidade para DTO (implemente o mapper conforme seu projeto)
        return pedidos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para montar o certificado de um pedido
    public CertificadoEquivalenciaDTO montarCertificado(EqvtPedidoDTO pedidoDTO) {
        CertificadoEquivalenciaDTO dto = new CertificadoEquivalenciaDTO();

        String idCriptografado = AESUtil.encrypt(pedidoDTO.getId().toString());
        String url = reporterConfig.getReporterEqvUrl() + "equiv/declaracao_equivalencia/" + idCriptografado;

        dto.setFormacaoOriginal(pedidoDTO.getFormacaoProf());
        dto.setEntidadeOriginal(pedidoDTO.getInstEnsino() != null ? pedidoDTO.getInstEnsino().getNome() : null);
        dto.setEquivalencia("Equivalência Profissional");
        dto.setNivelQualificacao(pedidoDTO.getNivel() != null ? pedidoDTO.getNivel().toString() : null);
        if (pedidoDTO.getDespacho() != null && pedidoDTO.getDespacho().equals("5")) {
            dto.setUrl(url);
        }
        dto.setDataEmissao(LocalDate.now());
        dto.setEntidadeEmissora("DGERT - Direção-Geral do Emprego e das Relações de Trabalho");
        dto.setNumeroProcesso(pedidoDTO.getRequisicao() != null && pedidoDTO.getRequisicao().getNProcesso() != null
                ? pedidoDTO.getRequisicao().getNProcesso().toString()
                : null);
        dto.setPaisOrigem(globalGeografiaService.buscarNomePorCodigoPais(pedidoDTO.getInstEnsino().getPais()));

        return dto;
    }

    // Monta a lista de certificados para todos os pedidos da requisição
    public List<CertificadoEquivalenciaDTO> montarCertificadosPorRequisicao(Integer idRequisicao) {
        List<EqvtPedidoDTO> pedidos = findPedidosByRequisicaoId(idRequisicao);
        return pedidos.stream()
                .map(this::montarCertificado)
                .collect(Collectors.toList());
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
        requerente.setEmail(dto.getEmail());
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
        instEnsino.setDateCreate(dto.getDateCreate());
    }

    private void copyRequisicaoFields(EqvTRequisicao requisicao, EqvTRequisicaoDTO dto) {

        if (dto == null) {
            return; // Ou define valores padrão
        }

        // Só define nProcesso se não for nulo no DTO
        if (dto.getNProcesso() != null) {
            requisicao.setNProcesso(dto.getNProcesso());
        }

        requisicao.setStatus(dto.getStatus());
        requisicao.setEtapa(dto.getEtapa());
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
        dto.setStatus(pedido.getStatus());
        dto.setAnoInicio(pedido.getAnoInicio());
        dto.setAnoFim(pedido.getAnoFim());
        dto.setNivel(pedido.getNivel());
        dto.setFamilia(pedido.getFamilia());
        dto.setEtapa(pedido.getEtapa());

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





        //dto.setDocumentos(docs);


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
            reqDTO.setNProcesso(pedido.getRequisicao().getNProcesso());
            reqDTO.setStatus(pedido.getRequisicao().getStatus());
            reqDTO.setEtapa(pedido.getRequisicao().getEtapa());
            reqDTO.setDataCreate(pedido.getRequisicao().getDataCreate());
            reqDTO.setDataUpdate(pedido.getRequisicao().getDataUpdate());
            dto.setRequisicao(reqDTO);
        }

        if (pedido.getRequisicao() != null && pedido.getRequisicao().getNProcesso() != null) {
            EqvTPagamento pagamento = pagamentoService.buscarPagamentoPorProcesso(pedido.getRequisicao().getNProcesso());
            if (pagamento != null) {
                String urlPagamento = mfkink + pagamento.getEntidade()
                        + "&referencia=" + pagamento.getReferencia()
                        + "&montante=" + pagamento.getTotal()
                        + "&call_back_url=" + ducCheck + pagamento.getNuDuc();

                String linkverduc = reporterDuc + pagamento.getNuDuc();
                dto.setUrlDucPagamento(urlPagamento);
                dto.setNuDuc(pagamento.getNuDuc().toString());
                dto.setEntidade(pagamento.getEntidade());
                dto.setReferencia(pagamento.getReferencia().toString());
                dto.setVerduc(linkverduc);

            }

        }

        return dto;
    }


    //  envair email para cada pedido
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
