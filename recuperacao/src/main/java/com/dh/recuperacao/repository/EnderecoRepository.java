package com.dh.recuperacao.repository;

import com.dh.recuperacao.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}