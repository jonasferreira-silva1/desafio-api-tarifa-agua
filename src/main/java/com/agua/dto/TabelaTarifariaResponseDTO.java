package com.agua.dto;

import com.agua.model.enums.StatusTabela;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TabelaTarifariaResponseDTO {
    private Long id;
    private String nome;
    private LocalDate dataInicioVigencia;
    private LocalDate dataFimVigencia;
    private StatusTabela status;
    private List<CategoriaResponseDTO> categorias;
}

