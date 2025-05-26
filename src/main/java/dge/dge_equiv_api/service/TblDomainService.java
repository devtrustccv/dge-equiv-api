package dge.dge_equiv_api.service;

import dge.dge_equiv_api.model.entity.TblDomain;
import dge.dge_equiv_api.repository.TblDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class TblDomainService {

    private final TblDomainRepository tblDomainRepository;

    public TblDomainService(TblDomainRepository tblDomainRepository) {
        this.tblDomainRepository = tblDomainRepository;
    }

    public String buscarDescricaoPorDominioEValor(String dominio, String valor) {
        return tblDomainRepository.findByDominioAndValor(dominio, valor)
                .map(TblDomain::getDescription)
                .orElse(valor); // Se não encontrar, retorna o valor original
    }

}


