-- ============================================
-- Script Rápido: Ativar Tabela Tarifária
-- ============================================
-- 
-- INSTRUÇÕES:
-- 1. Substitua o número 1 pelo ID da tabela que você quer ativar
-- 2. Execute este script no seu banco PostgreSQL
-- 3. Verifique se funcionou com: SELECT * FROM tabela_tarifaria WHERE status = 'ATIVA';
--
-- ============================================

-- Primeiro, veja todas as tabelas disponíveis
SELECT id, nome, status FROM tabela_tarifaria;

-- Desativar todas as tabelas (garantir que apenas uma esteja ativa)
UPDATE tabela_tarifaria SET status = 'INATIVA';

-- Ativar a tabela desejada (SUBSTITUA O ID AQUI 👇)
UPDATE tabela_tarifaria SET status = 'ATIVA' WHERE id = 1;

-- Verificar se está ativa
SELECT id, nome, status, data_inicio_vigencia, data_fim_vigencia 
FROM tabela_tarifaria 
WHERE status = 'ATIVA';

