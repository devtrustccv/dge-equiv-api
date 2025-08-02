package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.EqvTTipoDocumento;
import dge.dge_equiv_api.model.entity.FamiliaProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EqvTTipoDocumentoRepository extends JpaRepository<EqvTTipoDocumento, Integer> {
    Optional<EqvTTipoDocumento> findById(Integer integer);
}
