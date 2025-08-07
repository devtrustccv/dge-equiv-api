package dge.dge_equiv_api.exception;

public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    // getter e setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}

