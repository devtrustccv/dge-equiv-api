package dge.dge_equiv_api.service;

import dge.dge_equiv_api.Utils.Criptolink;
import dge.dge_equiv_api.model.dto.*;
import dge.dge_equiv_api.model.entity.EqvTPedido;
import dge.dge_equiv_api.repository.EqvTPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service


public class EqvTPedidoService {

    private final EqvTPedidoRepository pedidoRepository;
    private final TblDomainService tblDomainService;
    private final GlobalGeografiaService globalGeografiaService;
    private  final NivelQualificacaoService nivelQualificacaoService;
    private  final FamiliaProfissionalService familiaProfissionalService;

    public EqvTPedidoService(EqvTPedidoRepository pedidoService, TblDomainService tblDomainService,
                             GlobalGeografiaService globalGeografiaService,
                             NivelQualificacaoService nivelQualificacaoService,
                             FamiliaProfissionalService familiaProfissionalService) {
        this.pedidoRepository = pedidoService;
        this.tblDomainService = tblDomainService;
        this.globalGeografiaService = globalGeografiaService;
        this.nivelQualificacaoService = nivelQualificacaoService;
        this.familiaProfissionalService = familiaProfissionalService;
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

        return dto;
    }

}
