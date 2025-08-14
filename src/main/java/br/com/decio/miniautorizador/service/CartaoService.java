package br.com.decio.miniautorizador.service;

import br.com.decio.miniautorizador.domain.dto.CriarCartaoRequest;
import br.com.decio.miniautorizador.domain.dto.CriarCartaoResponse;
import br.com.decio.miniautorizador.domain.dto.ResultadoAutorizacao;
import br.com.decio.miniautorizador.domain.dto.TransacaoRequest;
import br.com.decio.miniautorizador.domain.entity.Cartao;
import br.com.decio.miniautorizador.exception.CartaoJaExisteException;
import br.com.decio.miniautorizador.exception.CartaoNaoEncontradoException;
import br.com.decio.miniautorizador.exception.ConcorrenciaException;
import br.com.decio.miniautorizador.repository.CartaoRepository;
import br.com.decio.miniautorizador.strategy.ExceptionFactory;
import br.com.decio.miniautorizador.strategy.ValidadorTransacao;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CartaoService {
    private final CartaoRepository cartaoRepository;
    private final ValidadorTransacao validadorTransacao;
    private final ExceptionFactory exceptionFactory;

    public CartaoService(CartaoRepository cartaoRepository, ValidadorTransacao validadorTransacao, ExceptionFactory exceptionFactory) {
        this.cartaoRepository = cartaoRepository;
        this.validadorTransacao = validadorTransacao;
        this.exceptionFactory = exceptionFactory;
    }
    public CriarCartaoResponse criarCartao(CriarCartaoRequest request) {
        return Optional.of(request.numeroCartao())
                .filter(numero -> !cartaoRepository.existsByNumeroCartao(numero))
                .map(numero -> new Cartao(numero, request.senha()))
                .map(cartaoRepository::save)
                .map(cartao -> new CriarCartaoResponse(cartao.getNumeroCartao(), cartao.getSenha()))
                .orElseThrow(() -> new CartaoJaExisteException(request.numeroCartao()));
    }

    @Transactional(readOnly = true)
    public BigDecimal obterSaldo(String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(Cartao::getSaldo)
                .orElseThrow(() -> new CartaoNaoEncontradoException(numeroCartao));
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class, ConcorrenciaException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void autorizarTransacao(TransacaoRequest request){
        try{
            Cartao cartao = cartaoRepository.findByNumeroCartaoWithOptimisticLock(request.numeroCartao())
                    .orElse(null);

            ResultadoAutorizacao resultado = validadorTransacao.validar(request, cartao);

            Optional.of(resultado)
                    .filter(r -> r.autorizada())
                    .map( r -> cartao)
                    .ifPresentOrElse(
                            c -> {
                                c.debitarSaldo(request.valor());
                                cartaoRepository.save(c);
                            },
                            () -> {
                                throw exceptionFactory.criarException(resultado.motivo());
                            }
                    );

        }catch (OptimisticLockingFailureException e){
            throw new ConcorrenciaException("Erro de concorrência ao processar a transação", e);
        }
    }
}
