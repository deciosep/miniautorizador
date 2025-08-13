package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.dto.CriarCartaoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CartaoJaExisteException.class)
    public ResponseEntity<CriarCartaoResponse> handleCartaoJaExiste(CartaoJaExisteException e) {
        log.warn("Tentativa de criar cartão já existente: {}", e.getMessage());
        // Retorna os dados do cartao existente conforme especificacao
        String numeroCartao = extractNumeroCartaoFromMessage(e.getMessage());
        CriarCartaoResponse response = new CriarCartaoResponse(numeroCartao, "****");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(CartaoNaoEncontradoException.class)
    public ResponseEntity<Void> handleCartaoNaoEncontrado(CartaoNaoEncontradoException e) {
        log.warn("Cartão não encontrado: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ConcorrenciaException.class)
    public ResponseEntity<String> handleConcorrenciaException(ConcorrenciaException e) {
        log.warn("Erro de concorrência: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("ERRO_CONCORRENCIA");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        log.warn("Erro de validação: {}", e.getMessage());
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        log.warn("Regra de negócio violada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(e.getMotivoRejeicao().name());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.warn("Erro interno do servidor: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("ERRO_INTERNO");
    }

    private String extractNumeroCartaoFromMessage(String message) {
        // Extrai o nr cartao
        return message.replaceAll(".*número (\\d{16}).*", "$1");
    }
}
