package dge.dge_equiv_api.application.pedido.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EqvtPedidoUpadateDto {

        private Integer id;
        @JsonIgnore

        private EqvTRequerenteDTO requerente;
        @NotNull(message = "Instituição é obrigatório")
        private EqvTInstEnsinoDTO instEnsino;
        @JsonIgnore
        private EqvTRequisicaoDTO requisicao;
        @NotNull(message = "Formação Profissional é obrigatório")
        private String formacaoProf;
        @NotNull (message = "Carga horária é obrigatório")
        private Integer carga;
        @NotNull (message = "Ano de início é obrigatório")
        private BigDecimal anoInicio;
        @NotNull (message = "Ano de fim é obrigatório")
        private BigDecimal anoFim;
        private Integer nivel;
        private String familia;
        private String despacho;
        private String numDeclaracao;
        private Integer Status;


        private List<DocumentoDTO> documentos;
        private List<DocumentoResponseDTO> documentosresp;

    }


