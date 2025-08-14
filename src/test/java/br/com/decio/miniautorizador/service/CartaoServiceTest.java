package br.com.decio.miniautorizador.service;

import br.com.decio.miniautorizador.domain.dto.CriarCartaoRequest;
import br.com.decio.miniautorizador.domain.dto.CriarCartaoResponse;
import br.com.decio.miniautorizador.domain.dto.ResultadoAutorizacao;
import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.domain.enums.MotivoRejeicao;
import br.com.decio.miniautorizador.exception.*;
import br.com.decio.miniautorizador.repository.CartaoRepository;
import br.com.decio.miniautorizador.strategy.ExceptionFactory;
import br.com.decio.miniautorizador.strategy.ValidadorTransacao;
import br.com.decio.miniautorizador.util.GenerateData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {
    @Mock
    CartaoRepository cartaoRepository;
    @Mock
    ValidadorTransacao validadorTransacao;
    @Mock
    ExceptionFactory exceptionFactory;
    @InjectMocks
    CartaoService cartaoService;

    private CriarCartaoRequest criarCartaoRequest;
    private TransacaoRequest transacaoRequest;
    private Cartao cartao;

    @BeforeEach
    void setUp() {
        cartao = GenerateData.dadosCartao();
        criarCartaoRequest = GenerateData.dadosNovoCartao(cartao.getNumeroCartao(), cartao.getSenha());
        transacaoRequest = GenerateData.dadosTransacao(cartao.getNumeroCartao(), cartao.getSenha(), new BigDecimal("100.00"));
    }

    @Test
    void deveCriarCartaoComSucesso() {
        when(cartaoRepository.existsByNumeroCartao(criarCartaoRequest.numeroCartao())).thenReturn(false);
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);

        CriarCartaoResponse result = cartaoService.criarCartao(criarCartaoRequest);

        Assertions.assertEquals(criarCartaoRequest.numeroCartao(), result.numeroCartao());
        verify(cartaoRepository).existsByNumeroCartao(cartao.getNumeroCartao());
        verify(cartaoRepository).save(any(Cartao.class));
    }

    @Test
    void deveLancarExcecaoQuandoCartaoJaExiste() {
        when(cartaoRepository.existsByNumeroCartao(anyString())).thenReturn(true);

        assertThatThrownBy(()-> cartaoService.criarCartao(criarCartaoRequest))
                .isInstanceOf(CartaoJaExisteException.class);

        verify(cartaoRepository, never()).save(any(Cartao.class));
    }

    @Test
    void deveObterSaldo() {
        Cartao expextedCartao = cartao;
        String expectedNrCartao = expextedCartao.getNumeroCartao();

        when(cartaoRepository.findByNumeroCartao(expectedNrCartao)).thenReturn(Optional.of(expextedCartao));

        BigDecimal result = cartaoService.obterSaldo(expectedNrCartao);

        Assertions.assertEquals(expextedCartao.getSaldo(), result);
    }

    @Test
    void deveLancarExcecaoQuandoCartaoNaoEncontradoParaSaldo() {
        Cartao expextedCartao = cartao;
        String expectedNrCartao = expextedCartao.getNumeroCartao();

        when(cartaoRepository.findByNumeroCartao(expectedNrCartao)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> cartaoService.obterSaldo(expectedNrCartao))
                .isInstanceOf(CartaoNaoEncontradoException.class);
    }

    @Test
    void deveAutorizarTransacaoComSuces() {
        TransacaoRequest expectedTransacaoRequest = GenerateData.dadosTransacao(cartao.getNumeroCartao(), cartao.getSenha(), new BigDecimal("0.00"));

        when(cartaoRepository.findByNumeroCartaoWithOptimisticLock(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);
        when(validadorTransacao.validar(expectedTransacaoRequest, cartao)).thenReturn(ResultadoAutorizacao.aprovada());

        cartaoService.autorizarTransacao(expectedTransacaoRequest);
    }

    @Test
    void deveRejeitarTransacaoQunadoValidacaoFalha() {
        TransacaoRequest expectedTransacaoRequest = GenerateData.dadosTransacao(cartao.getNumeroCartao(), cartao.getSenha(), new BigDecimal("0.00"));

        when(cartaoRepository.findByNumeroCartaoWithOptimisticLock(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));
        when(validadorTransacao.validar(expectedTransacaoRequest, cartao))
                .thenReturn(ResultadoAutorizacao.rejeitada( MotivoRejeicao.SALDO_INSUFICIENTE));
        when(exceptionFactory.criarException(any())).thenReturn(new SaldoInsuficienteException());

        assertThatThrownBy(()-> cartaoService.autorizarTransacao(expectedTransacaoRequest))
                .isInstanceOf(SaldoInsuficienteException.class);

        verify(cartaoRepository, never()).save(any(Cartao.class));
    }

    @Test
    void deveTratarErroDeOtimizacaoInvalida() {
        when(cartaoRepository.findByNumeroCartaoWithOptimisticLock(anyString()))
                .thenThrow(new OptimisticLockingFailureException("Erro de concorrência"));
        assertThatThrownBy(()-> cartaoService.autorizarTransacao(transacaoRequest))
                .isInstanceOf(ConcorrenciaException.class);
    }


}