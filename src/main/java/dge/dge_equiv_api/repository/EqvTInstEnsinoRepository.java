package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.EqvTInstEnsino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvTInstEnsinoRepository extends JpaRepository<EqvTInstEnsino, Integer> {

    Optional<EqvTInstEnsino> findByNomeIgnoreCaseAndPais(String nome, String pais);

}
