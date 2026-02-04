-- Criação das tabelas do sistema de cálculo de conta de água

-- Tabela de tabelas tarifárias
CREATE TABLE IF NOT EXISTS tabela_tarifaria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    data_inicio_vigencia DATE,
    data_fim_vigencia DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'INATIVA'
);

-- Tabela de categorias
CREATE TABLE IF NOT EXISTS categoria (
    id BIGSERIAL PRIMARY KEY,
    tabela_tarifaria_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    FOREIGN KEY (tabela_tarifaria_id) REFERENCES tabela_tarifaria(id) ON DELETE CASCADE
);

-- Tabela de faixas de consumo
CREATE TABLE IF NOT EXISTS faixa_consumo (
    id BIGSERIAL PRIMARY KEY,
    categoria_id BIGINT NOT NULL,
    inicio INTEGER NOT NULL,
    fim INTEGER NOT NULL,
    valor_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX idx_categoria_tabela ON categoria(tabela_tarifaria_id);
CREATE INDEX idx_categoria_tipo ON categoria(tipo);
CREATE INDEX idx_faixa_categoria ON faixa_consumo(categoria_id);
CREATE INDEX idx_tabela_status ON tabela_tarifaria(status);

