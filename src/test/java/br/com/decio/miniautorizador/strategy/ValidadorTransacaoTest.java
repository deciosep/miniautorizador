package br.com.decio.miniautorizador.strategy;

import br.com.decio.miniautorizador.domain.dto.ResultadoAutorizacao;
import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;
import br.com.decio.miniautorizador.util.GenerateData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ValidadorTransacaoTest {
    private ValidadorTransacao validadorTransacao;
    private TransacaoRequest transacaoRequest;
    private Cartao cartao;

    @BeforeEach
    void setUp() {
        List<ValidacaoStrategy> estrategias = List.of(
                new ValidacaoCartaoExistenteStrategy(),
                new ValidacaoSenhaStrategy(),
                new ValidacaoSaldoStrategy()
        );
        validadorTransacao = new ValidadorTransacao(estrategias);
        cartao = GenerateData.dadosCartao();
        transacaoRequest = GenerateData.dadosTransacao(cartao.getNumeroCartao(), cartao.getSenha(), new BigDecimal("100.00"));
    }

    @Test
    void deveAutorizarQdoTodasPassam() {
        ResultadoAutorizacao result = validadorTransacao.validar(transacaoRequest, cartao);
        Assertions.assertEquals(ResultadoAutorizacao.aprovada(), result);
    }
    @Test
    void deveRejeitarQdoCartaoNaoExistente() {
        ResultadoAutorizacao result = validadorTransacao.validar(transacaoRequest, null);
        Assertions.assertEquals(ResultadoAutorizacao.rejeitada(MotivoRejeicao.CARTAO_INEXISTENTE), result);
    }
    @Test
    void deveRejeitarQdoSaldoInsuficiente() {
        TransacaoRequest transacaoValorSaldoAlto = GenerateData.dadosTransacao(cartao.getNumeroCartao(), cartao.getSenha(), new BigDecimal("100000.00"));
        ResultadoAutorizacao result = validadorTransacao.validar(transacaoValorSaldoAlto, cartao);
        Assertions.assertEquals(ResultadoAutorizacao.rejeitada(MotivoRejeicao.SALDO_INSUFICIENTE), result);
    }
}
