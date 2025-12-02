package dge.dge_equiv_api.application.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
    private String userId;
    private String userEmail;
    private String action;
    private String tableName;
    private String tableId;
    private Integer profileId;
    private String profileName;
    private Integer orgId;
    private String orgName;
    private String appDad;
    private String appName;
    private String obs;
    private List<LogItemDTO> logsItems;
}
