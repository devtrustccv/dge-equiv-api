package dge.dge_equiv_api.application.pedido.mapper;
import dge.dge_equiv_api.application.pedido.dto.EqvTRequerenteDTO;
import dge.dge_equiv_api.infrastructure.primary.EqvTRequerente;
import org.springframework.stereotype.Component;

@Component
public class RequerenteMapper {

    public EqvTRequerente toEntity(EqvTRequerenteDTO dto) {
        EqvTRequerente requerente = new EqvTRequerente();
        requerente.setNome(dto.getNome());
        requerente.setDocNumero(dto.getDocNumero());
        requerente.setEmail(dto.getEmail());
        requerente.setNif(dto.getNif());
        requerente.setDataEmissaoDoc(dto.getDataEmissaoDoc());
        requerente.setDataValidadeDoc(dto.getDataValidadeDoc());
        requerente.setDataNascimento(dto.getDataNascimento());
        requerente.setNacionalidade(dto.getNacionalidade());
        requerente.setSexo(dto.getSexo());
        requerente.setHabilitacao(Integer.valueOf(dto.getHabilitacao()));
        requerente.setDocIdentificacao(dto.getDocIdentificacao());
        requerente.setDataUpdate(dto.getDataUpdate());
        return requerente;
    }

    public EqvTRequerenteDTO toDTO(EqvTRequerente requerente) {
        EqvTRequerenteDTO dto = new EqvTRequerenteDTO();
        dto.setId(requerente.getId());
        dto.setNome(requerente.getNome());
        dto.setNif(requerente.getNif());
        dto.setDocIdentificacao(requerente.getDocIdentificacao());
        dto.setDataEmissaoDoc(requerente.getDataEmissaoDoc());
        dto.setDataValidadeDoc(requerente.getDataValidadeDoc());
        dto.setDocNumero(requerente.getDocNumero());
        dto.setDataNascimento(requerente.getDataNascimento());
        dto.setNacionalidade(requerente.getNacionalidade());
        dto.setSexo(requerente.getSexo());
        dto.setHabilitacao(String.valueOf(requerente.getHabilitacao()));
        dto.setDateCreate(requerente.getDateCreate());
        dto.setDataUpdate(requerente.getDataUpdate());
        return dto;
    }
}