package dge.dge_equiv_api.application.reclamacao.service;

import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.service.DocumentService;
import dge.dge_equiv_api.application.pedidov01.dto.EqvtPedidoDTO;
import dge.dge_equiv_api.application.reclamacao.dto.EqvTReclamacaoDTO;
import dge.dge_equiv_api.application.reclamacao.dto.ReclamacaoBPMDTO;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTReclamacao;
import dge.dge_equiv_api.application.reclamacao.mapper.EqvTReclamacaoMapper;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTReclamacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dge.dge_equiv_api.domain.pedido.business.EqvTPedidoBusinessService.normalizeText;

@Slf4j
@Service
@RequiredArgsConstructor
public class EqvTReclamacaoService {

    private final EqvTReclamacaoRepository repository;
    private final EqvTReclamacaoMapper mapper;
    private final RestTemplate restTemplate;
    private final DocumentService documentService;
    private final EqvTPedidoRepository repositoryPedido;


    public List<EqvTReclamacao> findAll() {
        return repository.findAll();
    }

    public Optional<EqvTReclamacao> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<EqvTReclamacao> findByPedidoId(Long idPedido) {
        return repository.findByIdPedido_Id(idPedido);
    }

    @Transactional
    public EqvTReclamacao savePorNumeroProcesso(String nProcessoStr, EqvTReclamacaoDTO dto) {
        Integer nProcesso = Integer.valueOf(nProcessoStr);

        // Buscar pedido pelo número do processo
        List<EqvTPedido> pedidos = repositoryPedido.findByRequisicaoId(nProcesso);
        if (pedidos.isEmpty()) {
            throw new IllegalStateException("Nenhum pedido encontrado para o processo: " + nProcesso);
        }

        // Assumindo que sempre pegamos o primeiro pedido relacionado
        EqvTPedido pedido = pedidos.get(0);

        // Criar a reclamação associada ao pedido
        EqvTReclamacao entity = mapper.toEntity(dto);
        entity.setIdPedido(pedido);
        EqvTReclamacao saved = repository.save(entity);

        // Salvar documentos usando nProcesso
        salvarDocumentosDoPedido(dto, saved);

        // Enviar para API externa
        ReclamacaoBPMDTO bpmDTO = new ReclamacaoBPMDTO();
        enviarReclamacaoParaAPIExterna(nProcessoStr, bpmDTO, dto);

        return saved;
    }







    public String enviarReclamacaoParaAPIExterna(String nProcesso, ReclamacaoBPMDTO dadosBPM, EqvTReclamacaoDTO dadosReclamacao) {
        // URL da API externa
        String url = "http://localhost:8080/dge/services/feedback/reclamacao/?nProcesso=" + nProcesso;

        // Preparar o payload para a API externa
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
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Salvar também localmente se necessário
        if (response.getStatusCode().is2xxSuccessful()) {
            // Você pode salvar localmente também
            // save(dadosReclamacao);
        }

        return response.getBody();
    }
    private void salvarDocumentosDoPedido(EqvTReclamacaoDTO dto, EqvTReclamacao reclamacao) {
       DocumentoDTO docs = dto.getDocumentos();

        if (docs == null ) {
            log.warn("Nenhum documento recebido para o reclamacao {}", reclamacao.getId());
            return;
        }

        if (reclamacao.getIdPedido().getRequisicao() == null || reclamacao.getIdPedido().getRequisicao().getNProcesso() == null) {
            throw new IllegalStateException("Número de processo não disponível para salvar documentos");
        }

        String nProcesso = String.valueOf(reclamacao.getIdPedido().getRequisicao().getNProcesso());

       
            if (docs.getFile() == null) {
                log.warn("Documento {} sem arquivo", docs.getNome());
            }
            

            try {
                documentService.save(DocRelacaoDTO.builder()
                        .idRelacao(Math.toIntExact(reclamacao.getId()))
                        .tipoRelacao("RECLAMACAO")
                        .estado("A")
                        .appCode("equiv")
                        //.idTpDoc(String.valueOf(docs.getIdTpDoc()))
                        .fileName(normalizeText(docs.getNome()))
                        .file(docs.getFile())
                        .nProcesso(nProcesso)
                        .build());

                log.info("Documento {} salvo com sucesso para processo {}", docs.getNome(), nProcesso);
            } catch (Exception e) {
                log.error("Erro ao salvar documento {}", docs.getNome());
                throw new BusinessException("Erro ao salvar documento: " + docs.getNome());
            }
        }
    }
