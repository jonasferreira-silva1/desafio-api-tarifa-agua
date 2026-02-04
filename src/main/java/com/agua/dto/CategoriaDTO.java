package com.agua.dto;

import com.agua.model.enums.CategoriaConsumidor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    
    @NotNull(message = "Tipo de categoria é obrigatório")
    private CategoriaConsumidor tipo;
    
    @NotEmpty(message = "A categoria deve ter pelo menos uma faixa de consumo")
    @Valid
    private List<FaixaConsumoDTO> faixas;
}

