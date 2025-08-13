package com.teste.decio.miniautorizador.exception;

import com.teste.decio.miniautorizador.domain.enums.MotivoRejeicao;

public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract MotivoRejeicao getMotivoRejeicao();
}
