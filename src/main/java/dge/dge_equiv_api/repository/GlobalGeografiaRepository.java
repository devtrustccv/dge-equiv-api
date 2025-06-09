package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.GlobalGeografia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GlobalGeografiaRepository extends JpaRepository<GlobalGeografia, String> {

    Optional<GlobalGeografia> findById(String id);
}
