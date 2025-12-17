package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvtTDecisaoDespacho;
import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EqvtTDecisaoDespachoRepository extends JpaRepository<EqvtTDecisaoDespacho, Integer> {

    Optional<EqvtTDecisaoDespacho> findByIdPedido(EqvTPedido pedido);
}
