package dge.dge_equiv_api.web.exception;

import dge.dge_equiv_api.exception.BusinessException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLNonTransientConnectionException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Erros de validação de negócio
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validação de negócio",
                "message", ex.getMessage()
        ));
    }

    /**
     * Erros de conexão com o banco de dados (ex: BD offline)
     */
    @ExceptionHandler({
            CannotGetJdbcConnectionException.class,
            SQLNonTransientConnectionException.class
    })
    public ResponseEntity<Map<String, Object>> handleDatabaseConnectionException(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Erro de conexão com a base de dados",
                "message", "Não foi possível estabelecer ligação com a base de dados. Tente novamente mais tarde."
        ));
    }

    /**
     * Outros erros de acesso a dados (consultas, inserts, updates)
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Erro ao aceder à base de dados",
                "message", "Ocorreu um erro ao tentar ler ou gravar dados. Verifique os logs para mais detalhes."
        ));
    }

    /**
     * Qualquer outro erro não previsto
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Erro interno no servidor",
                "message", ex.getMessage()
        ));
    }



    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionException(CannotCreateTransactionException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Erro de conexão com a base de dados",
                "message", "Ocorreu um erro interno. Por favor, tente novamente mais tarde."
        ));
    }

}
