package dge.dge_equiv_api.application.InstEnsino.service;


import dge.dge_equiv_api.application.InstEnsino.dto.InstituicaoPaisDTO;
import dge.dge_equiv_api.application.pedido.dto.EqvTInstEnsinoDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTInstEnsino;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTInstEnsinoRepository;
import org.springframework.stereotype.Service;

@Service
public class EqvTInstEnsinoServiceImpl implements EqvTInstEnsinoService {

    private final EqvTInstEnsinoRepository repository;

    public EqvTInstEnsinoServiceImpl(EqvTInstEnsinoRepository repository) {
        this.repository = repository;
    }

    @Override
    public InstituicaoPaisDTO getPaisById(Integer id) {
        EqvTInstEnsino inst = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        return InstituicaoPaisDTO.builder()
                .id(inst.getId())
                .pais(inst.getPais())
                .build();
    }

}