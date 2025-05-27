package dge.dge_equiv_api.repository;



import dge.dge_equiv_api.model.entity.FamiliaProfissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamiliaProfissionalRepository extends JpaRepository<FamiliaProfissional, Integer> {

    @Override
    Optional<FamiliaProfissional> findById(Integer integer);

}

