package br.com.decio.miniautorizador.strategy;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;

import java.util.Optional;

// Interface para estrategias de validacao
public interface ValidacaoStrategy {
    Optional<MotivoRejeicao> validar(TransacaoRequest request, Cartao cartao);
    int getOrdem();

}
