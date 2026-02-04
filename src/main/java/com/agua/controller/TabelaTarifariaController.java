package com.agua.controller;

import com.agua.dto.TabelaTarifariaDTO;
import com.agua.dto.TabelaTarifariaResponseDTO;
import com.agua.service.TabelaTarifariaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tabelas-tarifarias")
public class TabelaTarifariaController {
    
    @Autowired
    private TabelaTarifariaService tabelaTarifariaService;
    
    @PostMapping
    public ResponseEntity<TabelaTarifariaResponseDTO> criarTabelaTarifaria(
            @Valid @RequestBody TabelaTarifariaDTO dto) {
        TabelaTarifariaResponseDTO response = tabelaTarifariaService.criarTabelaTarifaria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<TabelaTarifariaResponseDTO>> listarTodas() {
        List<TabelaTarifariaResponseDTO> tabelas = tabelaTarifariaService.listarTodas();
        return ResponseEntity.ok(tabelas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TabelaTarifariaResponseDTO> buscarPorId(@PathVariable Long id) {
        TabelaTarifariaResponseDTO tabela = tabelaTarifariaService.buscarPorId(id);
        return ResponseEntity.ok(tabela);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTabelaTarifaria(@PathVariable Long id) {
        tabelaTarifariaService.excluirTabelaTarifaria(id);
        return ResponseEntity.noContent().build();
    }
}

