package com.teste.decio.miniautorizador.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

// Request para transacao
public record TransacaoRequest(
        @JsonProperty("numeroCartao")
        @NotBlank(message = "Número do cartão não pode estar vazio")
        @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 dígitos")
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve conter apenas dígitos")
        String numeroCartao,

        @JsonProperty("senha")
        @NotBlank(message = "Senha não pode estar vazio")
        @Size(min = 4, max = 4, message = "Senha deve ter 4 dígitos")
        @Pattern(regexp = "\\d{4}", message = "Senha deve conter apenas dígitos")
        String senha,

        @JsonProperty("valor")
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal saldo){}

