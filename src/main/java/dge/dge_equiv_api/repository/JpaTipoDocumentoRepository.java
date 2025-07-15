package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.TipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaTipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, String> {

    @Query("SELECT t FROM TipoDocumentoEntity t " +
            "WHERE t.status = :status AND t.etapa = :etapa " +
            "ORDER BY t.nome ASC")
    List<TipoDocumentoEntity> findByEtapaAndStatusOrderByNomeAsc(@Param("etapa") String etapa, @Param("status") String status);
}
