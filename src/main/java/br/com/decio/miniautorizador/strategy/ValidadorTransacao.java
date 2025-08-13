package br.com.decio.miniautorizador.strategy;

import br.com.decio.miniautorizador.domain.dto.ResultadoAutorizacao;
import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// Servico que orquestra as validacoes usando Strategy Pattern
@Component
public class ValidadorTransacao {
    private List<ValidacaoStrategy> estrategias;
    public ValidadorTransacao(List<ValidacaoStrategy> estrategias) {
        this.estrategias = estrategias.stream()
                .sorted( (e1, e2) -> Integer.compare(e1.getOrdem(), e2.getOrdem()))
                .toList();
    }
    public ResultadoAutorizacao validar(TransacaoRequest request, Cartao cartao) {
        return estrategias.stream()
                .map( strat -> strat.validar(request, cartao))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .map(ResultadoAutorizacao::rejeitada)
                .orElse(ResultadoAutorizacao.aprovada());

    }
}
