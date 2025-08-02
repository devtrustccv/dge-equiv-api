package dge.dge_equiv_api.repository;


import dge.dge_equiv_api.model.entity.EqvtTDecisaoVp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EqvtTDecisaoVpRepository extends JpaRepository<EqvtTDecisaoVp, Integer> {
    // métodos customizados se necessário, por exemplo:
    List<EqvtTDecisaoVp> findByPedidoId(Integer idPedido);
}

