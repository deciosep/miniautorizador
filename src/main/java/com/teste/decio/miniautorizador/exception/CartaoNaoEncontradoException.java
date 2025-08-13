package com.teste.decio.miniautorizador.exception;

import com.teste.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para cartao não encontrado
public abstract class CartaoNaoEncontradoException extends BusinessException {
    public CartaoNaoEncontradoException(String numeroCartao) {
        super("Cartão com o número "+ numeroCartao + " não encontrado");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.CARTAO_INEXISTENTE;
    }
}
