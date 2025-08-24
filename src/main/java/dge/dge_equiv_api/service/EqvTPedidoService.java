package dge.dge_equiv_api.service;

import dge.dge_equiv_api.model.dto.*;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service


public class EqvTPedidoService {

    private final EqvTPedidoRepository pedidoRepository;
    private final TblDomainService tblDomainService;
    private final GlobalGeografiaService globalGeografiaService;
    private final DocRelacaoService docRelacaoService;


    public EqvTPedidoService(EqvTPedidoRepository pedidoService, TblDomainService tblDomainService,
                             GlobalGeografiaService globalGeografiaService, DocRelacaoService docRelacaoService, EqvTTipoDocumentoService eqvTTipoDocumentoService
                            ) {
        this.pedidoRepository = pedidoService;
        this.tblDomainService = tblDomainService;
        this.globalGeografiaService = globalGeografiaService;
        this.docRelacaoService = docRelacaoService;

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
        dto.setCarga(pedido.getCarga());
        dto.setNumDeclaracao(pedido.getNumDeclaracao());
        dto.setDataDespacho(pedido.getDataDespacho());

        // Requerente
        if (pedido.getRequerente() != null) {
            EqvTRequerenteDTO reqDto = new EqvTRequerenteDTO();

            reqDto.setId(pedido.getRequerente().getId());
            reqDto.setNome(pedido.getRequerente().getNome());
            reqDto.setDocNumero(pedido.getRequerente().getDocNumero());
            reqDto.setDataNascimento(pedido.getRequerente().getDataNascimento());
            reqDto.setEmail(pedido.getRequerente().getEmail());
            reqDto.setContato(pedido.getRequerente().getContato());

            // Sexo - pega a descrição
            String sexoCod = pedido.getRequerente().getSexo();
            if (sexoCod != null)
                reqDto.setSexo(tblDomainService.buscarDescricaoPorDominioEValor("SEXO", sexoCod));

            // Habilitação - pega a descrição
            Integer codHabilitacao = pedido.getRequerente().getHabilitacao();
            if (codHabilitacao != null)
                reqDto.setHabilitacao(tblDomainService.buscarDescricaoPorDominioEValor("HABILITAÇÃO", String.valueOf(codHabilitacao)));

            // Tipo de Documento - pega a descrição
            String tipoDocCod = pedido.getRequerente().getDocIdentificacao();
            if (tipoDocCod != null)
                reqDto.setDocIdentificacao(tblDomainService.buscarDescricaoPorDominioEValor("TIPO_DOCUMENTO_IDENT", tipoDocCod));

            // Nacionalidade - pega o nome do país
            String codPais = pedido.getRequerente().getNacionalidade();
            if (codPais != null)
                reqDto.setNacionalidade(globalGeografiaService.buscarNomePorCodigoPais(codPais));

            dto.setRequerente(reqDto);
        }


        // Instituição
        if (pedido.getInstEnsino() != null) {
            EqvTInstEnsinoDTO instDto = new EqvTInstEnsinoDTO();
            instDto.setId(pedido.getInstEnsino().getId());
            instDto.setNome(pedido.getInstEnsino().getNome());
            String getPais = globalGeografiaService.buscarNomePorCodigoPais(pedido.getInstEnsino().getPais());
            System.out.println(pedido.getInstEnsino());
            instDto.setPais(getPais);

            dto.setInstEnsino(instDto);
        }

        // Requisiçãoa
        if (pedido.getRequisicao() != null) {
            EqvTRequisicaoDTO reqqDto = new EqvTRequisicaoDTO();
            reqqDto.setId(pedido.getRequisicao().getId());
            reqqDto.setDataCreate(pedido.getRequisicao().getDataCreate());
            reqqDto.setnProcesso(pedido.getRequisicao().getnProcesso());
            dto.setRequisicao(reqqDto);
        }

        if (pedido.getDecisoesAp() != null && !pedido.getDecisoesAp().isEmpty()) {
            List<EqvtTDecisaoApDTO> decisoes = pedido.getDecisoesAp().stream().map(dec -> {
                EqvtTDecisaoApDTO d = new EqvtTDecisaoApDTO();
                d.setNivel(dec.getNivel());
                d.setFamilia(dec.getFamilia());
                d.setDecisao(dec.getDecisao());
                d.setParecerCnep(dec.getParecerCnep());
                d.setObs(dec.getObs());
                return d;
            }).toList();
            dto.setDecisoesAp(decisoes);
        }
        if (pedido.getDecisoesVp() != null && !pedido.getDecisoesVp().isEmpty()) {
            List<EqvtTDecisaoVpDTO> decisoes = pedido.getDecisoesVp().stream().map(dec -> {
                EqvtTDecisaoVpDTO d = new EqvtTDecisaoVpDTO();
                d.setNivel(dec.getNivel());
                d.setFamilia(dec.getFamilia());
                d.setDecisao(dec.getDecisao());
                d.setObsVp(dec.getObsVp());
                return d;
            }).toList();
            dto.setDecisoesVp(decisoes);
        }

        List<DocRelacaoDTO> documentos = docRelacaoService.buscarDocsComNomeTipoPorIdRelacao(pedido.getId());
        dto.setDocumentos(documentos);


        return dto;
    }

}
