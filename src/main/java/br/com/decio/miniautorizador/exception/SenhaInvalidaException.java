package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para senha
public class SenhaInvalidaException extends BusinessException {
    public SenhaInvalidaException() {
        super("Senha do cartão inválida");
    }
    public SenhaInvalidaException(String numeroCartao) {
        super("Senha do cartão " + numeroCartao + " inválida");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.SENHA_INVALIDA;
    }
}
