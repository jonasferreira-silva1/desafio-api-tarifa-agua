package com.agua.dto;

import com.agua.model.enums.CategoriaConsumidor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {
    private Long id;
    private CategoriaConsumidor tipo;
    private List<FaixaConsumoResponseDTO> faixas;
}

