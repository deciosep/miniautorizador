package com.teste.decio.miniautorizador.exception;

// Exception para erro de concorrencia
public abstract class ConcorrenciaException extends RuntimeException {
    public ConcorrenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
