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

    private String email;
    private String message;
    private String status;
    private String subject;
    private String appName;
    private String from;
    //private List<FileDTO> files = new ArrayList<>();



}
