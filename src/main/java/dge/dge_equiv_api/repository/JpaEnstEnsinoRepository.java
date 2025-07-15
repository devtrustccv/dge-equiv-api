package dge.dge_equiv_api.repository;


import dge.dge_equiv_api.model.entity.EqvTInstEnsino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaEnstEnsinoRepository extends JpaRepository<EqvTInstEnsino, Integer> {

    List<EqvTInstEnsino> findByStatusOrderByNomeAsc(String status);
}

