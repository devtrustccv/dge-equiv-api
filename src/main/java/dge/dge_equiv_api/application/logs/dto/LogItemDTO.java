package dge.dge_equiv_api.application.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogItemDTO {
    private String date;
    private String column;
    private String oldValue;
    private String newValue;
    private String etapa;
    private boolean file;
}

