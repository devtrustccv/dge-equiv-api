package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTInstEnsino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvTInstEnsinoRepository extends JpaRepository<EqvTInstEnsino, Integer> {

    Optional<EqvTInstEnsino> findByNomeIgnoreCaseAndPais(String nome, String pais);

}
