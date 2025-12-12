package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTReclamacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EqvTReclamacaoRepository extends JpaRepository<EqvTReclamacao, Long> {

    // Buscar reclamação por ID do pedido
    Optional<EqvTReclamacao> findByIdPedido_Id(Long idPedido);
    Optional<EqvTReclamacao> findById(Long id);

}