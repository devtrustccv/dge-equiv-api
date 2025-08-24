package dge.dge_equiv_api.infrastructure.primary.repository;


import dge.dge_equiv_api.infrastructure.primary.EqvtTDecisaoVp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EqvtTDecisaoVpRepository extends JpaRepository<EqvtTDecisaoVp, Integer> {
    // métodos customizados se necessário, por exemplo:
    List<EqvtTDecisaoVp> findByPedidoId(Integer idPedido);
}

