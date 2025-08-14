package br.com.decio.miniautorizador.exception;

// Exception para erro de concorrencia
public class ConcorrenciaException extends RuntimeException {
    public ConcorrenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}