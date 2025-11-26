// DocumentoMapper.java
package dge.dge_equiv_api.application.pedidov01.mapper;

import dge.dge_equiv_api.application.document.dto.DocumentoDTO;
import dge.dge_equiv_api.application.document.dto.DocumentoResponseDTO;
import dge.dge_equiv_api.application.document.dto.DocRelacaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentoMapper {

    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "path", source = "path")
    @Mapping(target = "tipoRelacao", source = "tipoRelacao")
    @Mapping(target = "idRelacao", source = "idRelacao")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "appCode", source = "appCode")
    // CAMPOS QUE NÃO EXISTEM EM DocRelacaoDTO DEVEM SER IGNORADOS
    @Mapping(target = "previewUrl", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    DocumentoResponseDTO toResponseDTO(DocRelacaoDTO docRelacaoDTO);

    @Mapping(target = "nome",        source = "fileName")
    @Mapping(target = "urlArquivo",  source = "previewUrl")
    @Mapping(target = "file",        ignore = true)
    DocumentoDTO toDTO(DocumentoResponseDTO responseDTO);
}
