package com.teste.decio.miniautorizador.exception;

import com.teste.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para senha
public abstract class SenhaInvalidaException extends BusinessException {
    public SenhaInvalidaException(String numeroCartao) {
        super("Senha do cartão inválida");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.SENHA_INVALIDA;
    }
}
