package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para senhaCartao
public class SenhaInvalidaException extends BusinessException {
    public SenhaInvalidaException(String numeroCartao) {
        super("Senha do cartão inválida");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.SENHA_INVALIDA;
    }
}
