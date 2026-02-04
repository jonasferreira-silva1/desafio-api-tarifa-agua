package com.agua.repository;

import com.agua.model.FaixaConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaixaConsumoRepository extends JpaRepository<FaixaConsumo, Long> {
}

