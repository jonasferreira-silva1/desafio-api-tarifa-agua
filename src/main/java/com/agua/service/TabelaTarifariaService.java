package com.agua.service;

import com.agua.dto.*;
import com.agua.exception.RecursoNaoEncontradoException;
import com.agua.model.Categoria;
import com.agua.model.FaixaConsumo;
import com.agua.model.TabelaTarifaria;
import com.agua.model.enums.StatusTabela;
import com.agua.repository.TabelaTarifariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TabelaTarifariaService {
    
    @Autowired
    private TabelaTarifariaRepository tabelaTarifariaRepository;
    
    @Autowired
    private ValidadorFaixasService validadorFaixasService;
    
    @Transactional
    public TabelaTarifariaResponseDTO criarTabelaTarifaria(TabelaTarifariaDTO dto) {
        // Validar todas as faixas de todas as categorias
        for (CategoriaDTO categoriaDTO : dto.getCategorias()) {
            validadorFaixasService.validarFaixas(categoriaDTO.getFaixas());
        }
        
        // Criar tabela tarifária
        TabelaTarifaria tabela = new TabelaTarifaria();
        tabela.setNome(dto.getNome());
        tabela.setDataInicioVigencia(dto.getDataInicioVigencia());
        tabela.setDataFimVigencia(dto.getDataFimVigencia());
        tabela.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusTabela.INATIVA);
        
        // Criar categorias e faixas
        for (CategoriaDTO categoriaDTO : dto.getCategorias()) {
            Categoria categoria = new Categoria();
            categoria.setTabelaTarifaria(tabela);
            categoria.setTipo(categoriaDTO.getTipo());
            
            for (FaixaConsumoDTO faixaDTO : categoriaDTO.getFaixas()) {
                FaixaConsumo faixa = new FaixaConsumo();
                faixa.setCategoria(categoria);
                faixa.setInicio(faixaDTO.getInicio());
                faixa.setFim(faixaDTO.getFim());
                faixa.setValorUnitario(faixaDTO.getValorUnitario());
                categoria.getFaixas().add(faixa);
            }
            
            tabela.getCategorias().add(categoria);
        }
        
        TabelaTarifaria salva = tabelaTarifariaRepository.save(tabela);
        return converterParaResponseDTO(salva);
    }
    
    public List<TabelaTarifariaResponseDTO> listarTodas() {
        return tabelaTarifariaRepository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    public TabelaTarifariaResponseDTO buscarPorId(Long id) {
        TabelaTarifaria tabela = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tabela tarifária não encontrada com id: " + id));
        return converterParaResponseDTO(tabela);
    }
    
    @Transactional
    public void excluirTabelaTarifaria(Long id) {
        TabelaTarifaria tabela = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tabela tarifária não encontrada com id: " + id));
        
        // Verificar se é a tabela ativa
        if (tabela.getStatus() == StatusTabela.ATIVA) {
            throw new IllegalStateException("Não é possível excluir uma tabela tarifária ativa");
        }
        
        tabelaTarifariaRepository.delete(tabela);
    }
    
    @Transactional
    public TabelaTarifariaResponseDTO ativarTabela(Long id) {
        TabelaTarifaria tabela = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tabela tarifária não encontrada com id: " + id));
        
        // Desativar todas as outras tabelas (garantir que apenas uma esteja ativa)
        List<TabelaTarifaria> tabelasAtivas = tabelaTarifariaRepository.findAll().stream()
                .filter(t -> t.getStatus() == StatusTabela.ATIVA)
                .collect(Collectors.toList());
        
        for (TabelaTarifaria t : tabelasAtivas) {
            t.setStatus(StatusTabela.INATIVA);
            tabelaTarifariaRepository.save(t);
        }
        
        // Ativar a tabela solicitada
        tabela.setStatus(StatusTabela.ATIVA);
        TabelaTarifaria salva = tabelaTarifariaRepository.save(tabela);
        
        return converterParaResponseDTO(salva);
    }
    
    @Transactional
    public TabelaTarifariaResponseDTO desativarTabela(Long id) {
        TabelaTarifaria tabela = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tabela tarifária não encontrada com id: " + id));
        
        tabela.setStatus(StatusTabela.INATIVA);
        TabelaTarifaria salva = tabelaTarifariaRepository.save(tabela);
        
        return converterParaResponseDTO(salva);
    }
    
    private TabelaTarifariaResponseDTO converterParaResponseDTO(TabelaTarifaria tabela) {
        TabelaTarifariaResponseDTO dto = new TabelaTarifariaResponseDTO();
        dto.setId(tabela.getId());
        dto.setNome(tabela.getNome());
        dto.setDataInicioVigencia(tabela.getDataInicioVigencia());
        dto.setDataFimVigencia(tabela.getDataFimVigencia());
        dto.setStatus(tabela.getStatus());
        
        dto.setCategorias(tabela.getCategorias().stream()
                .map(this::converterCategoriaParaDTO)
                .collect(Collectors.toList()));
        
        return dto;
    }
    
    private CategoriaResponseDTO converterCategoriaParaDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setTipo(categoria.getTipo());
        
        dto.setFaixas(categoria.getFaixas().stream()
                .map(this::converterFaixaParaDTO)
                .collect(Collectors.toList()));
        
        return dto;
    }
    
    private FaixaConsumoResponseDTO converterFaixaParaDTO(FaixaConsumo faixa) {
        FaixaConsumoResponseDTO dto = new FaixaConsumoResponseDTO();
        dto.setId(faixa.getId());
        dto.setInicio(faixa.getInicio());
        dto.setFim(faixa.getFim());
        dto.setValorUnitario(faixa.getValorUnitario());
        return dto;
    }
}

