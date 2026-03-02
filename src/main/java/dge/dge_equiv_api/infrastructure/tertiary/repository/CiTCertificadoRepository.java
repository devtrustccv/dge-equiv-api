package dge.dge_equiv_api.infrastructure.tertiary.repository;

import dge.dge_equiv_api.infrastructure.tertiary.CiTCertificado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CiTCertificadoRepository extends JpaRepository<CiTCertificado, Integer> {


    Optional<CiTCertificado> findTopByPessoaIdAndAppIgnoreCaseAndNomeFormacaoIgnoreCaseOrderByIdDesc(
            Long pessoaId,
            String app,
            String nomeFormacao
    );
}