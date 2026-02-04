package com.agua.dto;

import com.agua.model.enums.CategoriaConsumidor;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoRequestDTO {
    
    @NotNull(message = "Categoria é obrigatória")
    private CategoriaConsumidor categoria;
    
    @NotNull(message = "Consumo é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Consumo deve ser maior que zero")
    private BigDecimal consumo;
}

