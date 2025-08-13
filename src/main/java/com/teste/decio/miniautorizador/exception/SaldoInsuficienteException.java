package com.teste.decio.miniautorizador.exception;

import com.teste.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para saldo insuficiente
public abstract class SaldoInsuficienteException extends BusinessException {
    public SaldoInsuficienteException(String numeroCartao) {
        super("saldo insuficiente para realizar a operação");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.SALDO_INSUFICIENTE;
    }
}
