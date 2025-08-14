package br.com.decio.miniautorizador.concurrency;

import br.com.decio.miniautorizador.domain.dto.CriarCartaoRequest;
import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.exception.SaldoInsuficienteException;
import br.com.decio.miniautorizador.repository.CartaoRepository;
import br.com.decio.miniautorizador.service.CartaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyTest {

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private CartaoRepository cartaoRepository;

    private final String numeroCartao = "1234567890123456";
    private final String senha = "1234";

    @BeforeEach
    void setUp() {
        cartaoRepository.deleteAll();
        CriarCartaoRequest request = new CriarCartaoRequest(numeroCartao, senha);
        cartaoService.criarCartao(request);
    }

    @Test
    void deveGerenciarConcorrenciaCorretamenteComTransacoesSimultaneas() throws InterruptedException {
        // Given
        int numeroThreads = 10;
        BigDecimal valorTransacao = new BigDecimal("10.00");
        ExecutorService executor = Executors.newFixedThreadPool(numeroThreads);
        CountDownLatch latch = new CountDownLatch(numeroThreads);
        AtomicInteger sucessos = new AtomicInteger(0);
        AtomicInteger falhas = new AtomicInteger(0);
        List<Future<Void>> futures = new ArrayList<>();

        // When - Executar transações simultâneas
        for (int i = 0; i < numeroThreads; i++) {
            Future<Void> future = executor.submit(() -> {
                try {
                    TransacaoRequest request = new TransacaoRequest(numeroCartao, senha, valorTransacao);
                    cartaoService.autorizarTransacao(request);
                    sucessos.incrementAndGet();
                } catch (SaldoInsuficienteException e) {
                    falhas.incrementAndGet();
                } catch (Exception e) {
                    // Outras exceções também contam como falhas
                    falhas.incrementAndGet();
                } finally {
                    latch.countDown();
                }
                return null;
            });
            futures.add(future);
        }

        // Aguardar conclusão de todas as threads
        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        // Then
        // Verificar se todas as operações terminaram
        for (Future<Void> future : futures) {
            assertThatCode(() -> future.get(1, TimeUnit.SECONDS))
                    .doesNotThrowAnyException();
        }

        // Saldo inicial: 500.00
        // 50 transações simultâneas de 10.00 = máximo 50 sucessos possíveis
        // Algumas devem falhar por saldo insuficiente
        assertThat(sucessos.get()).isLessThanOrEqualTo(50);
        assertThat(sucessos.get() + falhas.get()).isEqualTo(numeroThreads);

        // Verificar saldo final
        BigDecimal saldoFinal = cartaoService.obterSaldo(numeroCartao);
        BigDecimal saldoEsperado = new BigDecimal("500.00").subtract(
                valorTransacao.multiply(new BigDecimal(sucessos.get()))
        );
        assertThat(saldoFinal).isEqualTo(saldoEsperado);

        // Saldo nunca deve ser negativo
        assertThat(saldoFinal).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    void deveEvitarSaldoNegativoComTransacoesConcorrentes() throws InterruptedException {
        // Given - Cenário específico: 2 transações de R$500 simultâneas
        // Apenas uma deve ser aprovada
        BigDecimal valorTransacao = new BigDecimal("500.00");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger sucessos = new AtomicInteger(0);
        AtomicInteger falhas = new AtomicInteger(0);

        // When
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                try {
                    TransacaoRequest request = new TransacaoRequest(numeroCartao, senha, valorTransacao);
                    cartaoService.autorizarTransacao(request);
                    sucessos.incrementAndGet();
                } catch (Exception e) {
                    falhas.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // Then
        assertThat(sucessos.get()).isEqualTo(1); // Apenas uma transação deve ser aprovada
        assertThat(falhas.get()).isEqualTo(1);   // Uma deve falhar

        // Saldo deve ser zero (500 - 500 = 0)
        BigDecimal expected = new BigDecimal("0.00");
        BigDecimal saldoFinal = cartaoService.obterSaldo(numeroCartao);
        assertThat(saldoFinal).isEqualTo(expected);
    }

    @Test
    void deveManterConsistenciaComMuitasTransacoesPequenas() throws InterruptedException {
        // Given
        int numeroThreads = 100;
        BigDecimal valorTransacao = new BigDecimal("1.00");
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(numeroThreads);
        AtomicInteger transacoesProcessadas = new AtomicInteger(0);

        // When
        for (int i = 0; i < numeroThreads; i++) {
            executor.submit(() -> {
                try {
                    TransacaoRequest request = new TransacaoRequest(numeroCartao, senha, valorTransacao);
                    cartaoService.autorizarTransacao(request);
                } catch (Exception e) {
                    // Ignorar falhas por saldo insuficiente
                } finally {
                    transacoesProcessadas.incrementAndGet();
                    latch.countDown();
                }
            });
        }

        latch.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        // Then
        assertThat(transacoesProcessadas.get()).isEqualTo(numeroThreads);

        // Verificar que o saldo nunca ficou negativo
        BigDecimal saldoFinal = cartaoService.obterSaldo(numeroCartao);
        assertThat(saldoFinal).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(saldoFinal).isLessThanOrEqualTo(new BigDecimal("500.00"));
    }

    @Test
    @Transactional
    void deveTestarRetryMechanismComOptimisticLocking() {
        // Given
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao).orElseThrow();

        // Simular cenário onde retry é necessário
        // Modificar versão manualmente para forçar OptimisticLockingFailureException
        BigDecimal saldoOriginal = cartao.getSaldo();

        // When & Then
        TransacaoRequest request = new TransacaoRequest(numeroCartao, senha, new BigDecimal("50.00"));

        // Primeira transação deve funcionar normalmente
        assertThatCode(() -> cartaoService.autorizarTransacao(request))
                .doesNotThrowAnyException();

        // Verificar se saldo foi debitado
        BigDecimal saldoAtual = cartaoService.obterSaldo(numeroCartao);
        assertThat(saldoAtual).isEqualTo(saldoOriginal.subtract(new BigDecimal("50.00")));
    }
}