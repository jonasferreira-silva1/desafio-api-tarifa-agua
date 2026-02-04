package com.agua.controller;

import com.agua.dto.CalculoRequestDTO;
import com.agua.dto.CalculoResponseDTO;
import com.agua.service.CalculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculos")
public class CalculoController {
    
    @Autowired
    private CalculoService calculoService;
    
    @PostMapping
    public ResponseEntity<CalculoResponseDTO> calcularConta(
            @Valid @RequestBody CalculoRequestDTO request) {
        CalculoResponseDTO response = calculoService.calcularConta(request);
        return ResponseEntity.ok(response);
    }
}

