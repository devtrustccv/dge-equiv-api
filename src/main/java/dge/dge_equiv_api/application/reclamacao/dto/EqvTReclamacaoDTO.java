package dge.dge_equiv_api.application.reclamacao.dto;

import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqvTReclamacaoDTO {

    private Long id;
    private Long idPedido;
    private Long idRequisicao;
    private String observacao;
    private Integer decisao;
    private String anexo;
    private String userCreate;
    private String userUpdate;
    private LocalDate dateCreate;
    private LocalDate dateUpdate;
    private DocumentoDTO documentos;


}