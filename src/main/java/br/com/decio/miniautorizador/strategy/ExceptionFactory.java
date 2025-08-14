package br.com.decio.miniautorizador.strategy;

import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;
import br.com.decio.miniautorizador.exception.*;
import org.springframework.stereotype.Component;

// Factory para criar exceptions baseada no motivo
@Component
public class ExceptionFactory {
    public BusinessException criarException(MotivoRejeicao motivo) {
        return switch (motivo){
            case CARTAO_INEXISTENTE -> new CartaoInexistenteException();
            case SENHA_INVALIDA -> new SenhaInvalidaException();
            case SALDO_INSUFICIENTE -> new SaldoInsuficienteException();
        };
    }
}
