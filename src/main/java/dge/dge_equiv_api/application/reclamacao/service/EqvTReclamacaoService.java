package dge.dge_equiv_api.application.reclamacao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.application.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.service.DocumentService;
import dge.dge_equiv_api.application.duc.service.PagamentoService;
import dge.dge_equiv_api.application.notification.dto.NotificationRequestDTO;
import dge.dge_equiv_api.application.notification.service.NotificationService;
import dge.dge_equiv_api.application.pedidov01.dto.EqvTRequerenteDTO;
import dge.dge_equiv_api.application.pedidov01.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.application.reclamacao.dto.EqvTReclamacaoDTO;
import dge.dge_equiv_api.application.reclamacao.dto.ReclamacaoBPMDTO;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.*;
import dge.dge_equiv_api.application.reclamacao.mapper.EqvTReclamacaoMapper;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTReclamacaoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvtTDecisaoDespachoRepository;
import dge.dge_equiv_api.infrastructure.quaternary.TNotificacaoConfigEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EqvTReclamacaoService {

    private final EqvTReclamacaoRepository repository;
    private final EqvTReclamacaoMapper mapper;
    private final RestTemplate restTemplate;
    private final DocumentService documentService;
    private final EqvTPedidoRepository repositoryPedido;
    private  final EqvtTDecisaoDespachoRepository despachoRepository;
    private final NotificationService notificationService;
    private final PagamentoService pagamentoService;
    private final ObjectMapper objectMapper;
    private  final AcompanhamentoService acompanhamentoService;
    @Value("${link.mf.duc}")
    private String mfkink;

    @Value("${link.api.duc.check}")
    private String ducCheck;

    @Value("${link.report.integration.sigof.duc}")
    private String reporterDuc;

    @Value("${process.equiv-reclamacao}")
    private String url;


    public Optional<EqvTReclamacao> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public EqvTReclamacao savePorNumeroProcesso(String nProcessoStr, EqvTReclamacaoDTO dto) {
        Integer nProcesso = Integer.valueOf(nProcessoStr);

        // Buscar pedido pelo número do processo
        List<EqvTPedido> pedidos = repositoryPedido.findByRequisicaoNProcesso(nProcesso);
        if (pedidos.isEmpty()) {
            throw new IllegalStateException("Nenhum pedido encontrado para o processo: " + nProcesso);
        }

        // Assumindo que sempre pegamos o primeiro pedido relacionado
        EqvTPedido pedido = pedidos.get(0);

        Optional<EqvTReclamacao> optReclamacao =
                repository.findByIdPedido(pedido);

        EqvTReclamacao entity;

        if (optReclamacao.isPresent()) {
            // UPDATE VÁLIDO
            entity = optReclamacao.get();
            entity.setObservacao(dto.getObservacao());
            entity.setAnexo(dto.getAnexo());
        } else {
            // CREATE
            entity = mapper.toEntity(dto);
            entity.setIdPedido(pedido);
            entity.setIdRequisicao(pedido.getRequisicao());

        }

        EqvTReclamacao saved = repository.save(entity);


        salvarDocumentosDoPedido(dto, saved);

        Optional<EqvtTDecisaoDespacho> optDespacho = despachoRepository.findByIdPedido(pedido);
        Integer decisaoDespacho = optDespacho.map(EqvtTDecisaoDespacho::getDecisao).orElse(1);

        ReclamacaoBPMDTO bpmDTO = new ReclamacaoBPMDTO();

        bpmDTO.setReclamar(String.valueOf(saved.getDecisao()));
        bpmDTO.setObs(dto.getObservacao() != null ? dto.getObservacao() : "");
        bpmDTO.setObs_fk(dto.getObservacao() != null ? dto.getObservacao() : "");


        if (decisaoDespacho == 2 && "2".equals(String.valueOf(saved.getDecisao()))) {
            bpmDTO.setReclamacao_1("INDEF");
            bpmDTO.setReclamacao_1__fk("INDEF");
            bpmDTO.setReclamacao_1__fk_desc("Indeferido");
            bpmDTO.setReclamacao_("");
            bpmDTO.setReclamacao__fk("");
            bpmDTO.setReclamacao__fk_desc("");
        } else if ((decisaoDespacho == 2 || decisaoDespacho == 1) && "1".equals(String.valueOf(saved.getDecisao()))) {
            bpmDTO.setReclamacao_("RETIF");
            bpmDTO.setReclamacao__fk("RETIF");
            bpmDTO.setReclamacao__fk_desc("Retificado");
            bpmDTO.setReclamacao_1("");
            bpmDTO.setReclamacao_1__fk("");
            bpmDTO.setReclamacao_1__fk_desc("");
        } else {

            bpmDTO.setReclamacao_("");
            bpmDTO.setReclamacao__fk("");
            bpmDTO.setReclamacao__fk_desc("");
            bpmDTO.setReclamacao_1("");
            bpmDTO.setReclamacao_1__fk("");
            bpmDTO.setReclamacao_1__fk_desc("");
        }

        if (decisaoDespacho == 1 && "2".equals(String.valueOf(saved.getDecisao()))) {
            EqvTPagamento pagamento =   gerarDUC(pedido.getRequerente().getNif(), pedido.getRequisicao().getNProcesso());
            enviarEmailDuc(pagamento, saved);

            String linkPagamento = mfkink + pagamento.getEntidade()
                    + "&referencia=" + pagamento.getReferencia()
                    + "&montante=" + pagamento.getTotal()
                    + "&call_back_url=" + ducCheck + pagamento.getNuDuc();

            Map<String, String> detalhes = Map.of(
                    "Referencia", pagamento.getReferencia().toString(),
                    "Entidade", pagamento.getEntidade(),
                    "valor", pagamento.getTotal().toString(),
                    "Pagar Online", linkPagamento,
                    "Ver duc", reporterDuc + pagamento.getNuDuc()
            );
            atualizarAcompanhamentoReficado(
                    pedido.getRequisicao().getNProcesso().toString(),
                    "equiv",
                    pedido.getRequisicao().getIdPessoa(),
                    "PEDIDO_EQUIV",
                    "Pagamento Certificado",
                    85,
                    "EM_PROGRESSO",
                    "Em processo",
                    "Periodo Reclamação Concluido",
                    "Processo aguardando pagamento do certificado",
                    detalhes, null, null,null,null
            );
        }
        if (decisaoDespacho == 2 && "2".equals(String.valueOf(saved.getDecisao()))) {
            atualizarAcompanhamentoReficado(
            pedido.getRequisicao().getNProcesso().toString(),
                    "equiv",
                    pedido.getRequisicao().getIdPessoa(),
                    "PEDIDO_EQUIV",
                    "",
                    100,
                    "FINALIZADO",
                    "Finalizado",
                    "Concluído",
                    "Informamos que o seu processo de equivalência, registado sob o número "
                            + pedido.getRequisicao().getNProcesso() + ", encontra-se finalizado.",
                    null,null,null,null,null
                );

        }

        bpmDTO.setDespacho(String.valueOf(decisaoDespacho != null ? decisaoDespacho : 1));
        bpmDTO.setDespacho__fk(String.valueOf(decisaoDespacho != null ? decisaoDespacho : 1));
        bpmDTO.setDespacho__fk_desc((decisaoDespacho != null && decisaoDespacho == 2) ? "Indeferido" : "Deferido");

        enviarReclamacaoParaAPIExterna(String.valueOf(pedido.getRequisicao().getNProcesso()), bpmDTO, dto);


        return saved;
    }

    public String enviarReclamacaoParaAPIExterna(String nProcesso, ReclamacaoBPMDTO dadosBPM, EqvTReclamacaoDTO dadosReclamacao) {


        Map<String, Object> payload = new HashMap<>();

        // Dados para BPM
        payload.put("reclamar", dadosBPM.getReclamar());
        payload.put("obs", dadosBPM.getObs());
        payload.put("reclamacao_", dadosBPM.getReclamacao_());
        payload.put("reclamacao__fk", dadosBPM.getReclamacao__fk());
        payload.put("reclamacao__fk_desc", dadosBPM.getReclamacao__fk_desc());
        payload.put("obs_fk", dadosBPM.getObs_fk());
        payload.put("despacho", dadosBPM.getDespacho());
        payload.put("despacho__fk", dadosBPM.getDespacho__fk());
        payload.put("despacho__fk_desc", dadosBPM.getDespacho__fk_desc());
        payload.put("reclamacao_1", dadosBPM.getReclamacao_1());
        payload.put("reclamacao_1__fk", dadosBPM.getReclamacao_1__fk());
        payload.put("reclamacao_1__fk_desc", dadosBPM.getReclamacao_1__fk_desc());

        // Dados para tabela de reclamações
        payload.put("decisao", dadosReclamacao.getDecisao());
        payload.put("anexo", dadosReclamacao.getAnexo());
        payload.put("userCreate", dadosReclamacao.getUserCreate());
        payload.put("userUpdate", dadosReclamacao.getUserUpdate());

        // Configurar headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        // Fazer a chamada
        ResponseEntity<String> response = restTemplate.exchange(
                url+"/"+nProcesso,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Salvar também localmente se necessário
        if (response.getStatusCode().is2xxSuccessful()) {
            // Você pode salvar localmente também
            // save(dadosReclamacao);
        }
        log.info("Resposta da API externa: {}", payload);

        return response.getBody();
    }
    private void salvarDocumentosDoPedido(EqvTReclamacaoDTO dto, EqvTReclamacao reclamacao) {
        DocumentoDTO docs = dto.getDocumentos();

        if (docs == null || docs.getFile() == null) {
            log.warn("Nenhum documento recebido para o pedido {}", reclamacao.getId());
            return;
        }



        // Definir nProcesso com base no pedido, nunca usar o que vem do DTO
        String nProcesso = String.valueOf(reclamacao.getIdPedido().getRequisicao().getNProcesso());

        try {
            documentService.saveReclamcao(DocRelacaoDTO.builder()
                    .idRelacao(Math.toIntExact(reclamacao.getId()))
                    .tipoRelacao("RECLAMACAO")
                    .estado("A")
                    .appCode("equiv")
                    .fileName("Ata")
                    .file(docs.getFile())
                    .nProcesso(nProcesso)
                    .build());

            log.info("Documento {} salvo com sucesso para processo {}", nProcesso);
        } catch (Exception e) {
            log.error("Erro ao salvar documento {}",  e);
            throw new BusinessException("Erro ao salvar documento: ");
        }
    }


    public void enviarEmailDuc(EqvTPagamento pagamento, EqvTReclamacao reclamacao) {
        if (reclamacao.getIdPedido()==null) {
            log.warn("Nenhum pedido para enviar email DUC");
            return;
        }

        String nomeRequerente = reclamacao.getIdPedido().getRequerente().getNome() != null ? reclamacao.getIdPedido().getRequerente().getNome() : "";
        String numeroPedido = reclamacao.getIdPedido().getRequisicao().getNProcesso() != null ? String.valueOf(reclamacao.getIdPedido().getRequisicao().getNProcesso()) : "";

        String linkPagamento = mfkink + pagamento.getEntidade()
                + "&referencia=" + pagamento.getReferencia()
                + "&montante=" + pagamento.getTotal()
                + "&call_back_url=" + ducCheck + pagamento.getNuDuc();

        String linkVerDuc = reporterDuc + pagamento.getNuDuc();

        String linksHtml = "<a href=\"" + linkVerDuc + "\">Clique aqui para visualizar o DUC </a><br>" +
                "<a href=\"" + linkPagamento + "\">Clique aqui para pagar o DUC</a>";

        String emailRequerente = reclamacao.getIdPedido().getRequerente().getEmail();

        TNotificacaoConfigEmail configEmail = notificationService.loadConfigNotification(
                "EQV_DESPACHO_D",
                "processo_equivalencia",
                  "reclamacao",
                "equiv"
        );

        if (configEmail == null) {
            throw new BusinessException("Configuração de email com o código [EQV_SOLICITACAO_DUC] não existe.");
        }

        String assunto = configEmail.getAssunto();
        String decoded = StringEscapeUtils.unescapeHtml4(configEmail.getMensagem());
        String mensagem = decoded
                .replace("[NOME_REQUERENTE]", nomeRequerente)
                .replace("[LINK_PAGAMENTO]", linksHtml);

        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setAppCode("equiv");
        dto.setAssunto(assunto);
        dto.setMensagem(mensagem);
        dto.setIdProcesso("");
        dto.setTipoProcesso("processo_equivalencia");
        dto.setIdRelacao("");
        dto.setTipoRelacao("");
        dto.setEmail(emailRequerente);

        notificationService.enviarEmail(dto);
    }

    public EqvTPagamento gerarDUC(Integer nif, Integer requisicao) {
        try {
            return pagamentoService.gerarDucRec(null, nif.toString(),
                    requisicao, null);
        } catch (Exception e) {
            log.error("Erro ao gerar DUC");
            throw new BusinessException("Erro ao gerar o DUC.");
        }
    }
    public void atualizarAcompanhamentoReficado(
            String numero,
            String appDad,
            Integer pessoaId,
            String tipo,
            String etapaAtual,
            int percentagem,
            String estado,
            String estadoDesc,
            String tituloEvento,
            String descricaoEventos,
            Map<String, String> detalhesEventos,
            String tituloComunicacao,
            String descricaoComunicacao,
            Map<String, String> detalhesComunicacoes,
            List<AcompanhamentoDTO.UrlItem> urlsComunicacao
    ) {
        try {

            log.info(" [REFICADO] Preparando DTO para atualização de acompanhamento...");

            // =============================================================
            // MONTAR DTO PRINCIPAL
            // =============================================================
            AcompanhamentoDTO dto = new AcompanhamentoDTO();
            dto.setNumero(numero);
            dto.setAppDad(appDad);
            dto.setPessoaId(pessoaId);
            dto.setTipo(tipo);
            dto.setEstado(estado);
            dto.setEstadoDesc(estadoDesc);
            dto.setEtapaAtual(etapaAtual);
            dto.setPercentagem(percentagem);

            // =============================================================
            // EVENTOS
            // =============================================================
            AcompanhamentoDTO.Evento evento = new AcompanhamentoDTO.Evento(
                    tituloEvento,
                    descricaoEventos,
                    LocalDateTime.now(),
                    detalhesEventos
            );

            dto.setEventos(List.of(evento));

            // =============================================================
            // COMUNICAÇÕES
            // =============================================================
            AcompanhamentoDTO.Comunicacao comunicacao = new AcompanhamentoDTO.Comunicacao();
            comunicacao.setTitulo(tituloComunicacao);
            comunicacao.setDescricao(descricaoComunicacao);
            comunicacao.setDatetime(LocalDateTime.now());
            comunicacao.setItems(detalhesComunicacoes);

            if (urlsComunicacao != null && !urlsComunicacao.isEmpty()) {
                comunicacao.setUrls(urlsComunicacao);
            }

            dto.setComunicacoes(List.of(comunicacao));

            // =============================================================
            // LOG PARA DEBUG
            // =============================================================
            try {
                String json = objectMapper.writeValueAsString(dto);
                log.info(" [REFICADO] JSON gerado para atualização: {}", json);
            } catch (Exception ex) {
                log.warn(" [REFICADO] Não foi possível serializar DTO para debug.");
            }

            // =============================================================
            // ENVIO PARA API EXTERNA
            // =============================================================
            log.info(" [REFICADO] Enviando atualização para API externa...");
            acompanhamentoService.criarAcompanhamentoUpadate(dto);

            log.info(" [REFICADO] Acompanhamento atualizado com sucesso.");

        } catch (Exception e) {
            log.error(" [REFICADO] Erro na atualização de acompanhamento reificado: {}", e.getMessage(), e);
        }
    }


}
