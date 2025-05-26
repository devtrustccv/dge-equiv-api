package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.TblDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TblDomainRepository extends JpaRepository<TblDomain, Integer> {

        Optional<TblDomain> findByDominioAndValor(String dominio, String valor);


}





