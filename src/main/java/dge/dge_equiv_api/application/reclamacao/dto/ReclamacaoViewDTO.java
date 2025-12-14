package dge.dge_equiv_api.application.reclamacao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamacaoViewDTO {

    // Número do Processo
    private Integer numeroProcesso;

    // Nº Apresentação
    private Integer numeroApresentacao;

    // País de Obtenção
    private String paisObtencao;

    // Instituição FTP
    private String instituicaoFtp;

    // Carga Horária
    private Integer cargaHoraria;

    // Data do Despacho
    private LocalDate dataDespacho;

    // Despacho
    private String despacho;

    private boolean podeAlterarSolic;
    private String messagemEstado;
}
