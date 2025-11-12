package dge.dge_equiv_api.web.exception;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLNonTransientConnectionException;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CannotGetJdbcConnectionException.class,
            SQLNonTransientConnectionException.class,
            DataAccessException.class
    })
    public ResponseEntity<Map<String, Object>> handleDatabaseConnectionError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "error", "Serviço indisponível",
                        "message", "Não foi possível conectar à base de dados. Tente novamente mais tarde.",
                        "path", "/api" // opcional: você pode obter dinamicamente via HttpServletRequest
                ));
    }

    // 🔹 Captura outras exceções gerais (para fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 500,
                        "error", "Erro interno no servidor",
                        "message", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde."
                ));
    }
}

