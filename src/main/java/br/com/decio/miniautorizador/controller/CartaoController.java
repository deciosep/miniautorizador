package br.com.decio.miniautorizador.controller;

import br.com.decio.miniautorizador.domain.dto.CriarCartaoRequest;
import br.com.decio.miniautorizador.domain.dto.CriarCartaoResponse;
import br.com.decio.miniautorizador.service.CartaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping
    public ResponseEntity<CriarCartaoResponse> criarCartao(@Valid @RequestBody CriarCartaoRequest request) {
        CriarCartaoResponse response = cartaoService.criarCartao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);
        return ResponseEntity.ok(saldo);
    }
}
