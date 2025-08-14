package br.com.decio.miniautorizador.util;

import br.com.decio.miniautorizador.domain.dto.CriarCartaoRequest;
import br.com.decio.miniautorizador.domain.dto.CriarCartaoResponse;
import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;

import java.math.BigDecimal;
import java.util.Random;

public class GenerateData {
    private static final Random random = new Random();
    private static String gerarNumeroCartao(){
        return "1234567890" + String.format("%06d", (int)(Math.random() * 1000000));
    }

    private static String senhaPadrao = "1234";
    private static BigDecimal valorPadrao = new BigDecimal("100.00");

    public static CriarCartaoRequest dadosNovoCartao(){
        return new CriarCartaoRequest(gerarNumeroCartao(), senhaPadrao);
    }
    public static CriarCartaoRequest dadosNovoCartao(String nr, String senha){
        return new CriarCartaoRequest(nr, senha);
    }

    public static CriarCartaoResponse dadosCartaoResponse(){
        CriarCartaoRequest cartaoRequest= dadosNovoCartao();
        return dadosCartaoResponse(cartaoRequest);
    }

    public static CriarCartaoResponse dadosCartaoResponse(CriarCartaoRequest cartaoRequest){
        return new CriarCartaoResponse(cartaoRequest.numeroCartao(), cartaoRequest.senha());
    }

    public static TransacaoRequest dadosTransacao() {
        return new TransacaoRequest(gerarNumeroCartao(), senhaPadrao, valorPadrao);
    }

    public static TransacaoRequest dadosTransacao(String nr, String senha, BigDecimal valor) {
        return new TransacaoRequest(nr, senha, valor);
    }

    public static Cartao dadosCartao() {
        return new Cartao(gerarNumeroCartao(), senhaPadrao);
    }

    public static Cartao dadosCartao(String nr, String senha) {
        return new Cartao(nr, senha);
    }
}
