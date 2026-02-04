package com.agua.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalheFaixaDTO {
    private Integer inicio;
    private Integer fim;
    private BigDecimal consumoNaFaixa;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
}

