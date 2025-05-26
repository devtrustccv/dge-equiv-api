package dge.dge_equiv_api.service;

import dge.dge_equiv_api.model.dto.*;
import dge.dge_equiv_api.model.entity.EqvTPedido;
import dge.dge_equiv_api.model.entity.TblDomain;
import dge.dge_equiv_api.repository.EqvTPedidoRepository;
import dge.dge_equiv_api.repository.TblDomainRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service


public class EqvTPedidoService {

    private final EqvTPedidoRepository pedidoRepository;
    private final TblDomainService tblDomainService;
    private final GlobalGeografiaService globalGeografiaService; // Injeção de dependência.

    public EqvTPedidoService(EqvTPedidoRepository pedidoService, TblDomainService tblDomainService, GlobalGeografiaService globalGeografiaService) {
        this.pedidoRepository = pedidoService;
        this.tblDomainService = tblDomainService;
        this.globalGeografiaService = globalGeografiaService;
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
        dto.setDespacho(pedido.getDespacho());
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
//            String descricaoNacionalidade = tblDomainService.buscarDescricaoPorDominioEValor("NACIONALIDADE", pedido.getRequerente().getNacionalidade());
//            reqDto.setNacionalidade(descricaoNacionalidade);
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
            instDto.setEndereco(pedido.getInstEnsino().getEndereco());
            instDto.setEmail(pedido.getInstEnsino().getEmail());
            instDto.setContato(pedido.getInstEnsino().getContato());
            String getPais = globalGeografiaService.buscarNomePorCodigoPais(pedido.getInstEnsino().getPais());
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
