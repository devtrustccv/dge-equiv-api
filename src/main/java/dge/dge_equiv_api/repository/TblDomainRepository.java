package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.TblDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblDomainRepository extends JpaRepository<TblDomain, Integer> {

        @Query("""
        SELECT d FROM TblDomain d 
        WHERE d.dominio = :dominio 
          AND d.valor = :valor 
          AND d.env.dad = 'equiv'
    """)
        Optional<TblDomain> findByDominioAndValorWithEnvDadEquiv(@Param("dominio") String dominio, @Param("valor") String valor);

        @Query("""
    SELECT d FROM TblDomain d
    WHERE d.dominio = :dominio AND d.env.dad = :dad
""")
        List<TblDomain> findByDominioAndEnvDad(@Param("dominio") String dominio, @Param("dad") String dad);

}





