package br.com.decio.miniautorizador.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CriarCartaoResponse(@JsonProperty("numeroCartao") String numeroCartao,
                                  @JsonProperty("senha") String senha) {
    // Responde cartão criada com sucesso
    public static CriarCartaoResponse of(String numeroCartao, String senha) {
        return new CriarCartaoResponse(numeroCartao, senha);
    }
    // Mascara senha para logs
    public CriarCartaoResponse comSenhaMascarada(){
        return new CriarCartaoResponse(numeroCartao, "****");
    }
}
