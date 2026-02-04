-- Script de exemplo com dados para teste
-- Execute este script após criar as tabelas

-- Inserir uma tabela tarifária de exemplo
INSERT INTO tabela_tarifaria (nome, data_inicio_vigencia, data_fim_vigencia, status)
VALUES ('Tabela Tarifária 2024', '2024-01-01', '2024-12-31', 'ATIVA');

-- Obter o ID da tabela inserida (ajuste conforme necessário)
-- Assumindo que o ID é 1

-- Categoria COMERCIAL
INSERT INTO categoria (tabela_tarifaria_id, tipo)
VALUES (1, 'COMERCIAL');

-- Faixas para COMERCIAL
INSERT INTO faixa_consumo (categoria_id, inicio, fim, valor_unitario)
VALUES 
    (1, 0, 10, 1.50),
    (1, 10, 20, 2.00),
    (1, 20, 30, 2.50),
    (1, 30, 999999, 3.00);

-- Categoria INDUSTRIAL
INSERT INTO categoria (tabela_tarifaria_id, tipo)
VALUES (1, 'INDUSTRIAL');

-- Faixas para INDUSTRIAL
INSERT INTO faixa_consumo (categoria_id, inicio, fim, valor_unitario)
VALUES 
    (2, 0, 15, 2.00),
    (2, 15, 30, 2.75),
    (2, 30, 50, 3.50),
    (2, 50, 999999, 4.00);

-- Categoria PARTICULAR
INSERT INTO categoria (tabela_tarifaria_id, tipo)
VALUES (1, 'PARTICULAR');

-- Faixas para PARTICULAR
INSERT INTO faixa_consumo (categoria_id, inicio, fim, valor_unitario)
VALUES 
    (3, 0, 10, 1.00),
    (3, 10, 20, 1.50),
    (3, 20, 30, 2.00),
    (3, 30, 999999, 2.50);

-- Categoria PÚBLICO
INSERT INTO categoria (tabela_tarifaria_id, tipo)
VALUES (1, 'PUBLICO');

-- Faixas para PÚBLICO
INSERT INTO faixa_consumo (categoria_id, inicio, fim, valor_unitario)
VALUES 
    (4, 0, 20, 1.25),
    (4, 20, 40, 1.75),
    (4, 40, 999999, 2.25);

