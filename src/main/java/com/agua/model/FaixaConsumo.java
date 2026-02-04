package com.agua.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "faixa_consumo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaixaConsumo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @Column(nullable = false)
    @NotNull(message = "Início da faixa é obrigatório")
    private Integer inicio;
    
    @Column(nullable = false)
    @NotNull(message = "Fim da faixa é obrigatório")
    private Integer fim;
    
    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Valor unitário é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor unitário deve ser maior que zero")
    private BigDecimal valorUnitario;
}

