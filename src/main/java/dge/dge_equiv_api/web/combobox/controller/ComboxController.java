package dge.dge_equiv_api.web.combobox.controller;

import dge.dge_equiv_api.model.entity.EqvTInstEnsino;
import dge.dge_equiv_api.model.entity.TipoDocumentoEntity;
import dge.dge_equiv_api.repository.JpaEnstEnsinoRepository;
import dge.dge_equiv_api.repository.JpaTipoDocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/combobox")
public class ComboxController {
    private final JpaTipoDocumentoRepository jpaTipoDocumentoRepository;
    private  final JpaEnstEnsinoRepository jpaEnstEnsinoRepository;


    public ComboxController(JpaTipoDocumentoRepository jpaTipoDocumentoRepository, JpaEnstEnsinoRepository jpaEnstEnsinoRepository) {
        this.jpaTipoDocumentoRepository = jpaTipoDocumentoRepository;
        this.jpaEnstEnsinoRepository = jpaEnstEnsinoRepository;
    }

    @GetMapping("/tipo_documento")
    public List<Map<String, String>> getTipoDocumentoByReferencia() {

        String etapa = "solitacao";



        List<TipoDocumentoEntity> tipoDocumentoOpt = jpaTipoDocumentoRepository.findByEtapaAndStatusOrderByNomeAsc(etapa, "A");
        List<Map<String, String>> list = new ArrayList<>();

        if (tipoDocumentoOpt != null) {
            for (TipoDocumentoEntity doc : tipoDocumentoOpt) {
                Map<String, String> response = new HashMap<>();
                response.put("VALOR", String.valueOf(doc.getId()));
                response.put("DESCRICAO", doc.getNome());
                list.add(response);
            }
        }

        return list;
    }



    @GetMapping("/instituicoes")
    public List<Map<String, String>> getInstituicoesAtivasSelect() {
        List<EqvTInstEnsino> instituicoes = jpaEnstEnsinoRepository.findByStatusOrderByNomeAsc("A");
        List<Map<String, String>> list = new ArrayList<>();

        for (EqvTInstEnsino inst : instituicoes) {
            Map<String, String> map = new HashMap<>();
            map.put("VALOR", String.valueOf(inst.getId()));      // valor do option
            map.put("DESCRICAO", inst.getNome());                // texto visível no option
            list.add(map);
        }

        return list;
    }



}
