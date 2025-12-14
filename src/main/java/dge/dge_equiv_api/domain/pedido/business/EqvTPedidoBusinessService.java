package dge.dge_equiv_api.domain.pedido.business;

import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
import dge.dge_equiv_api.application.document.service.DocumentService;
import dge.dge_equiv_api.application.motivo_retidicado.dto.MotivoRetificacaoResponseDTO;
import dge.dge_equiv_api.application.motivo_retidicado.service.MotivoRetificacaoService;
import dge.dge_equiv_api.application.notification.dto.NotificationRequestDTO;
import dge.dge_equiv_api.application.notification.service.NotificationService;

import dge.dge_equiv_api.application.pedidov01.dto.*;
import dge.dge_equiv_api.application.pedidov01.mapper.PedidoMapper;
import dge.dge_equiv_api.application.pedidov01.service.EqvTPedidoService;
import dge.dge_equiv_api.application.process.service.ProcessService;
import dge.dge_equiv_api.application.duc.service.PagamentoService;
import dge.dge_equiv_api.application.reclamacao.dto.ReclamacaoViewDTO;
import dge.dge_equiv_api.application.taxa.service.EqvTTaxaService;
import dge.dge_equiv_api.application.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.application.geografia.service.GlobalGeografiaService;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.*;
import dge.dge_equiv_api.infrastructure.primary.repository.*;
import dge.dge_equiv_api.infrastructure.quaternary.TNotificacaoConfigEmail;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EqvTPedidoBusinessService {

    private final EqvTPedidoRepository pedidoRepository;
    private final EqvTRequerenteRepository requerenteRepository;
    private final EqvTInstEnsinoRepository instEnsinoRepository;
    private final EqvTRequisicaoRepository requisicaoRepository;
    private final EqvtTDecisaoDespachoRepository despachoRepository;
    private final DocumentService documentService;
    private final AcompanhamentoService acompanhamentoService;
    private final NotificationService notificationService;
    private final ProcessService processService;
    private final PagamentoService pagamentoService;
    private final EqvTTaxaService taxaService;
    private final PedidoMapper pedidoMapper;
    private final MotivoRetificacaoService motivoRetificacaoService;
    private final GlobalGeografiaService globalGeografiaService;


    @Value("${link.mf.duc}")
    private String mfkink;

    @Value("${link.api.duc.check}")
    private String ducCheck;

    @Value("${link.report.integration.sigof.duc}")
    private String reporterDuc;

    public List<EqvTPedido> criarLotePedidos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicao requisicao,
            EqvTRequerente requerente,
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
        }

        return pedidosSalvos;
    }

    public EqvTRequerente processarRequerente(EqvTRequerenteDTO requerenteDTO, Integer pessoaId) {
        if (pessoaId == null) {
            throw new BusinessException("O campo 'pessoaId' é obrigatório.");
        }

        // Buscar requerente existente pelo pessoaId
        EqvTRequerente requerente = requerenteRepository.findByIdPessoa(pessoaId);

        if (requerente == null) {
            // Criar novo requerente
            requerente = pedidoMapper.toRequerenteEntity(requerenteDTO);
            requerente.setDateCreate(LocalDate.now());
            requerente.setIdPessoa(pessoaId);
            log.info("Novo requerente criado para pessoaId: {}", pessoaId);
        } else {
            // Atualizar dados do requerente existente
            log.info("Requerente existente encontrado (ID: {}), atualizando dados", requerente.getId());
            pedidoMapper.updateRequerenteFromDTO(requerenteDTO, requerente);
            requerente.setDataUpdate(LocalDate.now()); // Adicionar campo de data de atualização se necessário
        }

        requerente = requerenteRepository.save(requerente);
        log.info("Requerente salvo/atualizado com ID: {}", requerente.getId());

        return requerente;
    }

    public EqvTRequisicao processarRequisicao(EqvTRequisicaoDTO requisicaoDTO, Integer pessoaId) {
        // CORREÇÃO: Aceitar null e criar um padrão
        if (requisicaoDTO == null) {
            log.info("Criando requisicaoDTO padrão para pessoaId: {}", pessoaId);
            requisicaoDTO = EqvTRequisicaoDTO.builder()
                    .dataCreate(LocalDate.now())
                    .status(1)
                    .etapa(1)
                    .idPessoa(pessoaId)
                    .userCreate(pessoaId)
                    .build();
        }

        log.info("Processando requisição - DTO: {}, pessoaId: {}", requisicaoDTO, pessoaId);

        // DEBUG: Verificar se o mapper funciona
        EqvTRequisicao requisicao = pedidoMapper.safeToRequisicaoEntity(requisicaoDTO);

        if (requisicao == null) {
            log.error("Mapper retornou null mesmo com safeToRequisicaoEntity");
            requisicao = new EqvTRequisicao();
        }

        // Configurar campos que o mapper ignora
        requisicao.setStatus(1);
        requisicao.setEtapa(1);
        requisicao.setIdPessoa(pessoaId);
        requisicao.setDataCreate(LocalDate.now());

        log.info("Requisicao a ser salva: idPessoa={}, status={}, etapa={}",
                requisicao.getIdPessoa(), requisicao.getStatus(), requisicao.getEtapa());

        EqvTRequisicao saved = requisicaoRepository.save(requisicao);
        log.info("Requisicao salva com ID: {}", saved.getId());

        return saved;
    }

    public Map<Integer, EqvTInstEnsino> processarInstituicoesEnsino(List<EqvtPedidoDTO> pedidosDTO) {
        Map<Integer, EqvTInstEnsino> instMap = new HashMap<>();

        for (EqvtPedidoDTO dto : pedidosDTO) {
            if (dto.getInstEnsino() != null) {
                EqvTInstEnsino inst = processarInstituicaoEnsino(dto.getInstEnsino());
                instMap.put(inst.getId(), inst);
                dto.getInstEnsino().setId(inst.getId());
            }
        }

        return instMap;
    }

    public EqvTInstEnsino processarInstituicaoEnsino(EqvTInstEnsinoDTO dto) {
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
            throw new BusinessException("Já existe uma instituição com o nome '" + dto.getNome() +
                    "' para o país '" + dto.getPais() + "'.");
        }

        EqvTInstEnsino novo = pedidoMapper.toInstEnsinoEntity(dto);
        novo.setDateCreate(LocalDate.now());
        novo.setStatus("A");
        return instEnsinoRepository.save(novo);
    }

    public void validarDadosCriacao(List<EqvtPedidoDTO> pedidosDTO, EqvTRequerenteDTO requerenteDTO) {
        if (pedidosDTO == null || pedidosDTO.isEmpty()) {
            throw new IllegalArgumentException("Lista de pedidos não pode ser nula ou vazia.");
        }
        if (requerenteDTO == null) {
            throw new IllegalArgumentException("RequerenteDTO não pode ser nulo.");
        }
    }

    public String iniciarProcesso(EqvTRequerente requerente, List<EqvtPedidoDTO> pedidosDTO,
                                  Map<Integer, EqvTInstEnsino> instituicoes) {
        try {
            List<EqvTPedido> pedidosTemporarios = new ArrayList<>();

            for (EqvtPedidoDTO dto : pedidosDTO) {
                EqvTPedido pedido = pedidoMapper.toEntity(dto);

                // Preserva a instituição de ensino do DTO
                if (dto.getInstEnsino() != null && dto.getInstEnsino().getId() != null) {
                    EqvTInstEnsino instituicao = instituicoes.get(dto.getInstEnsino().getId());
                    if (instituicao != null) {
                        pedido.setInstEnsino(instituicao);
                    } else {
                        log.warn("Instituição não encontrada no mapa para ID: {}", dto.getInstEnsino().getId());
                    }
                }

                pedidosTemporarios.add(pedido);
            }

            return processService.iniciarProcessoEquivalencia(requerente, pedidosTemporarios);
        } catch (Exception e) {
            log.error("Falha ao iniciar processo de equivalência");
            throw new BusinessException("Não foi possível iniciar o processo de equivalência");
        }
    }


    public EqvTPagamento gerarDUC(EqvTRequerenteDTO requerenteDTO, EqvTRequisicao requisicao) {
        try {
            return pagamentoService.gerarDuc(null, requerenteDTO.getNif().toString(),
                    requisicao.getNProcesso(), null);
        } catch (Exception e) {
            log.error("Erro ao gerar DUC");
            throw new BusinessException("Erro ao gerar o DUC.");
        }
    }

    public void salvarDocumentosDosPedidos(List<EqvtPedidoDTO> pedidosDTO, List<EqvTPedido> pedidosSalvos) {
        for (int i = 0; i < pedidosDTO.size(); i++) {
            salvarDocumentosDoPedido(pedidosDTO.get(i), pedidosSalvos.get(i));
        }
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
                        .fileName(normalizeText(doc.getNome()))
                        .file(doc.getFile())
                        .nProcesso(nProcesso)
                        .build());

                log.info("Documento {} salvo com sucesso para processo {}", doc.getNome(), nProcesso);
            } catch (Exception e) {
                log.error("Erro ao salvar documento {}", doc.getNome());
                throw new BusinessException("Erro ao salvar documento: " + doc.getNome());
            }
        }
    }

    public void criarAcompanhamento(EqvTRequisicao requisicao, List<EqvTPedido> pedidos,
                                    Integer pessoaId, EqvTPagamento pagamento) {
        try {
            AcompanhamentoDTO acompanhamento = montarAcompanhamentoDTO(requisicao, pedidos, pessoaId, pagamento);
            if (acompanhamento != null) {
                acompanhamentoService.criarAcompanhamento(acompanhamento);
                log.info("Acompanhamento criado para processo {}", requisicao.getNProcesso());
            }
        } catch (Exception e) {
            log.error("Falha ao criar acompanhamento");
            throw new BusinessException("Erro ao criar acompanhamento");
        }
    }

    private AcompanhamentoDTO montarAcompanhamentoDTO(EqvTRequisicao requisicao, List<EqvTPedido> pedidos,
                                                      Integer pessoaId, EqvTPagamento pagamento) {
        try {
            BigDecimal valorTaxa = taxaService.getValorAtivoParaPagamentoAnalise();

            String titulos = pedidos.stream()
                    .map(EqvTPedido::getFormacaoProf)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" | "));

            Map<String, String> detalhes = new LinkedHashMap<>();
            for (int i = 0; i < pedidos.size(); i++) {
                EqvTPedido p = pedidos.get(i);
                detalhes.put("Formação Solicitada ", p.getFormacaoProf());
                detalhes.put("Instituição ", p.getInstEnsino() != null ? p.getInstEnsino().getNome() : null);
            }

            detalhes.put("Taxa Análise", valorTaxa.toString() + " $00");
            detalhes.put("Taxa Certificado", taxaService.getValorAtivoParaPagamentoCertificado().toString() + " $00");

            String urlPagamento = mfkink + pagamento.getEntidade()
                    + "&referencia=" + pagamento.getReferencia()
                    + "&montante=" + pagamento.getTotal()
                    + "&call_back_url=" + ducCheck + pagamento.getNuDuc();

            List<AcompanhamentoDTO.Evento> eventos = List.of(
                    new AcompanhamentoDTO.Evento(
                            "Processo Criado",
                            "Solicitação registada no sistema. Aguardando pagamento da taxa de análise para dar início à avaliação.",
                            LocalDateTime.now(),
                            Map.of(
                                    "Referencia", pagamento.getReferencia().toString(),
                                    "Entidade", pagamento.getEntidade(),
                                    "Valor", valorTaxa + " $00",
                                    "Pagar Online", urlPagamento,
                                    "Ver duc", pagamento.getLinkDuc()
                            )
                    )
            );

            List<AcompanhamentoDTO.Anexo> anexos = new ArrayList<>();
            for (EqvTPedido pedido : pedidos) {
                List<DocumentoResponseDTO> docs = documentService.getDocumentosPorRelacao(
                        pedido.getId(),
                        "SOLITACAO",
                        "equiv"
                );


                for (DocumentoResponseDTO doc : docs) {
                    String path = doc.getPath();

                    String publicUrl = documentService.gerarLinkPublico(path);
                    AcompanhamentoDTO.Anexo anexo = new AcompanhamentoDTO.Anexo(
                            doc.getFileName(),
                            LocalDateTime.now(),
                            publicUrl,
                            true
                    );
                    anexos.add(anexo);
                }
            }

            AcompanhamentoDTO acomp = new AcompanhamentoDTO();
            acomp.setNumero(String.valueOf(requisicao.getNProcesso()));
            acomp.setAppDad("equiv");
            acomp.setPessoaId(pessoaId);
            acomp.setEntidadeNif(null);
            acomp.setTipo("PEDIDO_EQUIV");
            acomp.setTitulo("Pedido(s): " + titulos);
            acomp.setDescricao("Equivalência para " + titulos);
            acomp.setPercentagem(10);
            acomp.setDataInicio(LocalDateTime.now());
            acomp.setDataFim(null);
            acomp.setEtapaAtual("Aguardando Pagamento Taxa Analise");
            acomp.setEstado("EM_PROGRESSO");
            acomp.setEstadoDesc("Em Progresso");
            acomp.setDetalhes(detalhes);
            acomp.setEventos(eventos);
            acomp.setOutputs(new ArrayList<>());
            acomp.setAnexos(anexos);

            return acomp;
        } catch (Exception e) {
            log.error("Erro ao montar AcompanhamentoDTO para requisição: {}", requisicao.getId());
            throw new BusinessException("Erro ao montar acompanhamento");
        }
    }


    // upadte pedido e avancar etapa
    public List<EqvtPedidoDTO> updatePedidosByRequisicaoId(Integer requisicaoId, PortalPedidosDTO dto) {
        // Buscar requisição existente
        EqvTRequisicao requisicao = requisicaoRepository.findByNProcesso(requisicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));

        // Buscar pedidos existentes
        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

        // Atualizar dados da requisição se fornecido no DTO
        if (dto.getRequisicao() != null) {
            pedidoMapper.updateRequisicaoFromDTO(dto.getRequisicao(), requisicao);
            requisicao.setDataUpdate(LocalDate.now());
            requisicao = requisicaoRepository.save(requisicao);
            log.info("Requisição atualizada com ID: {}", requisicao.getId());
        }

        // Atualizar requerente somente se fornecido no DTO
        EqvTRequerente requerente = null;
        if (dto.getRequerente() != null) {
            requerente = pedidos.stream()
                    .findFirst()
                    .map(EqvTPedido::getRequerente)
                    .orElseThrow(() -> new BusinessException("Nenhum requerente encontrado para esta requisição"));

            log.info("Encontrado requerente existente com ID: {} para atualização", requerente.getId());

            pedidoMapper.updateRequerenteFromDTOConditional(dto.getRequerente(), requerente);
            requerente.setDataUpdate(LocalDate.now());
            requerente = requerenteRepository.save(requerente);

            log.info("Requerente ATUALIZADO com ID: {}", requerente.getId());
        }

        List<EqvtPedidoDTO> result = new ArrayList<>();
        List<EqvtPedidoDTO> pedidosDTO = dto.getPedidos();

        // Validar se o número de pedidos corresponde
        if (pedidos.size() != pedidosDTO.size()) {
            throw new BusinessException("Número de pedidos no DTO (" + pedidosDTO.size() +
                    ") não corresponde ao número existente (" + pedidos.size() + ")");
        }

        // Processar cada pedido
        for (int i = 0; i < pedidos.size(); i++) {
            EqvTPedido pedido = pedidos.get(i);
            EqvtPedidoDTO pedidoDTO = pedidosDTO.get(i);

            // Atualizar dados básicos do pedido
            pedidoMapper.updatePedidoFromDTOConditional(pedidoDTO, pedido);

            // Atualizar instituição de ensino se fornecida
            if (pedidoDTO.getInstEnsino() != null) {
                EqvTInstEnsino instituicao = processarInstituicaoEnsino(pedidoDTO.getInstEnsino());
                pedido.setInstEnsino(instituicao);
                pedido.setEtapa("Verificação Previa");
                log.info("Instituição de ensino atualizada para pedido {}: {}", pedido.getId(), instituicao.getNome());
            }

            // Associar requerente atualizado, se houver
            if (requerente != null) {
                pedido.setRequerente(requerente);
            }
            // Associar requisição
            pedido.setRequisicao(requisicao);

            // Salvar pedido atualizado
            pedido = pedidoRepository.save(pedido);
            log.info("Pedido atualizado com ID: {}", pedido.getId());

            // Atualizar documentos do pedido
            if (pedidoDTO.getDocumentos() != null && !pedidoDTO.getDocumentos().isEmpty()) {
                log.info("Atualizando {} documentos para o pedido {}", pedidoDTO.getDocumentos().size(), pedido.getId());
                salvarDocumentosDoPedido(pedidoDTO, pedido);
            } else {
                log.info("Nenhum documento para atualizar no pedido {}", pedido.getId());
            }

            result.add(pedidoMapper.toDTO(pedido));
        }

        // Avançar processo, se nProcesso estiver presente
        if (requisicao.getNProcesso() != null && !requisicao.getNProcesso().toString().trim().isEmpty()) {
            try {
                EqvTRequerente requerenteParaProcesso = requerente != null ?
                        requerente : pedidos.get(0).getRequerente();

                if (requerenteParaProcesso != null) {
                    String processInstanceId = avancarProcesso(
                            requerenteParaProcesso,
                            dto.getPedidos(),
                            requisicao.getNProcesso().toString()
                    );
                    log.info("Processo avançado com sucesso. Instance ID: {}", processInstanceId);
                }
            } catch (Exception e) {
                log.error("Erro ao avançar processo, mas pedidos foram atualizados. Erro: {}", e.getMessage());
                throw e;
            }
        }

        log.info("Atualização concluída para requisição ID: {} - {} pedidos atualizados",
                requisicaoId, result.size());
        return result;
    }


    public String avancarProcesso(EqvTRequerente requerente, List<EqvtPedidoDTO> pedidosDTO, String numProcesso) {
        try {
            List<EqvTPedido> pedidosTemporarios = new ArrayList<>();

            for (EqvtPedidoDTO dto : pedidosDTO) {
                EqvTPedido pedido = pedidoMapper.toEntity(dto);
                pedidosTemporarios.add(pedido);
            }

            return processService.avancarProcessoEquivalencia(requerente, pedidosTemporarios, numProcesso);
        } catch (Exception e) {
            log.error("Falha ao avançar processo de equivalência para processo: {}", numProcesso, e);
            throw new BusinessException("Não foi possível avançar o processo de equivalência");
        }
    }

    public void enviarEmailDuc(EqvTPagamento pagamento, EqvTRequerente requerente,
                               EqvTRequisicao requisicao, List<EqvTPedido> pedidos) {
        if (pedidos.isEmpty()) {
            log.warn("Nenhum pedido para enviar email DUC");
            return;
        }

        String nomeRequerente = requerente.getNome() != null ? requerente.getNome() : "";
        String numeroPedido = requisicao.getNProcesso() != null ? String.valueOf(requisicao.getNProcesso()) : "";

        String linkPagamento = mfkink + pagamento.getEntidade()
                + "&referencia=" + pagamento.getReferencia()
                + "&montante=" + pagamento.getTotal()
                + "&call_back_url=" + ducCheck + pagamento.getNuDuc();

        String linkVerDuc = reporterDuc + pagamento.getNuDuc();

        String linksHtml = "<a href=\"" + linkVerDuc + "\">Clique aqui para visualizar o DUC </a><br>" +
                "<a href=\"" + linkPagamento + "\">Clique aqui para pagar o DUC</a>";

        String emailRequerente = requerente.getEmail();

        TNotificacaoConfigEmail configEmail = notificationService.loadConfigNotification(
                "EQV_SOLICITACAO_DUC",
                "processo_equivalencia",
                "solitacao",
                "equiv"
        );

        if (configEmail == null) {
            throw new BusinessException("Configuração de email com o código [EQV_SOLICITACAO_DUC] não existe.");
        }

        String assunto = configEmail.getAssunto();
        String mensagem = configEmail.getMensagem()
                .replace("[NOME_REQUERENTE]", nomeRequerente)
                .replace("[NUMERO_PROCESSO]", numeroPedido)
                .replace("[LIMKS]", linksHtml);

        NotificationRequestDTO dto = new NotificationRequestDTO();

        dto.setAssunto(assunto);
        dto.setMensagem(mensagem);
        dto.setIdProcesso(numeroPedido);
        dto.setIdRelacao(requisicao.getNProcesso().toString());
        dto.setIsAlert("NAO");
        dto.setEmail(emailRequerente);

        notificationService.enviarEmail(dto);
    }

    public static String normalizeText(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[/\\\\]", "");
    }


    public ProcessoPedidosDocumentosDTO getPedidosComDocumentosPorProcesso(String numeroProcesso) {
        log.info("Buscando pedidos com documentos para processo: {}", numeroProcesso);

        try {
            // 1. Buscar a requisição pelo número de processo
            EqvTRequisicao requisicao = requisicaoRepository.findByNProcesso(Integer.valueOf(numeroProcesso))
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Processo não encontrado com número: " + numeroProcesso));

            // 2. Buscar os pedidos relacionados à requisição
            List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);


            if (pedidos.isEmpty()) {
                throw new EntityNotFoundException(
                        "Nenhum pedido encontrado para o processo: " + numeroProcesso);
            }

            // 3. Processar cada pedido com seus documentos
            List<ProcessoPedidosDocumentosDTO.PedidoDocumentosDTO> pedidosComDocumentos = pedidos.stream()
                    .map(this::processarPedidoComDocumentos)
                    .collect(Collectors.toList());

            // 4. Montar o DTO de resposta
            List<MotivoRetificacaoResponseDTO> motivosRetificacao =
                    motivoRetificacaoService.buscarMotivoRetificacaoPorProcesso(Integer.valueOf(numeroProcesso));
            EqvTRequerente r = pedidos.get(0).getRequerente();
            return ProcessoPedidosDocumentosDTO.builder()
                    .idRequerente(r != null ? r.getId() : null)
                    .nif(r != null && r.getNif() != null ? r.getNif() : null)
                    .nome(r != null && r.getNome() != null ? r.getNome() : null)
                    .docNumero(r != null && r.getDocNumero() != null ? r.getDocNumero() : null)
                    .dataNascimento(r != null && r.getDataNascimento() != null ? r.getDataNascimento() : null)
                    .nacionalidade(r != null && r.getNacionalidade() != null ? r.getNacionalidade() : null)
                    .sexo(r != null && r.getSexo() != null ? r.getSexo() : null)
                    .habilitacao(r != null && r.getHabilitacao() != null ? r.getHabilitacao().toString() : null)
                    .docIdentificacao(r != null && r.getDocIdentificacao() != null ? r.getDocIdentificacao() : null)
                    .dataEmissaoDoc(r != null && r.getDataEmissaoDoc() != null ? r.getDataEmissaoDoc() : null)
                    .dataValidadeDoc(r != null && r.getDataValidadeDoc() != null ? r.getDataValidadeDoc() : null)
                    .email(r != null && r.getEmail() != null ? r.getEmail() : null)
                    .contato(r != null && r.getContato() != null ? r.getContato() : null)
                    .userCreate(r != null ? r.getUserCreate() : null)
                    .userUpdate(r != null ? r.getUserUpdate() : null)
                    .dateCreate(r != null && r.getDateCreate() != null ? r.getDateCreate() : null)
                    .dataUpdate(r != null && r.getDataUpdate() != null ? r.getDataUpdate() : null)
                    .idPessoa(r != null ? r.getIdPessoa() : null)
                    .numeroProcesso(numeroProcesso)
                    .motivosRetificacao(motivosRetificacao)
                    .pedidos(pedidosComDocumentos)
                    .build();


        } catch (EntityNotFoundException e) {
            log.warn("Processo não encontrado: {}", numeroProcesso);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar pedidos com documentos para processo: {}", numeroProcesso, e);
            throw new BusinessException("Erro ao obter pedidos e documentos do processo");
        }
    }

    public ReclamacaoViewDTO getPedidoParaReclamacao(String numeroProcesso) {
        try {


            // 1. Buscar a requisição pelo número de processo
            EqvTRequisicao requisicao = requisicaoRepository
                    .findByNProcesso(Integer.valueOf(numeroProcesso))
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Processo não encontrado com número: " + numeroProcesso));

            // 2. Buscar os pedidos relacionados à requisição
            List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

            if (pedidos.isEmpty()) {
                throw new EntityNotFoundException(
                        "Nenhum pedido encontrado para o processo: " + numeroProcesso);
            }

            boolean podeAlterar = false;
            String mensagemEstado;

            try {
                podeAlterar = motivoRetificacaoService.verificarPedidosReclamado(
                        requisicao.getNProcesso().toString()
                );

                mensagemEstado = podeAlterar
                        ? "É possível efetuar a reclamação deste processo."
                        : "Este processo já foi reclamado e não pode ser alterado.";


            } catch (Exception e) {
                log.error("Erro ao verificar etapa solicit: {}", e.getMessage());
                mensagemEstado = "Não foi possível verificar o estado do processo";
            }

            // 3. Usar o primeiro pedido
            EqvTPedido pedido = pedidos.get(0);
            Optional<EqvtTDecisaoDespacho> optDespacho = despachoRepository.findByIdPedido(pedido);
            Integer decisaoDespacho = optDespacho.map(EqvtTDecisaoDespacho::getDecisao).orElse(1);
            LocalDate  data = optDespacho.map(EqvtTDecisaoDespacho::getDataCreate).orElse(null);


            return ReclamacaoViewDTO.builder()
                    .numeroProcesso(requisicao.getNProcesso())
                    .numeroApresentacao(pedido.getId())
                    .numeroApresentacao(pedido.getId())
                    .paisObtencao(
                            globalGeografiaService.buscarNomePorCodigoPais(
                                    pedido.getInstEnsino().getPais()
                            )
                    )
                    .instituicaoFtp(pedido.getInstEnsino().getNome())
                    .cargaHoraria(pedido.getCarga())
                    .despacho(Integer.valueOf(1).equals(decisaoDespacho) ? "Deferido" : "Indeferido")
                    .dataDespacho(data)
                    .podeAlterarSolic(podeAlterar)
                    .messagemEstado(mensagemEstado)
                    .build();

        } catch (EntityNotFoundException e) {
            log.warn("Processo não encontrado: {}", numeroProcesso);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar pedidos para o processo: {}", numeroProcesso, e);
            throw new BusinessException("Erro ao obter pedidos do processo");
        }
    }

    /**
         * Processa um pedido extraindo suas informações e documentos
         */
    private ProcessoPedidosDocumentosDTO.PedidoDocumentosDTO processarPedidoComDocumentos(EqvTPedido pedido) {

        boolean podeAlterar = false;
        String mensagemEstado = null;

        try {
            podeAlterar = motivoRetificacaoService.verificarPedidosEmAlterSolic(
                    pedido.getRequisicao().getNProcesso().toString()
            );

            mensagemEstado = podeAlterar
                    ? "Este processo pode ser alterado"
                    : "Este processo não pode ser alterado";

        } catch (Exception e) {
            log.error("Erro ao verificar etapa solicit: {}", e.getMessage());
            mensagemEstado = "Não foi possível verificar o estado do processo";
        }
        ProcessoPedidosDocumentosDTO.PedidoDocumentosDTO pedidoDTO = ProcessoPedidosDocumentosDTO.PedidoDocumentosDTO.builder()
                .id(pedido.getId())
                .formacaoProf(pedido.getFormacaoProf())
                .instituicaoEnsino(pedido.getInstEnsino().getId() != null ?
                        pedido.getInstEnsino().getId() : null)
                .instituicaoEnsinoNome(pedido.getInstEnsino().getNome())
                .paisInstituicao(pedido.getInstEnsino() != null ?
                        pedido.getInstEnsino().getPais() : null)
                .paisNome(globalGeografiaService.buscarNomePorCodigoPais(pedido.getInstEnsino().getPais()!=null ? pedido.getInstEnsino().getPais() : ""))
                .anoFim(pedido.getAnoFim())
                .anoInicio(pedido.getAnoInicio())
                .carga(pedido.getCarga())
                .podeAlterarSolic(podeAlterar)
                .messagemEstado(mensagemEstado)
                .documentos(new ArrayList<>()) // Inicializar lista vazia
                .build();

        try {
            // 2. Buscar documentos do pedido
            List<DocumentoResponseDTO> documentosResponse = documentService.getDocumentosPorRelacao(
                    pedido.getId(),
                    "SOLITACAO",
                    "equiv"
            );

            if (documentosResponse != null && !documentosResponse.isEmpty()) {
                // 3. Converter documentos para o formato específico
                List<ProcessoPedidosDocumentosDTO.DocumentoInfoDTO> documentosInfo = documentosResponse.stream()
                        .map(this::converterDocumentoParaInfoDTO)
                        .filter(Objects::nonNull) // Filtrar documentos nulos
                        .collect(Collectors.toList());

                pedidoDTO.setDocumentos(documentosInfo);
            }

        } catch (Exception e) {
            log.warn("Erro ao buscar documentos do pedido {}: {}", pedido.getId(), e.getMessage());
            // Continuar mesmo sem documentos
        }

        return pedidoDTO;
    }

    /**
     * Converte DocumentoResponseDTO para DocumentoInfoDTO
     */
    private ProcessoPedidosDocumentosDTO.DocumentoInfoDTO converterDocumentoParaInfoDTO(DocumentoResponseDTO docResponse) {
        try {
            // Criar DTO com todas as informações do documento
            return ProcessoPedidosDocumentosDTO.DocumentoInfoDTO.builder()
                    .id(docResponse.getId())
                    .fileName(docResponse.getFileName())
                    .path(docResponse.getPath())
                    .tipoRelacao(docResponse.getTipoRelacao())
                    .idRelacao(docResponse.getIdRelacao())
                    .idTpDoc(docResponse.getIdTpDoc())
                    .estado(docResponse.getEstado())
                    .appCode(docResponse.getAppCode())
                    .previewUrl(docResponse.getPreviewUrl())
                    .build();

        } catch (Exception e) {
            log.warn("Erro ao converter documento {}: {}", docResponse.getId(), e.getMessage());
            return null;
        }
    }


}