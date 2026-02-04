package com.agua.dto;

import com.agua.model.enums.CategoriaConsumidor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoResponseDTO {
    private CategoriaConsumidor categoria;
    private BigDecimal consumoTotal;
    private BigDecimal valorTotal;
    private List<DetalheFaixaDTO> detalhesFaixas;
}

