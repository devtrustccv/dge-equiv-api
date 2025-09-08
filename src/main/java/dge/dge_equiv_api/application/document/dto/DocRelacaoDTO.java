package dge.dge_equiv_api.application.document.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocRelacaoDTO {
    private Integer idRelacao;
    private String tipoRelacao;
    private String estado;
    private String idTpDoc;
    private String fileName;
    private String path;
    private String appCode;
    private MultipartFile file;
    private String nProcesso;

}
