package dge.dge_equiv_api.application.duc.service;

import dge.dge_equiv_api.application.duc.dto.DucModel;
import dge.dge_equiv_api.application.taxa.service.EqvTTaxaService;
import dge.dge_equiv_api.infrastructure.primary.EqvTPagamento;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    @Value("${link.report.integration.sigof.duc}")
    private String link;

    @Value("${api.base.service.Duc}")
    private String duc;

    @Autowired
    private EqvTTaxaService eqvTTaxaService ;
     @Autowired
    private  EqvTPagamentoRepository pagamentoRepository;
    public EqvTPagamento gerarDuc(EqvTPedido pedido, String nif, Integer nrProcesso) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            BigDecimal valortaxa = eqvTTaxaService.getValorAtivoParaPagamentoAnalise();
            // Monta URL com query params
            String url = UriComponentsBuilder
                    .fromHttpUrl(duc) // duc deve ser algo como "http://localhost:8083/api/duc/criar"
                    .queryParam("valor", 1000 )
                    .queryParam("nif", nif)
                    .queryParam("obs", "teste duc equiv")
                    .toUriString();

            System.out.println("saida....."+url);

            // Usa GET porque não há body
            DucModel duc = restTemplate.postForObject(url, null,DucModel.class);

            return savePagamento(pedido, duc, nrProcesso);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar DUC via endpoint pai", e);
        }
    }



    private EqvTPagamento savePagamento(EqvTPedido pedido, DucModel duc, Integer nrProcesso) {
        EqvTPagamento pagamento = new EqvTPagamento();

        if (pedido != null) {
            pagamento.setIdPedido(pedido);
        }

        pagamento.setNuDuc(toBigDecimal(duc.getDuc()));
        pagamento.setTotal(toBigDecimal(duc.getMontante()));
        pagamento.setReferencia(toBigDecimal(duc.getReferencia()));
        pagamento.setEntidade(duc.getEntidade());
        pagamento.setLinkDuc(link + duc.getDuc());
        pagamento.setEstado(1);
        pagamento.setNrProcesso(nrProcesso);
        pagamento.setEtapa("pagamento_analise");

        System.out.println("Salvou pagamento: " + duc.getDuc());
        return pagamentoRepository.save(pagamento);

    }

    private BigDecimal toBigDecimal(String value) {
        try {
            return value != null ? new BigDecimal(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public EqvTPagamento buscarPagamentoPorProcesso(Integer nProcesso) {
        return pagamentoRepository.findBynrProcesso(nProcesso)
                .orElse(null); // retorna null se não houver pagamento
    }
}
