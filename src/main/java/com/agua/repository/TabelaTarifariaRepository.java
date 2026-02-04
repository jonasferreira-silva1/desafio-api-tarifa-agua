package com.agua.repository;

import com.agua.model.TabelaTarifaria;
import com.agua.model.enums.StatusTabela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TabelaTarifariaRepository extends JpaRepository<TabelaTarifaria, Long> {
    
    Optional<TabelaTarifaria> findByStatus(StatusTabela status);
    
    @Query("SELECT t FROM TabelaTarifaria t WHERE t.status = :status ORDER BY t.dataInicioVigencia DESC")
    Optional<TabelaTarifaria> findAtiva(StatusTabela status);
}

