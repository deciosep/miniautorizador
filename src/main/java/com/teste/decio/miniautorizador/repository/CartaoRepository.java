package com.teste.decio.miniautorizador.repository;

import com.teste.decio.miniautorizador.domain.entity.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String>{

}
