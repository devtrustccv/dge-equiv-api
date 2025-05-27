package dge.dge_equiv_api.service;

import dge.dge_equiv_api.model.entity.FamiliaProfissional;
import dge.dge_equiv_api.repository.FamiliaProfissionalRepository;
import org.springframework.stereotype.Service;

@Service
public class FamiliaProfissionalService {

    private  final FamiliaProfissionalRepository familiaProfissionalRepository;

    public FamiliaProfissionalService(FamiliaProfissionalRepository familiaProfissionalRepository) {
        this.familiaProfissionalRepository = familiaProfissionalRepository;
    }

    public String BuscarFamiliaProfissionalbyid(Integer id) {
        return familiaProfissionalRepository.findById(id)
                .map(FamiliaProfissional::getDenominacao)
                .orElse(null);
    }

}
