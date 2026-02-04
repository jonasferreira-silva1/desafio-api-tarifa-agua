package com.agua.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaixaConsumoDTO {
    
    @NotNull(message = "Início da faixa é obrigatório")
    private Integer inicio;
    
    @NotNull(message = "Fim da faixa é obrigatório")
    private Integer fim;
    
    @NotNull(message = "Valor unitário é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor unitário deve ser maior que zero")
    private BigDecimal valorUnitario;
}

