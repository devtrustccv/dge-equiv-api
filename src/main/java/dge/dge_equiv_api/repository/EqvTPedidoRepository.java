package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.EqvTPedido;
import dge.dge_equiv_api.model.entity.EqvTRequisicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface EqvTPedidoRepository extends JpaRepository<EqvTPedido, Integer> {
      //  List<EqvTPedido> findByRequisicao(EqvTRequisicao requisicao);
    }

