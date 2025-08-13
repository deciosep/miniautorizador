package br.com.decio.miniautorizador.strategy;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Strategy para validar a existencia do cartao
@Component
public class ValidacaoSenhaStrategy implements ValidacaoStrategy {

    @Override
    public Optional<MotivoRejeicao> validar(TransacaoRequest request, Cartao cartao) {
        return Optional.ofNullable(cartao)
                .filter( c -> c.senhaCorreta(request.senhaCartao()))
                .map( c -> Optional.<MotivoRejeicao>empty())
                .orElse(Optional.of(MotivoRejeicao.SENHA_INVALIDA));
    }

    @Override
    public int getOrdem() {
        return 2;
    }
}
