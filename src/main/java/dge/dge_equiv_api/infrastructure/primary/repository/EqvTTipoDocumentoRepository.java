package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTTipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvTTipoDocumentoRepository extends JpaRepository<EqvTTipoDocumento, Integer> {
    Optional<EqvTTipoDocumento> findById(Integer integer);
}
