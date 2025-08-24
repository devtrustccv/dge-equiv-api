package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface EqvTPedidoRepository extends JpaRepository<EqvTPedido, Integer> {
        List<EqvTPedido> findByRequisicao(EqvTRequisicao requisicao);

       List<EqvTPedido> findByRequisicaoId(Integer requisicaoId);
    }

