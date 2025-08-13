package br.com.decio.miniautorizador.controller;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@Valid @RequestBody TransacaoRequest request) {
        transacaoService.processarTransacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
}
