package br.com.decio.miniautorizador.repository;

import br.com.decio.miniautorizador.domain.entity.Cartao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String>{
    Optional<Cartao> findByNumeroCartao(String numeroCartao);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c from Cartao c WHERE c.numeroCartao = :numeroCartao")
    Optional<Cartao> findByNumeroCartaoWithOptimisticLock(@Param("numeroCartao") String numeroCartao);

    @Query("SELECT CASE WHEN COUNT(c)> 0 THEN true ELSE false END from Cartao c WHERE c.numeroCartao = :numeroCartao")
    public boolean existsByNumeroCartao(@Param("numeroCartao") String numeroCartao);
}
