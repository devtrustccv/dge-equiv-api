package dge.dge_equiv_api.application.taxa.service;


import dge.dge_equiv_api.infrastructure.primary.EqvTTaxa;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTTaxaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
    public class EqvTTaxaService {

        @Autowired
        private EqvTTaxaRepository taxaRepository;

        public BigDecimal getValorAtivoParaPagamentoAnalise() {
            return taxaRepository
                    .findFirstByEstadoIgnoreCaseAndEtapaIgnoreCase("A", "pagamento_analise")
                    .map(EqvTTaxa::getValor)
                    .orElse(BigDecimal.ZERO);
        }

    public BigDecimal getValorAtivoParaPagamentoCertificado() {
        return taxaRepository
                .findFirstByEstadoIgnoreCaseAndEtapaIgnoreCase("A", "pagCertificado")
                .map(EqvTTaxa::getValor)
                .orElse(BigDecimal.ZERO);
    }


}
