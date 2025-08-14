package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para valor insuficiente
public class SaldoInsuficienteException extends BusinessException {
    public SaldoInsuficienteException() {
        super("Saldo insuficiente para realizar a operação");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.SALDO_INSUFICIENTE;
    }
}
