package com.agua.repository;

import com.agua.model.Categoria;
import com.agua.model.TabelaTarifaria;
import com.agua.model.enums.CategoriaConsumidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Optional<Categoria> findByTabelaTarifariaAndTipo(TabelaTarifaria tabelaTarifaria, CategoriaConsumidor tipo);
}

