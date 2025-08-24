package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTRequerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EqvTRequerenteRepository extends JpaRepository<EqvTRequerente, Integer> {
}