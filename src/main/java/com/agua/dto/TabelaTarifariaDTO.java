package com.agua.dto;

import com.agua.model.enums.StatusTabela;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TabelaTarifariaDTO {
    
    @NotNull(message = "Nome da tabela é obrigatório")
    private String nome;
    
    private LocalDate dataInicioVigencia;
    
    private LocalDate dataFimVigencia;
    
    private StatusTabela status = StatusTabela.INATIVA;
    
    @NotEmpty(message = "A tabela deve ter pelo menos uma categoria")
    @Valid
    private List<CategoriaDTO> categorias;
}

