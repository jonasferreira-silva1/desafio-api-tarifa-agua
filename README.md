# 💧 API REST - Sistema de Tabela Tarifária de Água

> **Desafio Técnico RAS 2026** - Solução completa desenvolvida em Java com Spring Boot

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como solução para o **Desafio Técnico RAS 2026 - API de Tabela Tarifária de Água**. A aplicação é uma API REST completa que gerencia tabelas tarifárias de água e realiza cálculos progressivos de consumo baseados em categorias de consumidores e faixas de consumo parametrizáveis.

### 🎯 Objetivo do Desafio

Desenvolver uma API REST para gerenciar e calcular tarifas de água com base em:
- **Categorias de consumidores**: COMERCIAL, INDUSTRIAL, PARTICULAR, PÚBLICO
- **Faixas de consumo progressivas**: Sistema de cobrança por faixas com valores unitários diferentes
- **Parametrização total**: Todas as configurações armazenadas no banco de dados, sem valores fixos no código

---

## ✨ Características Principais

### 🏗️ Arquitetura e Design

- ✅ **Arquitetura em Camadas**: Separação clara entre Controller, Service, Repository e DTOs
- ✅ **Padrões de Projeto**: Implementação de DTOs, Exception Handling centralizado, Validações robustas
- ✅ **Clean Code**: Código limpo, bem documentado e seguindo boas práticas Java/Spring Boot
- ✅ **RESTful API**: Endpoints seguindo convenções REST com códigos HTTP apropriados

### 💾 Parametrização Total

- ✅ **100% Configurável**: Todos os valores (preços, faixas, categorias) armazenados no PostgreSQL
- ✅ **Sem Hardcoding**: Nenhum valor fixo no código - tudo pode ser alterado via banco de dados
- ✅ **Reflexão Automática**: Alterações no banco refletem imediatamente nos cálculos, sem necessidade de alterar código

### 🔢 Cálculo Progressivo

- ✅ **Sistema de Faixas**: Cálculo progressivo onde cada faixa tem valor unitário diferente
- ✅ **Detalhamento Completo**: Retorno JSON detalhado com breakdown por faixa
- ✅ **Precisão Decimal**: Uso de `BigDecimal` para cálculos monetários precisos

### 🛡️ Validações e Regras de Negócio

- ✅ **Validação de Faixas**: Sistema impede gaps, sobreposição e garante cobertura completa
- ✅ **Regras de Consistência**: Validação automática de início < fim, primeira faixa em 0, etc.
- ✅ **Tratamento de Erros**: Exceções customizadas com mensagens claras e códigos HTTP apropriados

### 📊 Funcionalidades Implementadas

- ✅ **CRUD Completo**: Criar, listar, buscar e excluir tabelas tarifárias
- ✅ **Ativação/Desativação**: Endpoints para gerenciar status das tabelas
- ✅ **Cálculo de Consumo**: Endpoint para calcular valor a pagar com detalhamento por faixa
- ✅ **Cadastro em Lote**: Suporte para criação completa de tabelas via JSON

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| **Java** | 17 | Linguagem principal |
| **Spring Boot** | 3.2.0 | Framework web e injeção de dependências |
| **Spring Data JPA** | 3.2.0 | Persistência e acesso a dados |
| **PostgreSQL** | 12+ | Banco de dados relacional |
| **Maven** | 3.6+ | Gerenciamento de dependências |
| **Lombok** | - | Redução de boilerplate |
| **Bean Validation** | - | Validação de dados de entrada |

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.6+**
- **PostgreSQL 12+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code) - opcional

---

## 🚀 Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone <url-do-repositorio>
cd Desafio-Java
```

### 2. Configure o Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE agua_db;
```

### 3. Configure as Credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agua_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

### 4. Execute o Projeto

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A API estará disponível em: **http://localhost:8080**

### 5. (Opcional) Carregar Dados de Exemplo

Execute o script SQL de exemplo:

```bash
psql -U seu_usuario -d agua_db -f src/main/resources/db/exemplos/dados_exemplo.sql
```

Ou use o endpoint POST para criar uma tabela tarifária via JSON (veja exemplos abaixo).

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/agua/
│   │   ├── controller/          # Controllers REST (TabelaTarifariaController, CalculoController)
│   │   ├── dto/                 # Data Transfer Objects (request/response)
│   │   ├── exception/           # Exceções customizadas e GlobalExceptionHandler
│   │   ├── model/               # Entidades JPA
│   │   │   ├── enums/           # Enumeradores (CategoriaConsumidor, StatusTabela)
│   │   │   ├── TabelaTarifaria.java
│   │   │   ├── Categoria.java
│   │   │   └── FaixaConsumo.java
│   │   ├── repository/          # Repositórios JPA
│   │   ├── service/             # Lógica de negócio
│   │   │   ├── TabelaTarifariaService.java
│   │   │   ├── CalculoService.java
│   │   │   └── ValidadorFaixasService.java
│   │   └── CalculadoraAguaApplication.java
│   └── resources/
│       ├── application.properties
│       └── db/
│           ├── migration/        # Scripts de migração (V1__create_tables.sql)
│           └── exemplos/         # Dados e exemplos JSON
└── test/
```

---

## 🔌 Endpoints da API

### 📊 Gerenciamento de Tabelas Tarifárias

#### 1. Criar Tabela Tarifária

```http
POST /api/tabelas-tarifarias
Content-Type: application/json
```

**Request Body:**
```json
{
  "nome": "Tabela Tarifária 2024",
  "dataInicioVigencia": "2024-01-01",
  "dataFimVigencia": "2024-12-31",
  "status": "ATIVA",
  "categorias": [
    {
      "tipo": "COMERCIAL",
      "faixas": [
        {"inicio": 0, "fim": 10, "valorUnitario": 1.50},
        {"inicio": 10, "fim": 20, "valorUnitario": 2.00},
        {"inicio": 20, "fim": 30, "valorUnitario": 2.50},
        {"inicio": 30, "fim": 999999, "valorUnitario": 3.00}
      ]
    },
    {
      "tipo": "INDUSTRIAL",
      "faixas": [
        {"inicio": 0, "fim": 15, "valorUnitario": 2.00},
        {"inicio": 15, "fim": 30, "valorUnitario": 2.75},
        {"inicio": 30, "fim": 50, "valorUnitario": 3.50},
        {"inicio": 50, "fim": 999999, "valorUnitario": 4.00}
      ]
    },
    {
      "tipo": "PARTICULAR",
      "faixas": [
        {"inicio": 0, "fim": 10, "valorUnitario": 1.00},
        {"inicio": 10, "fim": 20, "valorUnitario": 1.50},
        {"inicio": 20, "fim": 30, "valorUnitario": 2.00},
        {"inicio": 30, "fim": 999999, "valorUnitario": 2.50}
      ]
    },
    {
      "tipo": "PUBLICO",
      "faixas": [
        {"inicio": 0, "fim": 20, "valorUnitario": 1.25},
        {"inicio": 20, "fim": 40, "valorUnitario": 1.75},
        {"inicio": 40, "fim": 999999, "valorUnitario": 2.25}
      ]
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "Tabela Tarifária 2024",
  "dataInicioVigencia": "2024-01-01",
  "dataFimVigencia": "2024-12-31",
  "status": "ATIVA",
  "categorias": [...]
}
```

#### 2. Listar Todas as Tabelas

```http
GET /api/tabelas-tarifarias
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Tabela Tarifária 2024",
    "status": "ATIVA",
    ...
  }
]
```

#### 3. Buscar Tabela por ID

```http
GET /api/tabelas-tarifarias/{id}
```

#### 4. Excluir Tabela Tarifária

```http
DELETE /api/tabelas-tarifarias/{id}
```

**⚠️ Importante:** Não é possível excluir uma tabela com status `ATIVA`. Primeiro desative-a.

#### 5. Ativar Tabela Tarifária

```http
PATCH /api/tabelas-tarifarias/{id}/ativar
```

**Funcionalidade:** Ativa a tabela especificada e desativa automaticamente todas as outras.

#### 6. Desativar Tabela Tarifária

```http
PATCH /api/tabelas-tarifarias/{id}/desativar
```

---

### 💰 Cálculo do Valor a Pagar

#### Endpoint de Cálculo

```http
POST /api/calculos
Content-Type: application/json
```

**Request Body:**
```json
{
  "categoria": "INDUSTRIAL",
  "consumo": 18
}
```

**Response (200 OK):**
```json
{
  "categoria": "INDUSTRIAL",
  "consumoTotal": 18,
  "valorTotal": 26.00,
  "detalhamento": [
    {
      "faixa": {
        "inicio": 0,
        "fim": 10
      },
      "m3Cobrados": 10,
      "valorUnitario": 1.00,
      "subtotal": 10.00
    },
    {
      "faixa": {
        "inicio": 11,
        "fim": 20
      },
      "m3Cobrados": 8,
      "valorUnitario": 2.00,
      "subtotal": 16.00
    }
  ]
}
```

**Explicação do Cálculo:**
- **Faixa 1 (0-10 m³)**: 10 m³ × R$ 1,00 = R$ 10,00
- **Faixa 2 (11-20 m³)**: 8 m³ × R$ 2,00 = R$ 16,00
- **Total**: R$ 26,00

---

## 🗄️ Modelagem de Dados

### Diagrama de Relacionamentos

```
TabelaTarifaria (1) ────< (N) Categoria (1) ────< (N) FaixaConsumo
```

### Entidades

#### TabelaTarifaria
- `id`: Long (PK)
- `nome`: String (único, obrigatório)
- `dataInicioVigencia`: LocalDate
- `dataFimVigencia`: LocalDate
- `status`: StatusTabela (ATIVA/INATIVA)
- `categorias`: List<Categoria>

#### Categoria
- `id`: Long (PK)
- `tabelaTarifaria`: TabelaTarifaria (FK)
- `tipo`: CategoriaConsumidor (COMERCIAL, INDUSTRIAL, PARTICULAR, PUBLICO)
- `faixas`: List<FaixaConsumo>

#### FaixaConsumo
- `id`: Long (PK)
- `categoria`: Categoria (FK)
- `inicio`: Integer (obrigatório)
- `fim`: Integer (obrigatório)
- `valorUnitario`: BigDecimal (obrigatório, > 0)

---

## 🔒 Regras de Validação de Faixas

O sistema valida automaticamente que as faixas atendem às seguintes regras:

| Regra | Descrição | Validação |
|-------|-----------|-----------|
| **Não sobreposição** | Faixas não podem ter intervalos que se cruzam | ✅ Implementado |
| **Ordem válida** | Início < Fim | ✅ Implementado |
| **Cobertura completa** | Deve iniciar em 0 (zero) m³ | ✅ Implementado |
| **Cobertura suficiente** | Deve haver faixas que cubram qualquer consumo | ✅ Implementado |
| **Sem gaps** | Não pode haver lacunas entre faixas | ✅ Implementado |

### Exemplo de Validação que Falha

**Request inválido:**
```json
{
  "tipo": "COMERCIAL",
  "faixas": [
    {"inicio": 0, "fim": 10, "valorUnitario": 1.00},
    {"inicio": 15, "fim": 20, "valorUnitario": 2.00}  // ❌ Gap entre 10 e 15
  ]
}
```

**Response (400 Bad Request):**
```json
{
  "erro": "Gap detectado: há uma lacuna entre [0-10] e [15-20]"
}
```

---

## 📊 Exemplos de Uso

### Exemplo 1: Criar Tabela Tarifária Completa

```bash
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d @src/main/resources/db/exemplos/exemplo_json_tabela.json
```

### Exemplo 2: Calcular Conta para Consumo de 18 m³ (Categoria INDUSTRIAL)

```bash
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "INDUSTRIAL",
    "consumo": 18
  }'
```

**Cálculo detalhado:**
- 0 a 10 m³: 10 m³ × R$ 1,00 = R$ 10,00
- 11 a 20 m³: 8 m³ × R$ 2,00 = R$ 16,00
- **Total: R$ 26,00**

### Exemplo 3: Ativar uma Tabela

```bash
curl -X PATCH http://localhost:8080/api/tabelas-tarifarias/1/ativar
```

---

## 🧪 Testando a API

### Usando cURL

```bash
# Criar tabela tarifária
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tabela Teste",
    "status": "ATIVA",
    "categorias": [{
      "tipo": "PARTICULAR",
      "faixas": [
        {"inicio": 0, "fim": 10, "valorUnitario": 1.00},
        {"inicio": 10, "fim": 999999, "valorUnitario": 2.00}
      ]
    }]
  }'

# Calcular conta
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{"categoria": "PARTICULAR", "consumo": 15}'
```

### Usando Postman

1. Importe a coleção (se disponível)
2. Configure a URL base: `http://localhost:8080`
3. Execute as requisições

Para mais exemplos de testes, consulte o arquivo [TESTES.md](TESTES.md).

---

## 🎯 Soluções Implementadas para o Desafio

### ✅ Requisitos Atendidos

| Requisito | Status | Implementação |
|-----------|--------|---------------|
| Criar tabela tarifária completa | ✅ | Endpoint POST com validações |
| Listar todas as tabelas | ✅ | Endpoint GET |
| Excluir tabela tarifária | ✅ | Endpoint DELETE com validação de status |
| Calcular valor correto | ✅ | Cálculo progressivo por faixas |
| Parametrização total | ✅ | Tudo no banco de dados |
| Validações de faixas | ✅ | ValidadorFaixasService |
| Retorno detalhado | ✅ | JSON conforme especificação |
| Cadastro em lote via JSON | ✅ | Suporte completo |

### 🚀 Funcionalidades Extras Implementadas

Além dos requisitos obrigatórios, foram implementadas funcionalidades extras:

- ✅ **Endpoints de Ativação/Desativação**: Gerenciamento de status via API
- ✅ **Validação Automática de Exclusão**: Impede exclusão de tabelas ativas
- ✅ **Garantia de Tabela Única Ativa**: Ao ativar uma tabela, desativa automaticamente as outras
- ✅ **Tratamento de Exceções Centralizado**: GlobalExceptionHandler com mensagens claras
- ✅ **Serialização Customizada**: Categoria "PUBLICO" serializada como "PÚBLICO" no JSON

---

## 🐛 Tratamento de Erros

A API retorna erros padronizados com códigos HTTP apropriados:

| Código | Situação | Exemplo |
|--------|----------|---------|
| **400 Bad Request** | Validação de dados ou regras de negócio | Faixas com gap, tabela ativa sendo excluída |
| **404 Not Found** | Recurso não encontrado | Tabela inexistente, categoria não encontrada |
| **500 Internal Server Error** | Erro interno do servidor | Exceções não tratadas |

**Exemplo de erro:**
```json
{
  "erro": "Não existe uma tabela tarifária ativa. Por favor, ative uma tabela primeiro."
}
```

---

## 📝 Notas Importantes

- ⚠️ **Apenas uma tabela pode estar ATIVA por vez** - Use o endpoint `/ativar` para garantir isso
- ⚠️ **A última faixa deve ter um valor de fim muito alto** (ex: 999999) para cobrir qualquer consumo
- ⚠️ **Não é possível excluir uma tabela ATIVA** - Primeiro desative-a
- ✅ **Todas as alterações no banco refletem imediatamente nos cálculos**
- ✅ **Categoria "PUBLICO" é serializada como "PÚBLICO"** no JSON de resposta

---

## 📚 Documentação Adicional

- **[COMO_ATIVAR_TABELA.md](COMO_ATIVAR_TABELA.md)**: Guia simples para ativar/desativar tabelas
- **[TESTES.md](TESTES.md)**: Exemplos práticos de testes da API
- **[ENDPOINTS_ATIVAR_DESATIVAR.md](ENDPOINTS_ATIVAR_DESATIVAR.md)**: Documentação dos endpoints extras

---

## 🏆 Diferenciais da Solução

### 1. Arquitetura Profissional
- Separação clara de responsabilidades
- Código limpo e bem documentado
- Padrões de projeto aplicados corretamente

### 2. Validações Robustas
- Sistema completo de validação de faixas
- Tratamento de exceções centralizado
- Mensagens de erro claras e descritivas

### 3. Parametrização Total
- Nenhum valor fixo no código
- Tudo configurável via banco de dados
- Alterações refletem automaticamente

### 4. Conformidade 100%
- Todos os requisitos do desafio atendidos
- Formato JSON exatamente conforme especificação
- Cálculos precisos e detalhados

---

## 📄 Licença

Este projeto foi desenvolvido como parte do **Desafio Técnico RAS 2026**.

---

## 👤 Autor Jonas Ferreira da Silva

Desenvolvido como solução para o Desafio Técnico - API de Tabela Tarifária de Água.

---

**🎯 Resumo**: API REST completa, totalmente parametrizável, com validações robustas e cálculos progressivos precisos. Solução profissional desenvolvida para o Desafio Técnico RAS 2026.
