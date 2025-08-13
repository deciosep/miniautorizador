package br.com.decio.miniautorizador.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Request para transacao
public record TransacaoRequest(
        @JsonProperty("numeroCartao")
        @NotBlank(message = "Número do cartão não pode estar vazio")
        @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 dígitos")
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve conter apenas dígitos")
        String numeroCartao,

        @JsonProperty("senhaCartao")
        @NotBlank(message = "Senha não pode estar vazio")
        @Size(min = 4, max = 4, message = "Senha deve ter 4 dígitos")
        @Pattern(regexp = "\\d{4}", message = "Senha deve conter apenas dígitos")
        String senhaCartao,

        @JsonProperty("valor")
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
        BigDecimal valor){
        // Valida se eh uma trasacao de alto valor - 1000
        public boolean isTransacaoAltoValor() {
                return valor.compareTo(new BigDecimal("1000.00")) > 0;
        }
        // Valida se eh uma trasacao de baixo valor - 10
        public boolean isMicroTransacao() {
                return valor.compareTo(new BigDecimal("10.00")) < 0;
        }

        // Mascara senha para logs
        public TransacaoRequest comDadosMascarado() {
                return new TransacaoRequest(
                        numeroCartao.substring(0,4) + "****" + numeroCartao.substring(12),
                        "****",
                        valor
                );
        }

        // Converte valor para centavos para cálculos precisos
        public long getValorEmCentavos() {
                return valor.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).longValue();
        }
}

