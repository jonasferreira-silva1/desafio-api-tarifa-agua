-- Script para ativar uma tabela tarifária
-- Substitua o ID pela tabela que você deseja ativar

-- Opção 1: Ativar uma tabela específica por ID
UPDATE tabela_tarifaria 
SET status = 'ATIVA' 
WHERE id = 1;

-- Opção 2: Desativar todas as tabelas e ativar apenas uma específica
-- (Útil para garantir que apenas uma tabela esteja ativa)
BEGIN;
  UPDATE tabela_tarifaria SET status = 'INATIVA';
  UPDATE tabela_tarifaria SET status = 'ATIVA' WHERE id = 1;
COMMIT;

-- Opção 3: Ativar a tabela mais recente
UPDATE tabela_tarifaria 
SET status = 'ATIVA' 
WHERE id = (SELECT id FROM tabela_tarifaria ORDER BY id DESC LIMIT 1);

-- Verificar tabelas ativas
SELECT * FROM tabela_tarifaria WHERE status = 'ATIVA';

