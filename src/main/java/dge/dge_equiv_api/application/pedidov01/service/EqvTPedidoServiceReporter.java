package dge.dge_equiv_api.application.pedidov01.service;

import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import dge.dge_equiv_api.application.logs.dto.ParecerCnepHistoricoDTO;
import dge.dge_equiv_api.application.pedidov01.dto.*;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.application.document.service.DocRelacaoService;
import dge.dge_equiv_api.application.geografia.service.GlobalGeografiaService;
import dge.dge_equiv_api.application.domain.service.TblDomainService;
import dge.dge_equiv_api.application.logs.service.LogService;
import dge.dge_equiv_api.application.logs.dto.LogDTO;
import dge.dge_equiv_api.application.logs.dto.LogItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EqvTPedidoServiceReporter {

    private final EqvTPedidoRepository pedidoRepository;
    private final TblDomainService tblDomainService;
    private final GlobalGeografiaService globalGeografiaService;
    private final DocRelacaoService docRelacaoService;
    private final LogService logService;

    public EqvTPedidoServiceReporter(EqvTPedidoRepository pedidoService,
                                     TblDomainService tblDomainService,
                                     GlobalGeografiaService globalGeografiaService,
                                     DocRelacaoService docRelacaoService,
                                     LogService logService) {
        this.pedidoRepository = pedidoService;
        this.tblDomainService = tblDomainService;
        this.globalGeografiaService = globalGeografiaService;
        this.docRelacaoService = docRelacaoService;
        this.logService = logService;
    }

    public EqvtPedidoReporteDTO getPedidoDTOById(Integer id) {
        Optional<EqvTPedido> pedidoOpt = pedidoRepository.findById(id);
        return pedidoOpt.map(this::toDto).orElse(null);
    }

    public EqvtPedidoReporteDTO toDto(EqvTPedido pedido) {
        EqvtPedidoReporteDTO dto = new EqvtPedidoReporteDTO();
        dto.setId(pedido.getId());
        dto.setFormacaoProf(pedido.getFormacaoProf());
        dto.setCarga(pedido.getCarga());
        dto.setAnoInicio(pedido.getAnoInicio());
        dto.setAnoFim(pedido.getAnoFim());
        dto.setNivel(pedido.getNivel());
        dto.setFamilia(pedido.getFamilia());
        String despacho = tblDomainService.buscarDescricaoPorDominioEValor("DESPACHO", String.valueOf(pedido.getDespacho()));
        dto.setDespacho(despacho);
        dto.setNumDeclaracao(pedido.getNumDeclaracao());
        dto.setDataDespacho(pedido.getDataDespacho());

        // Requerente (mantido igual)
        if (pedido.getRequerente() != null) {
            EqvTRequerenteDTO reqDto = new EqvTRequerenteDTO();

            reqDto.setId(pedido.getRequerente().getId());
            reqDto.setNome(pedido.getRequerente().getNome());
            reqDto.setDocNumero(pedido.getRequerente().getDocNumero());
            reqDto.setDataNascimento(pedido.getRequerente().getDataNascimento());
            reqDto.setEmail(pedido.getRequerente().getEmail());
            reqDto.setContato(pedido.getRequerente().getContato());

            String sexoCod = pedido.getRequerente().getSexo();
            if (sexoCod != null)
                reqDto.setSexo(tblDomainService.buscarDescricaoPorDominioEValor("SEXO", sexoCod));

            Integer codHabilitacao = pedido.getRequerente().getHabilitacao();
            if (codHabilitacao != null)
                reqDto.setHabilitacao(tblDomainService.buscarDescricaoPorDominioEValor("HABILITAÇÃO", String.valueOf(codHabilitacao)));

            String tipoDocCod = pedido.getRequerente().getDocIdentificacao();
            if (tipoDocCod != null)
                reqDto.setDocIdentificacao(tblDomainService.buscarDescricaoPorDominioEValor("TIPO_DOCUMENTO_IDENT", tipoDocCod));

            String codPais = pedido.getRequerente().getNacionalidade();
            if (codPais != null)
                reqDto.setNacionalidade(globalGeografiaService.buscarNomePorCodigoPais(codPais));

            dto.setRequerente(reqDto);
        }

        // Instituição (mantido igual)
        if (pedido.getInstEnsino() != null) {
            EqvTInstEnsinoDTO instDto = new EqvTInstEnsinoDTO();
            instDto.setId(pedido.getInstEnsino().getId());
            instDto.setNome(pedido.getInstEnsino().getNome());
            String getPais = globalGeografiaService.buscarNomePorCodigoPais(pedido.getInstEnsino().getPais());
            instDto.setPais(getPais);
            dto.setInstEnsino(instDto);
        }

        // Requisição (mantido igual)
        if (pedido.getRequisicao() != null) {
            EqvTRequisicaoDTO reqqDto = new EqvTRequisicaoDTO();
            reqqDto.setId(pedido.getRequisicao().getId());
            reqqDto.setDataCreate(pedido.getRequisicao().getDataCreate());
            reqqDto.setNProcesso(pedido.getRequisicao().getNProcesso());
            dto.setRequisicao(reqqDto);
        }

        // DECISÕES AP - COM HISTÓRICO DO PARECER CNEP APENAS
        // Na parte das decisões AP no EqvTPedidoServiceReporter:
        if (pedido.getDecisoesAp() != null && !pedido.getDecisoesAp().isEmpty()) {
            List<EqvtTDecisaoApDTO> decisoes = pedido.getDecisoesAp().stream().map(dec -> {
                EqvtTDecisaoApDTO d = new EqvtTDecisaoApDTO();
                d.setNivel(dec.getNivel());
                d.setFamilia(dec.getFamilia());
                d.setDecisao(dec.getDecisao());
                d.setParecerCnep(dec.getParecerCnep());
                d.setObs(dec.getObs());

                // BUSCAR HISTÓRICO DO PARECER CNEP
                if (dec.getId() != null) {
                    List<ParecerCnepHistoricoDTO> historico = logService.getHistoricoParecerCnep(dec.getId());

                    // Se houver histórico, adicionar
                    if (!historico.isEmpty()) {
                        d.setHistoricoParecer(historico);
                    }
                }

                return d;
            }).collect(Collectors.toList());

            dto.setDecisoesAp(decisoes);
        }

        // Decisões VP (sem logs por enquanto - mantido igual)
        if (pedido.getDecisoesVp() != null && !pedido.getDecisoesVp().isEmpty()) {
            List<EqvtTDecisaoVpDTO> decisoes = pedido.getDecisoesVp().stream().map(dec -> {
                EqvtTDecisaoVpDTO d = new EqvtTDecisaoVpDTO();
                d.setNivel(dec.getNivel());
                d.setFamilia(dec.getFamilia());
                d.setDecisao(dec.getDecisao());
                d.setObsVp(dec.getObsVp());
                return d;
            }).collect(Collectors.toList());

            dto.setDecisoesVp(decisoes);
        }

        // Documentos (mantido igual)
        List<DocRelacaoDTO> documentos = docRelacaoService.buscarDocsComNomeTipoPorIdRelacao(pedido.getId());
        dto.setDocumentos(documentos);

        return dto;
    }
}