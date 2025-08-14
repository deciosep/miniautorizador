package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para cartao não encontrado
public class CartaoNaoEncontradoException extends RuntimeException {
    public CartaoNaoEncontradoException(String numeroCartao) {
        super("Cartão com o número "+ numeroCartao + " não encontrado");
    }
}
