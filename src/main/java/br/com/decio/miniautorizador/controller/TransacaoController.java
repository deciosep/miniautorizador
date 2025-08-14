package br.com.decio.miniautorizador.controller;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.service.TransacaoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final static Logger log = LoggerFactory.getLogger(CartaoController.class);
    private TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@Valid @RequestBody TransacaoRequest request) {
        log.info("Recebendo o request da transacao : numero {}, senha {} ", request.numeroCartao(), request.senha());
        transacaoService.processarTransacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
}
