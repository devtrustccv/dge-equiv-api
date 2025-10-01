package dge.dge_equiv_api.exception.controller;

import dge.dge_equiv_api.exception.BusinessException;
import dge.dge_equiv_api.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "Validação de negócio"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Outros handlers se necessário
}

