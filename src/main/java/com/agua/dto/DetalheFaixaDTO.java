package com.agua.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalheFaixaDTO {
    private FaixaDTO faixa;
    private Integer m3Cobrados;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaixaDTO {
        private Integer inicio;
        private Integer fim;
    }
}

