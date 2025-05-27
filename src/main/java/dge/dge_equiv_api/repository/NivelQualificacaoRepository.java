package dge.dge_equiv_api.repository;

import dge.dge_equiv_api.model.entity.NivelQualificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NivelQualificacaoRepository extends JpaRepository<NivelQualificacao, Integer> {
    @Override
    Optional<NivelQualificacao> findById(Integer integer);
}
