package dge.dge_equiv_api.service;

import dge.dge_equiv_api.model.entity.NivelQualificacao;
import dge.dge_equiv_api.repository.NivelQualificacaoRepository;
import org.springframework.stereotype.Service;


@Service
public class NivelQualificacaoService {

  private  final NivelQualificacaoRepository nivelQualificacaoRepository;

  public NivelQualificacaoService(NivelQualificacaoRepository nivelQualificacaoRepository) {
    this.nivelQualificacaoRepository = nivelQualificacaoRepository;
  }

    public Integer BuscarNivelQualificacaoPorId(Integer id) {
        return nivelQualificacaoRepository.findById(id)
                .map(NivelQualificacao::getNumber)
                .orElse(null);
    }

}
