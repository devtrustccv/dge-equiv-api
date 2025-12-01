package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvTRequisicaoRepository extends JpaRepository<EqvTRequisicao, Integer> {
    @Override
    Optional<EqvTRequisicao> findById(Integer integer);
    Optional<EqvTRequisicao> findByNProcesso(Integer nProcesso); //findByNProcesso(String nProcesso)>
}

