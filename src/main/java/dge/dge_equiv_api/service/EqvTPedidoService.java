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

    public EqvtPedidoDTO getPedidoDTOById(Integer id) {
        Optional<EqvTPedido> pedidoOpt = pedidoRepository.findById(id);
        return pedidoOpt.map(this::toDto).orElse(null);
    }


    public EqvtPedidoDTO toDto(EqvTPedido pedido) {
        EqvtPedidoDTO dto = new EqvtPedidoDTO();
        dto.setId(pedido.getId());
        dto.setFormacaoProf(pedido.getFormacaoProf());
        dto.setCarga(pedido.getCarga());
        dto.setAnoInicio(pedido.getAnoInicio());
        dto.setAnoFim(pedido.getAnoFim());
        dto.setNivel(pedido.getNivel());
        dto.setFamilia(pedido.getFamilia());
        String despacho = tblDomainService.buscarDescricaoPorDominioEValor("DESPACHO", pedido.getDespacho());
        dto.setDespacho(despacho);
        dto.setNumDeclaracao(pedido.getNumDeclaracao());
        dto.setDataDespacho(pedido.getDataDespacho());

        // Requerente
        if (pedido.getRequerente() != null) {
            EqvTRequerenteDTO reqDto = new EqvTRequerenteDTO();
            reqDto.setId(pedido.getRequerente().getId());
            reqDto.setNome(pedido.getRequerente().getNome());
            reqDto.setDocNumero(pedido.getRequerente().getDocNumero());
            reqDto.setDocIdentificacao(pedido.getRequerente().getDocIdentificacao());
            reqDto.setDataNascimento(pedido.getRequerente().getDataNascimento());
            String descricao = tblDomainService.buscarDescricaoPorDominioEValor("SEXO", pedido.getRequerente().getSexo());
            reqDto.setSexo(descricao);
            String descricaoHab = tblDomainService.buscarDescricaoPorDominioEValor("HABILITAÇÃO", pedido.getRequerente().getHabilitacao());
            reqDto.setHabilitacao(descricaoHab);
            String descricaoDoc = tblDomainService.buscarDescricaoPorDominioEValor("TIPO_DOCUMENTO_IDENT", pedido.getRequerente().getDocIdentificacao());
            reqDto.setDocIdentificacao(descricaoDoc);
            String getNacionalidade = globalGeografiaService.buscarNomePorCodigoPais(pedido.getRequerente().getNacionalidade());
            reqDto.setNacionalidade(getNacionalidade);

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

        // Requisição
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
