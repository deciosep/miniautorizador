package br.com.decio.miniautorizador.strategy;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Strategy para validar a existencia do cartao
@Component
public class ValidacaoCartaoExistenteStrategy implements ValidacaoStrategy {
    @Override
    public Optional<MotivoRejeicao> validar(TransacaoRequest request, Cartao cartao) {
        return Optional.ofNullable(cartao)
                .map( c -> Optional.<MotivoRejeicao>empty())
                .orElse(Optional.of(MotivoRejeicao.CARTAO_INEXISTENTE));
    }

    @Override
    public int getOrdem() {
        return 1;
    }
}
