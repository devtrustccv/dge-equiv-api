package dge.dge_equiv_api.application.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationRequestDTO {

    private String mensagem;
    private String assunto;
    private String appCode;
    private String email;
    private String tipoProcesso;
    private String idProcesso;
    private String tipoRelacao;
    private String idRelacao;
    private String isAlert;

}
