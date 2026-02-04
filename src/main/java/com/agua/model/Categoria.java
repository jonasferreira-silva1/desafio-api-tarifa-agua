package com.agua.model;

import com.agua.model.enums.CategoriaConsumidor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_tarifaria_id", nullable = false)
    private TabelaTarifaria tabelaTarifaria;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaConsumidor tipo;
    
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("inicio ASC")
    private List<FaixaConsumo> faixas = new ArrayList<>();
}

