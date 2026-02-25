package dge.dge_equiv_api.infrastructure.tertiary.repository;

import dge.dge_equiv_api.infrastructure.tertiary.CiTCertificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CiTCertificadoRepository extends JpaRepository<CiTCertificado, Integer> {

    @Query("""
    SELECT c FROM CiTCertificado c
    WHERE c.pessoaId = :pessoaId
      AND LOWER(TRIM(c.app)) = 'equiv'
      AND LOWER(TRIM(c.nomeFormacao)) = LOWER(TRIM(:nomeFormacao))
    ORDER BY c.createdAt DESC
""")
    Optional<CiTCertificado> buscarCertificadoEquivPorNome(
            Long pessoaId,
            String nomeFormacao
    );
}