package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para valor insuficiente
public class SaldoInsuficienteException extends BusinessException {
    public SaldoInsuficienteException(String numeroCartao) {
        super("valor insuficiente para realizar a operação");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.SALDO_INSUFICIENTE;
    }
}
