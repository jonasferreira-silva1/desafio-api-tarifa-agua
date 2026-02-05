package com.agua.service;

import com.agua.dto.*;
import com.agua.exception.RecursoNaoEncontradoException;
import com.agua.model.Categoria;
import com.agua.model.FaixaConsumo;
import com.agua.model.TabelaTarifaria;
import com.agua.model.enums.StatusTabela;
import com.agua.repository.CategoriaRepository;
import com.agua.repository.TabelaTarifariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculoService {
    
    @Autowired
    private TabelaTarifariaRepository tabelaTarifariaRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public CalculoResponseDTO calcularConta(CalculoRequestDTO request) {
        // Buscar tabela tarifária ativa
        TabelaTarifaria tabelaAtiva = tabelaTarifariaRepository.findAtiva(StatusTabela.ATIVA)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Não existe uma tabela tarifária ativa. Por favor, ative uma tabela primeiro."));
        
        // Buscar categoria
        Categoria categoria = categoriaRepository.findByTabelaTarifariaAndTipo(tabelaAtiva, request.getCategoria())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        String.format("Categoria %s não encontrada na tabela tarifária ativa", request.getCategoria())));
        
        // Calcular valor progressivo
        BigDecimal consumo = request.getConsumo();
        Integer consumoTotalInt = consumo.intValue();
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<DetalheFaixaDTO> detalhamento = new ArrayList<>();
        
        List<FaixaConsumo> faixas = categoria.getFaixas();
        for (int i = 0; i < faixas.size(); i++) {
            FaixaConsumo faixa = faixas.get(i);
            boolean isUltimaFaixa = (i == faixas.size() - 1);
            
            // Determinar quanto do consumo cai nesta faixa
            BigDecimal consumoNaFaixa = calcularConsumoNaFaixa(consumo, faixa, isUltimaFaixa);
            
            if (consumoNaFaixa.compareTo(BigDecimal.ZERO) > 0) {
                // Calcular subtotal desta faixa
                BigDecimal subtotal = consumoNaFaixa.multiply(faixa.getValorUnitario())
                        .setScale(2, RoundingMode.HALF_UP);
                
                valorTotal = valorTotal.add(subtotal);
                
                // Adicionar detalhe conforme especificação da documentação
                DetalheFaixaDTO detalhe = new DetalheFaixaDTO();
                
                // Criar objeto faixa aninhado
                DetalheFaixaDTO.FaixaDTO faixaDTO = new DetalheFaixaDTO.FaixaDTO();
                faixaDTO.setInicio(faixa.getInicio());
                faixaDTO.setFim(faixa.getFim());
                detalhe.setFaixa(faixaDTO);
                
                // m3Cobrados deve ser Integer (quantidade de m³ cobrados)
                detalhe.setM3Cobrados(consumoNaFaixa.intValue());
                detalhe.setValorUnitario(faixa.getValorUnitario());
                detalhe.setSubtotal(subtotal);
                detalhamento.add(detalhe);
            }
        }
        
        // Montar resposta conforme especificação da documentação
        CalculoResponseDTO response = new CalculoResponseDTO();
        response.setCategoria(request.getCategoria());
        response.setConsumoTotal(consumoTotalInt);
        response.setValorTotal(valorTotal.setScale(2, RoundingMode.HALF_UP));
        response.setDetalhamento(detalhamento);
        
        return response;
    }
    
    /**
     * Calcula quanto do consumo total cai dentro de uma faixa específica.
     * Exemplo: consumo = 18, faixa [0-10] retorna 10, faixa [11-20] retorna 8
     * 
     * @param consumoTotal Consumo total em m³
     * @param faixa Faixa de consumo
     * @param isUltimaFaixa Se true, trata a faixa como ilimitada quando o consumo ultrapassa o fim
     */
    private BigDecimal calcularConsumoNaFaixa(BigDecimal consumoTotal, FaixaConsumo faixa, boolean isUltimaFaixa) {
        int inicio = faixa.getInicio();
        int fim = faixa.getFim() != null ? faixa.getFim() : Integer.MAX_VALUE;
        
        // Se o consumo total é menor que o início da faixa, não cai nesta faixa
        if (consumoTotal.compareTo(BigDecimal.valueOf(inicio)) < 0) {
            return BigDecimal.ZERO;
        }
        
        // Se o consumo total é maior ou igual ao fim da faixa
        if (consumoTotal.compareTo(BigDecimal.valueOf(fim)) >= 0) {
            // Se é a última faixa, cobrar o excedente também (faixa ilimitada)
            if (isUltimaFaixa) {
                return consumoTotal.subtract(BigDecimal.valueOf(inicio));
            }
            // Caso contrário, apenas toda a faixa é cobrada
            return BigDecimal.valueOf(fim - inicio);
        }
        
        // Caso contrário, apenas parte da faixa é cobrada
        return consumoTotal.subtract(BigDecimal.valueOf(inicio));
    }
}

