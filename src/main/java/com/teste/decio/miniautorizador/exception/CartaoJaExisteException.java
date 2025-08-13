package com.teste.decio.miniautorizador.exception;

// Exception para cartao ja existente
public abstract class CartaoJaExisteException extends RuntimeException {
    public CartaoJaExisteException(String numeroCartao) {
        super("Cartão com o número "+ numeroCartao + " já existe");
    }
}
