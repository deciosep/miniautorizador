package br.com.decio.miniautorizador.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Request para criacao do cartao
public record CriarCartaoRequest(
        @JsonProperty("numeroCartao")
        @NotBlank(message = "Número do cartão é obrigatório")
        @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 dígitos")
        @Pattern(regexp = "\\d{16}", message = "Número do cartão deve conter apenas dígitos")
        String numeroCartao,

        @JsonProperty("senha")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 4, max = 4, message = "Senha deve ter 4 dígitos")
        @Pattern(regexp = "\\d{4}", message = "Senha deve conter apenas dígitos")
        String senha) {

        // Valida se o numero cartao eh valido
        public boolean isNumeroCartaoValido(String numeroCartao) {
                return validarLuhn(numeroCartao);
        }

        private boolean validarLuhn(String numeroCartao) {
                int soma = 0;
                boolean digitoVerificado = false;

                for (int i = numeroCartao.length()-1; i>0; i--) {
                        int digito = numeroCartao.charAt(i) - '0';

                        if (digitoVerificado) {
                                digito *= 2;
                                if (digito > 9) {
                                    digito = (digito % 10) + 1;
                                }
                        }

                        soma += digito;
                        digitoVerificado = !digitoVerificado;
                }

                return (soma % 10) == 0;
        }

        // Mascara senha para logs
        public CriarCartaoRequest comSenhaMascarada() {
                return new CriarCartaoRequest(numeroCartao, "****");
        }
}

