package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTPedido;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequisicao;
import dge.dge_equiv_api.infrastructure.primary.EqvtTDecisaoVp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface EqvTPedidoRepository extends JpaRepository<EqvTPedido, Integer> {
        List<EqvTPedido> findByRequisicao(EqvTRequisicao requisicao);

       List<EqvTPedido> findByRequisicaoId(Integer requisicaoId);

       List<EqvTPedido> findByRequisicaoNProcesso(Integer nProcesso);


    /**
     * Query OTIMIZADA usando JOIN FETCH para evitar N+1
     * Traz tudo em uma única consulta ao banco
     */
    @Query("""
        SELECT d FROM EqvtTDecisaoVp d 
        JOIN FETCH d.pedido p 
        JOIN FETCH p.requisicao r 
        WHERE r.NProcesso = :numeroProcesso 
        AND d.motivoRetificado IS NOT NULL 
        AND TRIM(d.motivoRetificado) != ''
        ORDER BY d.dataCreate DESC
        """)
    List<EqvtTDecisaoVp> findDecisoesVpComMotivoByNumeroProcesso(
            @Param("numeroProcesso") Integer numeroProcesso);
    }

