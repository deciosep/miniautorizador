package br.com.decio.miniautorizador.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @Column(name = "numero_cartao", length = 16, nullable = false)
    @NotBlank(message = "Número do cartão não pode estar vazio")
    @Size(min = 16, max = 16, message = "Número do cartão deve ter 16 dígitos")
    private String numeroCartao;

    @Column(name = "senhaCartao", length = 4, nullable = false)
    @NotBlank(message = "Senha não pode estar vazio")
    @Size(min = 4, max = 4, message = "Senha deve ter 4 dígitos")
    private String senha;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "Saldo não pode ser nulo")
    @DecimalMin(value = "0.00", message = "Saldo não pode ser negativo")
    private BigDecimal saldo;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Cartao() {
    }

    public Cartao(String numeroCartao, String senha) {
        this.numeroCartao = numeroCartao;
        this.senha = senha;
        this.saldo = new BigDecimal("500.00");
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void debitarSaldo(BigDecimal valor){
        this.saldo = this.saldo.subtract(valor);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean possuiSaldoSuficiene(BigDecimal valor){
        return this.saldo.compareTo(valor) >= 0;
    }

    public boolean senhaCorreta(String senha){
        return this.senha.equals(senha);
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getSenha() {
        return senha;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Long getVersion() {
        return version;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cartao cartao = (Cartao) o;
        return Objects.equals(numeroCartao, cartao.numeroCartao);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numeroCartao);
    }

    @Override
    public String toString() {
        return "Cartao{" +
                "numeroCartao='" + numeroCartao + '\'' +
                ", valor=" + saldo +
                '}';
    }
}
