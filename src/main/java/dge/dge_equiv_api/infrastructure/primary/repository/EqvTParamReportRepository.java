package dge.dge_equiv_api.infrastructure.primary.repository;

import dge.dge_equiv_api.infrastructure.primary.EqvTParamReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EqvTParamReportRepository extends JpaRepository<EqvTParamReport, Integer> {
    // Podemos adicionar métodos customizados aqui se necessário
}
