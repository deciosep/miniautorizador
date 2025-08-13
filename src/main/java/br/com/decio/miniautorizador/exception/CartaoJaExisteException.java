package br.com.decio.miniautorizador.exception;

// Exception para cartao ja existente
public class CartaoJaExisteException extends RuntimeException {
    public CartaoJaExisteException(String numeroCartao) {
        super("Cartão com o número "+ numeroCartao + " já existe");
    }
}
