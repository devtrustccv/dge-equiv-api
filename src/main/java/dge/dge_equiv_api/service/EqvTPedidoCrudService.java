package dge.dge_equiv_api.service;

import dge.dge_equiv_api.acompanhamento.dto.AcompanhamentoDTO;
import dge.dge_equiv_api.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.document.dto.DocumentoDTO;
import dge.dge_equiv_api.model.dto.*;
import dge.dge_equiv_api.model.entity.EqvTPedido;
import dge.dge_equiv_api.model.entity.EqvTRequerente;
import dge.dge_equiv_api.model.entity.EqvTInstEnsino;
import dge.dge_equiv_api.model.entity.EqvTRequisicao;
import dge.dge_equiv_api.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.repository.EqvTRequerenteRepository;
import dge.dge_equiv_api.repository.EqvTInstEnsinoRepository;
import dge.dge_equiv_api.repository.EqvTRequisicaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EqvTPedidoCrudService {

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

    private static final Logger log = LoggerFactory.getLogger(EqvTPedidoCrudService.class);


    // CREATE: cria sempre novos requerente e requisição, usa ou cria instituição ensino
//    public EqvtPedidoDTO createPedido(EqvtPedidoDTO dto) {
//
//        EqvTPedido pedido = new EqvTPedido();
//
//        copyPedidoFields(pedido, dto);
//
//        // Criar e salvar novo requerente
//        if (dto.getRequerente() != null) {
//            EqvTRequerente requerente = new EqvTRequerente();
//            copyRequerenteFields(requerente, dto.getRequerente());
//            requerente = requerenteRepository.save(requerente);
//            pedido.setRequerente(requerente);
//        }
//
//        // Inst. ensino: buscar por id ou criar novo
//        if (dto.getInstEnsino() != null) {
//            EqvTInstEnsino instEnsino = null;
//            if (dto.getInstEnsino().getId() != null) {
//                instEnsino = instEnsinoRepository.findById(dto.getInstEnsino().getId()).orElse(null);
//            }
//            if (instEnsino == null) {
//                instEnsino = new EqvTInstEnsino();
//                copyInstEnsinoFields(instEnsino, dto.getInstEnsino());
//                instEnsino = instEnsinoRepository.save(instEnsino);
//            }
//            pedido.setInstEnsino(instEnsino);
//        }
//
//        // Criar e salvar nova requisição
//        if (dto.getRequisicao() != null) {
//            EqvTRequisicao requisicao = new EqvTRequisicao();
//            copyRequisicaoFields(requisicao, dto.getRequisicao());
//            requisicao = requisicaoRepository.save(requisicao);
//            pedido.setRequisicao(requisicao);
//        }
//
////        AcompanhamentoDTO acompanhamento = montarAcompanhamentoDTO(dto, pedido);
////        acompanhamentoService.criarAcompanhamento(acompanhamento);
//
//       // log.info("Acompanhamento criado: {}", acompanhamento);
//
//
//
//        pedido = pedidoRepository.save(pedido);
//        salvarDocumentosDoPedido(dto, pedido);
//
//
//        return convertToDTO(pedido);
//
//
//    }


    public List<EqvtPedidoDTO> createLotePedidosComRequisicaoERequerenteUnicos(
            List<EqvtPedidoDTO> pedidosDTO,
            EqvTRequisicaoDTO requisicaoDTO,
            EqvTRequerenteDTO requerenteDTO) {

        // Cria e salva o requerente
        EqvTRequerente requerente = new EqvTRequerente();
        copyRequerenteFields(requerente, requerenteDTO);
        requerente = requerenteRepository.save(requerente);

        // Cria e salva a requisição
        EqvTRequisicao requisicao = new EqvTRequisicao();
        copyRequisicaoFields(requisicao, requisicaoDTO);
        requisicao = requisicaoRepository.save(requisicao);

        List<EqvtPedidoDTO> result = new ArrayList<>();

        for (EqvtPedidoDTO dto : pedidosDTO) {
            EqvTPedido pedido = new EqvTPedido();
            copyPedidoFields(pedido, dto);

            // Instituição de ensino
            if (dto.getInstEnsino() != null) {
                EqvTInstEnsino inst = null;
                if (dto.getInstEnsino().getId() != null) {
                    inst = instEnsinoRepository.findById(dto.getInstEnsino().getId()).orElse(null);
                }
                if (inst == null) {
                    inst = new EqvTInstEnsino();
                    copyInstEnsinoFields(inst, dto.getInstEnsino());
                    inst = instEnsinoRepository.save(inst);
                }
                pedido.setInstEnsino(inst);
            }

//            // Associa requerente e requisição comuns
            pedido.setRequerente(requerente);
            pedido.setRequisicao(requisicao);

            // Salva o pedido
            pedido = pedidoRepository.save(pedido);

            // SALVAR DOCUMENTOS (adicionado)
            salvarDocumentosDoPedido(dto, pedido);
            AcompanhamentoDTO acompanhamento = montarAcompanhamentoDTO(dto, pedido);
            acompanhamentoService.criarAcompanhamento(acompanhamento);
            log.info("Acompanhamento criado: {}", acompanhamento);

            result.add(convertToDTO(pedido));
        }

        return result;
    }



    // UPDATE: atualiza entidades relacionadas, cria se não existirem
    public List<EqvtPedidoDTO> updatePedidosByRequisicaoId(Integer requisicaoId, PortalPedidosDTO dto) {
        EqvTRequisicao requisicao = requisicaoRepository.findById(requisicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Requisição não encontrada com ID: " + requisicaoId));

        List<EqvTPedido> pedidos = pedidoRepository.findByRequisicao(requisicao);

        // Atualiza Requisição
        if (dto.getRequisicao() != null) {
            copyRequisicaoFields(requisicao, dto.getRequisicao());
            requisicaoRepository.save(requisicao);
        }

        // Atualiza ou cria Requerente
        EqvTRequerente requerente = null;
        if (dto.getRequerente() != null) {
            if (dto.getRequerente().getId() != null) {
                requerente = requerenteRepository.findById(dto.getRequerente().getId()).orElse(null);
            }
            if (requerente == null) {
                requerente = new EqvTRequerente();
            }
            copyRequerenteFields(requerente, dto.getRequerente());
            requerente = requerenteRepository.save(requerente);
        }

        // Atualiza cada pedido com dados individuais
        List<EqvtPedidoDTO> result = new ArrayList<>();

        List<EqvtPedidoDTO> pedidosDTO = dto.getPedidos();
        for (int i = 0; i < pedidos.size(); i++) {
            EqvTPedido pedido = pedidos.get(i);
            EqvtPedidoDTO dadosPedido = pedidosDTO.get(i); // dados atualizados desse pedido

            copyPedidoFields(pedido, dadosPedido);

            if (requerente != null)
                pedido.setRequerente(requerente);

            pedido.setRequisicao(requisicao);
            pedido = pedidoRepository.save(pedido);

            // ✅ Agora salva só os documentos desse pedido específico
            salvarDocumentosDoPedido(dadosPedido, pedido);

            result.add(convertToDTO(pedido));
        }


        return result;
    }



    // DELETE
    public void deletePedido(Integer id) {
        if (!pedidoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido não encontrado com ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    // LIST ALL
    public List<EqvtPedidoDTO> findAll() {
        return pedidoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // FIND BY ID
    public EqvtPedidoDTO findById(Integer id) {
        EqvTPedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        return convertToDTO(pedido);
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


    // gravar documento no minio

    private void salvarDocumentosDoPedido(EqvtPedidoDTO dto, EqvTPedido pedido) {
        List<DocumentoDTO> docs = dto.getDocumentos();

        if (docs == null) {
            log.warn("Nenhum documento recebido para o pedido {}", pedido.getId());
            return;
        }

        log.info("Salvando {} documentos para o pedido {}", docs.size(), pedido.getId());

        for (DocumentoDTO doc : docs) {
            if (doc.getFile() == null) {
                log.warn("Documento {} sem arquivo associado", doc.getNome());
                continue;
            }

            log.debug("Processando documento: {}", doc.getNome());

            DocRelacaoDTO docRelacao = DocRelacaoDTO.builder()
                    .idRelacao(pedido.getId())
                    .tipoRelacao("SOLICITACAO")
                    .estado("A")
                    .appCode("equiv")
                    .idTpDoc(""+doc.getIdTpDoc())
                    .fileName(doc.getNome())
                    .file(doc.getFile())
                    .build();

            try {
                documentService.save(docRelacao);
                log.info("Documento {} salvo com sucesso", doc.getNome());
            } catch (Exception e) {
                log.error("Erro ao salvar documento {}", doc.getNome(), e);
            }
        }
    }


    private AcompanhamentoDTO montarAcompanhamentoDTO(EqvtPedidoDTO dto, EqvTPedido pedido) {
        try {
            AcompanhamentoDTO acomp = new AcompanhamentoDTO();
            acomp.setNumero(String.valueOf(pedido.getRequisicao().getnProcesso()));
            acomp.setAppDad("EQUIV");
            acomp.setPessoaId(12);
            acomp.setEntidadeNif(null);
            acomp.setTipo("PEDIDO_RVCC");
            acomp.setTitulo(dto.getFormacaoProf());
            acomp.setDescricao(dto.getFormacaoProf());
            acomp.setEntidade(dto.getInstEnsino() != null ? dto.getInstEnsino().getNome() : null);
            acomp.setPercentagem(10);
            acomp.setDataInicio(LocalDateTime.now());
            acomp.setDataFim(LocalDateTime.now());
            acomp.setDataFimPrevisto(LocalDate.now().plusDays(14));
            acomp.setEtapaAtual("Solicitacao");
            acomp.setEstado("EM_PROGRESSO");
            acomp.setEstadoDesc("Em Progresso");
            acomp.setDetalhes(Map.of("Formação", dto.getFormacaoProf(), "Valor", "2345"));

//            AcompanhamentoDTO.Evento evento = new AcompanhamentoDTO.Evento("Etapa 1", "Iniciado", LocalDateTime.now());
//            acomp.setEventos(List.of(evento));
//
//            AcompanhamentoDTO.Comunicacao comunicacao = new AcompanhamentoDTO.Comunicacao("Notificação", "Faltam docs", LocalDateTime.now(),
//                    Map.of("Proximo_Passo", "Pagamento", "teste", "232323"));
//            acomp.setComunicacoes(List.of(comunicacao));
//
//            AcompanhamentoDTO.Output output = new AcompanhamentoDTO.Output("Certificado", "wwww");
//            acomp.setOutputs(List.of(output, output));

            return acomp;

        } catch (Exception e) {
            log.error("Erro ao montar AcompanhamentoDTO", e);
            return null; // ou lançar exceção, dependendo do seu fluxo
        }
    }



}
