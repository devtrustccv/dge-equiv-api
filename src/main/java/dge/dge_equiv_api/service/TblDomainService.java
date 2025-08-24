package dge.dge_equiv_api.service;

import dge.dge_equiv_api.infrastructure.secondary.TblDomain;
import dge.dge_equiv_api.infrastructure.secondary.repository.TblDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class TblDomainService {

    private final TblDomainRepository tblDomainRepository;

    public TblDomainService(TblDomainRepository tblDomainRepository) {
        this.tblDomainRepository = tblDomainRepository;
    }

    public String buscarDescricaoPorDominioEValor(String dominio, String valor) {
        return tblDomainRepository.findByDominioAndValorWithEnvDadEquiv(dominio, valor)
                .map(TblDomain::getDescription)
                .orElse(valor);
    }


}


