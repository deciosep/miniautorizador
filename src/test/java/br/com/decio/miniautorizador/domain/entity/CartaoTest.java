package br.com.decio.miniautorizador.domain.entity;

import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.util.GenerateData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartaoTest {
    @Mock
    BigDecimal saldo;
    @InjectMocks
    Cartao cartao;

    @BeforeEach
    void setUp() {
        cartao = GenerateData.dadosCartao();
    }

    @Test
    void testOnUpdate() {
        cartao.onUpdate();
    }

    @Test
    void deveCriarCartaoComSaldoInicial() {
        Assertions.assertEquals(cartao.getSaldo(), new BigDecimal("500.00"));
    }

    @Test
    void deveDebitarSaldoCorretamente() {
        BigDecimal valorDebito = new BigDecimal("100.00");

        cartao.debitarSaldo(valorDebito);

        Assertions.assertEquals(cartao.getSaldo(), new BigDecimal("400.00"));
    }

    @Test
    void deveVerificarSaldoSuficiene() {
        Assertions.assertEquals(true, cartao.possuiSaldoSuficiene(new BigDecimal(100)));
        Assertions.assertEquals(true, cartao.possuiSaldoSuficiene(new BigDecimal(400)));
        Assertions.assertEquals(false, cartao.possuiSaldoSuficiene(new BigDecimal(600)));
    }

    @Test
    void deveVerificarSenhaCorretamente() {
        Assertions.assertEquals(true, cartao.senhaCorreta("1234"));
        Assertions.assertEquals(false, cartao.senhaCorreta("123"));
    }

    @Test
    void deveImplementarEqualsHashCode() {
        Cartao cartao1 = new Cartao("1234567890123456", "1234");
        Cartao cartao2 = new Cartao("1234567890123456", "5678");
        Cartao cartao3 = new Cartao("9876543210987654", "1234");

        // When & Then
        assertThat(cartao1).isEqualTo(cartao2); // Mesmo número
        assertThat(cartao1).isNotEqualTo(cartao3); // Número diferente
        assertThat(cartao1.hashCode()).isEqualTo(cartao2.hashCode());
    }

    @Test
    void deveImplementarToString() {
        Cartao cartao1 = new Cartao("1234567890425099", "1234");
        String result = cartao1.toString();
        Assertions.assertEquals("Cartao{numeroCartao='1234567890425099', valor=500.00}", result);
    }
}