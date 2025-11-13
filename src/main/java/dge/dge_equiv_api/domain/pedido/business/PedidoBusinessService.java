package dge.dge_equiv_api.domain.pedido.business;

import dge.dge_equiv_api.application.acompanhamento.dto.AcompanhamentoDTO;

import dge.dge_equiv_api.application.acompanhamento.service.AcompanhamentoService;
import dge.dge_equiv_api.application.process.service.ProcessService;
import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.infrastructure.primary.EqvTInstEnsino;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequerente;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoBusinessService {

    private final ProcessService processService;
    private final AcompanhamentoService acompanhamentoService;

    public String iniciarProcessoEquivalencia(EqvTRequerente requerente,
                                              List<EqvTPedido> pedidos,
                                              Map<Integer, EqvTInstEnsino> instituicoes) {
        try {
            return processService.iniciarProcessoEquivalencia(requerente, pedidos);
        } catch (Exception e) {
            log.error("Falha ao iniciar processo de equivalência", e);
            throw new BusinessException("Não foi possível iniciar o processo de equivalência");
        }
    }

    public void criarAcompanhamentoProcesso(EqvTRequisicao requisicao,
                                            List<EqvTPedido> pedidos,
                                            Integer pessoaId) {
        try {
            AcompanhamentoDTO acompanhamento = montarAcompanhamentoDTO(requisicao, pedidos, pessoaId);
            if (acompanhamento != null) {
                acompanhamentoService.criarAcompanhamento(acompanhamento);
                log.info("Acompanhamento criado para processo {}", requisicao.getNProcesso());
            }
        } catch (Exception e) {
            log.error("Falha ao criar acompanhamento", e);
            throw new BusinessException("Erro ao criar acompanhamento do processo");
        }
    }

    private AcompanhamentoDTO montarAcompanhamentoDTO(EqvTRequisicao requisicao,
                                                      List<EqvTPedido> pedidos,
                                                      Integer pessoaId) {
        try {
            AcompanhamentoDTO acomp = new AcompanhamentoDTO();
            acomp.setNumero(String.valueOf(requisicao.getNProcesso()));
            acomp.setAppDad("EQUIV");
            acomp.setPessoaId(pessoaId);
            acomp.setEntidadeNif(null);
            acomp.setTipo("PEDIDO EQUIV");

            String titulos = pedidos.stream()
                    .map(EqvTPedido::getFormacaoProf)
                    .filter(Objects::nonNull)
                    .collect(java.util.stream.Collectors.joining(" | "));
            acomp.setTitulo("Pedido(s): " + titulos);
            acomp.setDescricao("Equivalência para " + titulos);

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

            Map<String, String> detalhes = new LinkedHashMap<>();
            for (EqvTPedido p : pedidos) {
                detalhes.put("Formação Profissional " + p.getId(), p.getFormacaoProf());
            }
            acomp.setDetalhes(detalhes);

//            acomp.setEventos(List.of(
//                    new AcompanhamentoDTO.Evento("Etapa 1", "Iniciado", LocalDateTime.now())
//            ));
            acomp.setComunicacoes(List.of(
                    new AcompanhamentoDTO.Comunicacao("Notificação", "", LocalDateTime.now(),
                            Map.of("Proximo_Passo", "Análise"))
            ));
            acomp.setOutputs(List.of());

            return acomp;
        } catch (Exception e) {
            log.error("Erro ao montar AcompanhamentoDTO", e);
            return null;
        }
    }
}