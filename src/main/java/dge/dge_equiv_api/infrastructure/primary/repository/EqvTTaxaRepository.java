package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTTaxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvTTaxaRepository extends JpaRepository<EqvTTaxa, Integer> {

    Optional<EqvTTaxa> findFirstByEstadoIgnoreCaseAndEtapaIgnoreCase(String estado, String etapa);
}
