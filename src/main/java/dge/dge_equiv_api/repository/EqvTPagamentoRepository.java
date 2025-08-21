package dge.dge_equiv_api.repository;
import dge.dge_equiv_api.model.entity.EqvTPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvTPagamentoRepository extends JpaRepository<EqvTPagamento, Integer> {
    Optional<EqvTPagamento> findBynrProcesso(Integer nProcesso);
}
