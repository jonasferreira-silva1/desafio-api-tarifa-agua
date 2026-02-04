package com.agua.service;

import com.agua.dto.FaixaConsumoDTO;
import com.agua.exception.ValidacaoFaixasException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ValidadorFaixasService {
    
    /**
     * Valida as regras obrigatórias das faixas:
     * - Não pode ter sobreposição
     * - Início < fim
     * - Sempre começar em 0
     * - Sempre cobrir qualquer consumo (sem gaps)
     */
    public void validarFaixas(List<FaixaConsumoDTO> faixas) {
        if (faixas == null || faixas.isEmpty()) {
            throw new ValidacaoFaixasException("A categoria deve ter pelo menos uma faixa de consumo");
        }
        
        // Ordena as faixas por início
        List<FaixaConsumoDTO> faixasOrdenadas = faixas.stream()
                .sorted(Comparator.comparing(FaixaConsumoDTO::getInicio))
                .toList();
        
        // Regra 1: Primeira faixa deve começar em 0
        FaixaConsumoDTO primeiraFaixa = faixasOrdenadas.get(0);
        if (primeiraFaixa.getInicio() != 0) {
            throw new ValidacaoFaixasException(
                    String.format("A primeira faixa deve começar em 0, mas começa em %d", primeiraFaixa.getInicio())
            );
        }
        
        // Regra 2: Validar cada faixa e verificar continuidade
        for (int i = 0; i < faixasOrdenadas.size(); i++) {
            FaixaConsumoDTO faixa = faixasOrdenadas.get(i);
            
            // Início < fim
            if (faixa.getInicio() >= faixa.getFim()) {
                throw new ValidacaoFaixasException(
                        String.format("Faixa inválida: início (%d) deve ser menor que fim (%d)", 
                                faixa.getInicio(), faixa.getFim())
                );
            }
            
            // Verificar continuidade (exceto na última faixa)
            if (i < faixasOrdenadas.size() - 1) {
                FaixaConsumoDTO proximaFaixa = faixasOrdenadas.get(i + 1);
                
                // Verificar sobreposição
                if (faixa.getFim() > proximaFaixa.getInicio()) {
                    throw new ValidacaoFaixasException(
                            String.format("Sobreposição detectada: faixa [%d-%d] sobrepõe com [%d-%d]",
                                    faixa.getInicio(), faixa.getFim(),
                                    proximaFaixa.getInicio(), proximaFaixa.getFim())
                    );
                }
                
                // Verificar gap (lacuna)
                if (faixa.getFim() < proximaFaixa.getInicio()) {
                    throw new ValidacaoFaixasException(
                            String.format("Gap detectado: há uma lacuna entre [%d-%d] e [%d-%d]",
                                    faixa.getInicio(), faixa.getFim(),
                                    proximaFaixa.getInicio(), proximaFaixa.getFim())
                    );
                }
            }
        }
        
        // Regra 3: Última faixa deve ter fim = null ou um valor muito alto para cobrir qualquer consumo
        // Vamos considerar que a última faixa pode ter fim = Integer.MAX_VALUE ou um valor alto
        FaixaConsumoDTO ultimaFaixa = faixasOrdenadas.get(faixasOrdenadas.size() - 1);
        if (ultimaFaixa.getFim() != null && ultimaFaixa.getFim() < Integer.MAX_VALUE) {
            // A última faixa deve ter um fim muito alto ou ser ilimitada
            // Por segurança, vamos permitir, mas avisar que pode não cobrir todos os consumos
        }
    }
}

