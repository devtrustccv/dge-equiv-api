package dge.dge_equiv_api.domain;

import dge.dge_equiv_api.model.entity.TblDomain;
import dge.dge_equiv_api.repository.TblDomainRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/domain")
public class DomainController {

    private final TblDomainRepository tblDomainRepository;

    public DomainController(TblDomainRepository tblDomainRepository) {
        this.tblDomainRepository = tblDomainRepository;
    }

    // Retorna o domínio 'APRECIACAO' por padrão
    @GetMapping("/apreciacao")
    public Map<String, String> getDominioApreciacao() {
        return getDominioValores("APRECIACAO");
    }

    @GetMapping("/habilitacao")
    public Map<String, String> getDominioHabilitacao() {
        return getDominioValores("HABILITAÇÃO");
    }

    @GetMapping("/tipo_documento_ident")
    public Map<String, String> getDominioTipoDocumentoIdent() {
        return getDominioValores("TIPO_DOCUMENTO_IDENT");
    }
    @GetMapping("/sexo")
    public Map<String, String> getDominioTSexo() {
        return getDominioValores("SEXO");
    }




    public Map<String, String> getDominioValores(@PathVariable String dominio) {
        List<TblDomain> lista = tblDomainRepository.findByDominioAndEnvDad(dominio.toUpperCase(), "equiv");

        Map<String, String> map = new LinkedHashMap<>();
        //map.put("", "--Selecionar--");

        for (TblDomain d : lista) {
            map.put(d.getValor(), d.getDescription());
        }

        return map;
    }
}

