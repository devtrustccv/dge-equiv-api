package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.EqvTRequerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EqvTRequerenteRepository extends JpaRepository<EqvTRequerente, Integer> {
}