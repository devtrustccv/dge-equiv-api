package dge.dge_equiv_api.application.pedido.service;

import dge.dge_equiv_api.infrastructure.primary.EqvTTipoDocumento;
import dge.dge_equiv_api.infrastructure.primary.repository.EqvTTipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EqvTTipoDocumentoService {

    private final EqvTTipoDocumentoRepository tipoDocumentoRepository;

    public EqvTTipoDocumentoService(EqvTTipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public String buscarNomePorId(Integer id) {
        Optional<EqvTTipoDocumento> tipoDocOpt = tipoDocumentoRepository.findById(id);
        return tipoDocOpt.map(EqvTTipoDocumento::getNome).orElse(null);
    }
}
