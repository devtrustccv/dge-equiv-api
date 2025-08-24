package dge.dge_equiv_api.infrastructure.tertiary.repository;

import dge.dge_equiv_api.infrastructure.tertiary.DocRelacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocRelacaoRepository extends JpaRepository<DocRelacaoEntity, Integer> {
    List<DocRelacaoEntity> findByIdRelacaoAndTipoRelacaoAndAppCode(Long idRelacao, String tipoRelacao, String appCode);

    List<DocRelacaoEntity> findByIdRelacao(Integer idRelacao);

}
