package com.agua.model;

import com.agua.model.enums.StatusTabela;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tabela_tarifaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TabelaTarifaria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nome;
    
    @Column(name = "data_inicio_vigencia")
    private LocalDate dataInicioVigencia;
    
    @Column(name = "data_fim_vigencia")
    private LocalDate dataFimVigencia;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTabela status = StatusTabela.INATIVA;
    
    @OneToMany(mappedBy = "tabelaTarifaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categoria> categorias = new ArrayList<>();
}

