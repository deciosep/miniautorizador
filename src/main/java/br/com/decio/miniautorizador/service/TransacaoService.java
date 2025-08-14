package br.com.decio.miniautorizador.service;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransacaoService {
    private final CartaoService cartaoService;
    public TransacaoService(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    public void processarTransacao(TransacaoRequest request) {
        cartaoService.autorizarTransacao(request);
    }
}
