package br.com.decio.miniautorizador.exception;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

// Exception para cartao não encontrado
public class CartaoInexistenteException extends BusinessException {
    public CartaoInexistenteException() {
        super("Cartão não existe para realizar a transação");
    }
    public CartaoInexistenteException(String numeroCartao) {
        super("Cartão com o número "+ numeroCartao + " não encontrado");
    }
    @Override
    public MotivoRejeicao getMotivoRejeicao() {
        return MotivoRejeicao.CARTAO_INEXISTENTE;
    }
}
