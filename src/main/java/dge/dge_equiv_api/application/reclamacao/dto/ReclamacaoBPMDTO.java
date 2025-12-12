package dge.dge_equiv_api.application.reclamacao.dto;

import lombok.Data;

@Data
public class ReclamacaoBPMDTO {
    private String reclamar;
    private String obs;
    private String reclamacao_;
    private String reclamacao__fk;
    private String reclamacao__fk_desc;
    private String obs_fk;
    private String despacho;
    private String despacho__fk;
    private String despacho__fk_desc;
    private String reclamacao_1;
    private String reclamacao_1__fk;
    private String reclamacao_1__fk_desc;
}