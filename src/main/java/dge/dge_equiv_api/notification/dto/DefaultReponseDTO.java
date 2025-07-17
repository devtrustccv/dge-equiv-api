package dge.dge_equiv_api.notification.dto;

import lombok.Data;

@Data
public class DefaultReponseDTO {
    private String msg;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}