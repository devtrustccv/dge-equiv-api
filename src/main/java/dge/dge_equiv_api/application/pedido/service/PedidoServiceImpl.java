package dge.dge_equiv_api.application.pedido.service;
import dge.dge_equiv_api.Utils.AESUtil;
import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.application.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.application.certificado.config.ReporterConfig;
import dge.dge_equiv_api.application.certificado.dto.CertificadoEquivalenciaDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
import dge.dge_equiv_api.application.document.service.DocumentService;
import dge.dge_equiv_api.application.duc.service.PagamentoService;
import dge.dge_equiv_api.application.geografia.service.GlobalGeografiaService;
import dge.dge_equiv_api.application.notification.dto.NotificationRequestDTO;
import dge.dge_equiv_api.application.notification.service.NotificationService;
import dge.dge_equiv_api.application.pedido.dto.*;
import dge.dge_equiv_api.application.pedido.mapper.PedidoMapper;
import dge.dge_equiv_api.application.pedido.service.PedidoService;
import dge.dge_equiv_api.application.process.service.ProcessService;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.*;
import dge.dge_equiv_api.infrastructure.primary.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final EqvTPedidoRepository pedidoRepository;
    private final EqvTRequerenteRepository requerenteRepository;
    private final EqvTInstEnsinoRepository instEnsinoRepository;
    private final EqvTRequisicaoRepository requisicaoRepository;
    private final DocumentService documentService;
    private final AcompanhamentoService acompanhamentoService;
    private final NotificationService notificationService;
    private final ProcessService processService;
    private final GlobalGeografiaService globalGeografiaService;
    private final PagamentoService pagamentoService;
    private final PedidoMapper pedidoMapper;
    private final ReporterConfig reporterConfig;


    @Value("${link.mf.duc}")
    private String mfkink;

    @Value("${link.api.duc.check}")
    private String ducCheck;

    @Value("${link.report.integration.sigof.duc}")
    private String reporterDuc;

    @Override
    @Transactional
    public List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO,
            Integer pessoaId) {

        validarPessoaId(pessoaId);
        validarInputs(pedidosDTO, requerenteDTO);

        // Salvar requerente
        EqvTRequerente requerente = salvarRequerente(requerenteDTO);

        // Processar instituições
        Map<Integer, EqvTInstEnsino> instituicoesProcessadas = processarInstituicoes(pedidosDTO);

        // Salvar requisição
        EqvTRequisicao requisicao = salvarRequisicao(requisicaoDTO, pessoaId);

        // Iniciar processo
        String processInstanceId = iniciarProcessoComValidacao(requerente, pedidosDTO, instituicoesProcessadas);
        requisicao.setNProcesso(Integer.valueOf(processInstanceId));
       // EqvTPagamento duc = pagamentoService.gerarDuc(null, requerenteDTO.getNif().toString(), requisicao.getNProcesso());
        requisicaoRepository.save(requisicao);

        // Salvar pedidos e documentos
        List<EqvTPedido> pedidosSalvos = salvarPedidosEDocumentos(pedidosDTO, requerente, requisicao, instituicoesProcessadas);

        // Criar acompanhamento
        criarAcompanhamento(requisicao, pedidosSalvos, pessoaId);
        //enviarEmailConfirmacao(requerente, requisicao, duc);

        return pedidosSalvos.stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EqvtPedidoDTO> findAll() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EqvtPedidoDTO findById(Integer id) {
        EqvTPedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        return pedidoMapper.toDto(pedido);
    }

    @Override
    public List<EqvtPedidoDTO> findPedidosByRequisicaoId(Integer idRequisicao) {
        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicaoId(idRequisicao);
        return pedidoMapper.toDtoList(pedidos);
    }

    @Override
    public List<EqvtPedidoDTO> findPedidosComDocumentosByRequisicao(Integer requisicaoId) {
        EqvTRequisicao requisicao = requisicaoRepository.findById(requisicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));

        return pedidoRepository.findByRequisicao(requisicao).stream()
                .map(pedido -> {
                    EqvtPedidoDTO dto = pedidoMapper.toDto(pedido);
                    List<DocumentoResponseDTO> documentos = documentService.getDocumentosPorRelacao(
                            pedido.getId(), "SOLITACAO", "equiv"
                    );
                    dto.setDocumentosresp(documentos);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
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
        dto.setNumeroProcesso(obterNumeroProcesso(pedidoDTO));
        dto.setPaisOrigem(obterPaisOrigem(pedidoDTO));

        return dto;
    }

    @Override
    public List<CertificadoEquivalenciaDTO> montarCertificadosPorRequisicao(Integer idRequisicao) {
        List<EqvtPedidoDTO> pedidos = findPedidosByRequisicaoId(idRequisicao);
        return pedidos.stream()
                .map(this::montarCertificado)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoSimplesDTO convertToSimplesDTO(EqvtPedidoDTO pedido) {
        return pedidoMapper.toSimplesDto(pedido);
    }

    // Private helper methods
    private void validarPessoaId(Integer pessoaId) {
        if (pessoaId == null) {
            throw new BusinessException("O campo 'pessoaId' é obrigatório para o registro do acompanhamento.");
        }
    }

    private void validarInputs(List<EqvtPedidoDTO> pedidosDTO, EqvTRequerenteDTO requerenteDTO) {
        if (pedidosDTO == null || pedidosDTO.isEmpty()) {
            throw new IllegalArgumentException("Lista de pedidos não pode ser nula ou vazia");
        }
        if (requerenteDTO == null) {
            throw new IllegalArgumentException("RequerenteDTO não pode ser nulo");
        }
    }

    private EqvTRequerente salvarRequerente(EqvTRequerenteDTO requerenteDTO) {
        EqvTRequerente requerente = pedidoMapper.toRequerenteEntity(requerenteDTO);
        requerente.setDateCreate(LocalDate.now());
        return requerenteRepository.save(requerente);
    }

    private Map<Integer, EqvTInstEnsino> processarInstituicoes(List<EqvtPedidoDTO> pedidosDTO) {
        Map<Integer, EqvTInstEnsino> instituicoesProcessadas = new HashMap<>();

        for (EqvtPedidoDTO dto : pedidosDTO) {
            if (dto.getInstEnsino() != null) {
                EqvTInstEnsino inst = processarInstituicaoEnsino(dto.getInstEnsino());
                instituicoesProcessadas.put(inst.getId(), inst);
                dto.getInstEnsino().setId(inst.getId());
            }
        }

        return instituicoesProcessadas;
    }

    private EqvTInstEnsino processarInstituicaoEnsino(EqvTInstEnsinoDTO dto) {
        if (dto.getId() != null) {
            return instEnsinoRepository.findById(dto.getId())
                    .orElseGet(() -> criarNovaInstituicao(dto));
        }
        return criarNovaInstituicao(dto);
    }

    private EqvTInstEnsino criarNovaInstituicao(EqvTInstEnsinoDTO dto) {
        if (dto.getId() != null) {
            return instEnsinoRepository.findById(dto.getId())
                    .orElseThrow(() -> new BusinessException("Instituição existente não encontrada com ID: " + dto.getId()));
        }

        if (dto.getNome() == null || dto.getPais() == null) {
            throw new BusinessException("Nome e país da instituição são obrigatórios.");
        }

        Optional<EqvTInstEnsino> existente = instEnsinoRepository.findByNomeIgnoreCaseAndPais(
                dto.getNome().trim(), dto.getPais().trim()
        );

        if (existente.isPresent()) {
            String nomePais = globalGeografiaService.buscarNomePorCodigoPais(dto.getPais());
            throw new BusinessException("Já existe uma instituição com o nome '" + dto.getNome() + "' para o país '" + nomePais + "'.");
        }

        EqvTInstEnsino novo = pedidoMapper.toInstEnsinoEntity(dto);
        novo.setDateCreate(LocalDate.now());
        novo.setStatus("A");
        return instEnsinoRepository.save(novo);
    }

    private EqvTRequisicao salvarRequisicao(EqvTRequisicaoDTO requisicaoDTO, Integer pessoaId) {
        EqvTRequisicao requisicao = pedidoMapper.toRequisicaoEntity(requisicaoDTO);
        requisicao.setStatus(1);
        requisicao.setEtapa(1);
        requisicao.setIdPessoa(pessoaId);
        requisicao.setDataCreate(LocalDate.now());
        return requisicaoRepository.save(requisicao);
    }

    private List<EqvTPedido> salvarPedidosEDocumentos(List<EqvtPedidoDTO> pedidosDTO,
                                                      EqvTRequerente requerente,
                                                      EqvTRequisicao requisicao,
                                                      Map<Integer, EqvTInstEnsino> instituicoes) {
        List<EqvTPedido> pedidosSalvos = new ArrayList<>();

        for (EqvtPedidoDTO dto : pedidosDTO) {
            EqvTPedido pedido = pedidoMapper.toEntity(dto);
            pedido.setRequerente(requerente);
            pedido.setStatus(1);
            pedido.setEtapa("Solicitação");
            pedido.setRequisicao(requisicao);

            if (dto.getInstEnsino() != null && dto.getInstEnsino().getId() != null) {
                pedido.setInstEnsino(instituicoes.get(dto.getInstEnsino().getId()));
            }

            pedido = pedidoRepository.save(pedido);
            pedidosSalvos.add(pedido);
            salvarDocumentosDoPedido(dto, pedido);
        }

        return pedidosSalvos;
    }

    private void salvarDocumentosDoPedido(EqvtPedidoDTO dto, EqvTPedido pedido) {
        List<DocumentoDTO> docs = dto.getDocumentos();

        if (docs == null || docs.isEmpty()) {
            log.warn("Nenhum documento recebido para o pedido {}", pedido.getId());
            return;
        }

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
                        .nProcesso(nProcesso)
                        .build());

                log.info("Documento {} salvo com sucesso para processo {}", doc.getNome(), nProcesso);
            } catch (Exception e) {
                log.error("Erro ao salvar documento {}", doc.getNome(), e);
            }
        }
    }

    private String iniciarProcessoComValidacao(EqvTRequerente requerente,
                                               List<EqvtPedidoDTO> pedidosDTO,
                                               Map<Integer, EqvTInstEnsino> instituicoes) {
        try {
            List<EqvTPedido> pedidosTemporarios = pedidosDTO.stream()
                    .map(dto -> {
                        EqvTPedido p = pedidoMapper.toEntity(dto);
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

    private AcompanhamentoDTO montarAcompanhamentoDTO(EqvTRequisicao requisicao, List<EqvTPedido> pedidos, Integer pessoaId) {
        try {
            AcompanhamentoDTO acomp = new AcompanhamentoDTO();
            acomp.setNumero(String.valueOf(requisicao.getNProcesso()));
            acomp.setAppDad("equiv");
            acomp.setPessoaId(pessoaId);
            acomp.setEntidadeNif(null);
            acomp.setTipo("PEDIDO_EQUIV");

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

            // BUSCAR DOCUMENTOS REAIS E CRIAR ANEXOS
            List<AcompanhamentoDTO.Anexo> anexos = new ArrayList<>();
            for (EqvTPedido pedido : pedidos) {
                List<DocumentoResponseDTO> docs = documentService.getDocumentosPorRelacao(
                        pedido.getId(),
                        "SOLITACAO",
                        "equiv"
                );
                for (DocumentoResponseDTO doc : docs) {
                    AcompanhamentoDTO.Anexo anexo = new AcompanhamentoDTO.Anexo();
                    anexo.setTitulo(doc.getName());
                    anexo.setUrl(doc.getPreviewUrl()); // usa previewUrl para abrir
                    anexos.add(anexo);
                }
            }
            acomp.setAnexos(anexos);

            return acomp;
        } catch (Exception e) {
            log.error("Erro ao montar AcompanhamentoDTO", e);
            throw new RuntimeException("Erro ao montar acompanhamento", e);
        }
    }

    private String obterNumeroProcesso(EqvtPedidoDTO pedidoDTO) {
        return pedidoDTO.getRequisicao() != null && pedidoDTO.getRequisicao().getNProcesso() != null
                ? pedidoDTO.getRequisicao().getNProcesso().toString()
                : null;
    }

    private String obterPaisOrigem(EqvtPedidoDTO pedidoDTO) {
        return pedidoDTO.getInstEnsino() != null && pedidoDTO.getInstEnsino().getPais() != null
                ? globalGeografiaService.buscarNomePorCodigoPais(pedidoDTO.getInstEnsino().getPais())
                : null;
    }
}
