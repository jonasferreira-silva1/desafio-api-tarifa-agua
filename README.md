# 💧 API REST - Calculadora de Conta de Água

API REST desenvolvida em Java com Spring Boot para cálculo de contas de água com base em categorias de consumidores e faixas de consumo progressivas. **Tudo é configurável via banco de dados PostgreSQL** - nenhum valor fixo no código!

## 📋 Índice

- [Características](#-características)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Endpoints da API](#-endpoints-da-api)
- [Modelagem de Dados](#-modelagem-de-dados)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Validações de Faixas](#-validações-de-faixas)

## ✨ Características

- ✅ **100% Configurável**: Todos os valores (preços, faixas, categorias) são armazenados no banco de dados
- ✅ **Cálculo Progressivo**: Sistema de faixas de consumo onde cada parte é cobrada com valores diferentes
- ✅ **Múltiplas Categorias**: Suporte para COMERCIAL, INDUSTRIAL, PARTICULAR e PÚBLICO
- ✅ **Validações Robustas**: Sistema valida que as faixas não se sobrepõem, não têm gaps e sempre começam em 0
- ✅ **Retorno Detalhado**: O cálculo retorna o detalhamento completo de cada faixa cobrada
- ✅ **API REST Completa**: CRUD de tabelas tarifárias e endpoint de cálculo

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**
- **Bean Validation**

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 12+
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd Desafio-Java
```

### 2. Configure o banco de dados PostgreSQL

Crie um banco de dados:

```sql
CREATE DATABASE agua_db;
```

### 3. Configure as credenciais do banco

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agua_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4. Execute o projeto

```bash
mvn clean install
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### 5. (Opcional) Carregar dados de exemplo

Execute o script SQL de exemplo:

```bash
psql -U seu_usuario -d agua_db -f src/main/resources/db/exemplos/dados_exemplo.sql
```

Ou use o endpoint POST para criar uma tabela tarifária via JSON (veja exemplos abaixo).

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/agua/
│   │   ├── controller/          # Controllers REST
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Exceções customizadas
│   │   ├── model/               # Entidades JPA
│   │   │   └── enums/           # Enumeradores
│   │   ├── repository/          # Repositórios JPA
│   │   ├── service/             # Lógica de negócio
│   │   └── CalculadoraAguaApplication.java
│   └── resources/
│       ├── application.properties
│       └── db/
│           ├── migration/        # Scripts de migração
│           └── exemplos/        # Dados e exemplos
└── test/
```

## 🔌 Endpoints da API

### 1. Gerenciar Tabelas Tarifárias

#### Criar Tabela Tarifária
```http
POST /api/tabelas-tarifarias
Content-Type: application/json
```

**Body:**
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
        {
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 1.50
        },
        {
          "inicio": 10,
          "fim": 20,
          "valorUnitario": 2.00
        },
        {
          "inicio": 20,
          "fim": 30,
          "valorUnitario": 2.50
        },
        {
          "inicio": 30,
          "fim": 999999,
          "valorUnitario": 3.00
        }
      ]
    }
  ]
}
```

**Resposta (201 Created):**
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

#### Listar Todas as Tabelas
```http
GET /api/tabelas-tarifarias
```

#### Buscar Tabela por ID
```http
GET /api/tabelas-tarifarias/{id}
```

#### Excluir Tabela Tarifária
```http
DELETE /api/tabelas-tarifarias/{id}
```

**Nota:** Não é possível excluir uma tabela que esteja com status ATIVA.

### 2. Calcular Conta de Água

```http
POST /api/calculos
Content-Type: application/json
```

**Body:**
```json
{
  "categoria": "INDUSTRIAL",
  "consumo": 18
}
```

**Resposta (200 OK):**
```json
{
  "categoria": "INDUSTRIAL",
  "consumoTotal": 18.00,
  "valorTotal": 38.25,
  "detalhesFaixas": [
    {
      "inicio": 0,
      "fim": 15,
      "consumoNaFaixa": 15.00,
      "valorUnitario": 2.00,
      "subtotal": 30.00
    },
    {
      "inicio": 15,
      "fim": 30,
      "consumoNaFaixa": 3.00,
      "valorUnitario": 2.75,
      "subtotal": 8.25
    }
  ]
}
```

## 🗄️ Modelagem de Dados

### Entidades

#### TabelaTarifaria
- `id`: Identificador único
- `nome`: Nome da tabela tarifária
- `dataInicioVigencia`: Data de início da vigência
- `dataFimVigencia`: Data de fim da vigência
- `status`: ATIVA ou INATIVA
- `categorias`: Lista de categorias associadas

#### Categoria
- `id`: Identificador único
- `tabelaTarifaria`: Referência à tabela tarifária
- `tipo`: COMERCIAL, INDUSTRIAL, PARTICULAR ou PUBLICO
- `faixas`: Lista de faixas de consumo

#### FaixaConsumo
- `id`: Identificador único
- `categoria`: Referência à categoria
- `inicio`: Início da faixa (m³)
- `fim`: Fim da faixa (m³)
- `valorUnitario`: Valor por m³ nesta faixa

### Relacionamentos

```
TabelaTarifaria (1) ────< (N) Categoria (1) ────< (N) FaixaConsumo
```

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
- 0 a 15 m³: 15 m³ × R$ 2,00 = R$ 30,00
- 15 a 30 m³: 3 m³ × R$ 2,75 = R$ 8,25
- **Total: R$ 38,25**

### Exemplo 3: Calcular Conta para Consumo de 25 m³ (Categoria PARTICULAR)

```bash
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "PARTICULAR",
    "consumo": 25
  }'
```

**Cálculo detalhado:**
- 0 a 10 m³: 10 m³ × R$ 1,00 = R$ 10,00
- 10 a 20 m³: 10 m³ × R$ 1,50 = R$ 15,00
- 20 a 30 m³: 5 m³ × R$ 2,00 = R$ 10,00
- **Total: R$ 35,00**

## 🔒 Validações de Faixas

O sistema valida automaticamente que as faixas de consumo atendem às seguintes regras:

1. ✅ **Sempre começar em 0**: A primeira faixa deve começar em 0
2. ✅ **Sem sobreposição**: Faixas não podem se sobrepor
3. ✅ **Sem gaps**: Não pode haver lacunas entre faixas
4. ✅ **Início < Fim**: O início deve ser menor que o fim
5. ✅ **Cobertura completa**: Todas as faixas juntas devem cobrir qualquer consumo possível

### Exemplo de Validação que Falha

```json
{
  "tipo": "COMERCIAL",
  "faixas": [
    {"inicio": 0, "fim": 10, "valorUnitario": 1.00},
    {"inicio": 15, "fim": 20, "valorUnitario": 2.00}  // ❌ Gap entre 10 e 15
  ]
}
```

**Erro retornado:**
```json
{
  "erro": "Gap detectado: há uma lacuna entre [0-10] e [15-20]"
}
```

## 🧪 Testando a API

### Usando cURL

```bash
# Criar tabela tarifária
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tabela Teste",
    "status": "ATIVA",
    "categorias": [
      {
        "tipo": "PARTICULAR",
        "faixas": [
          {"inicio": 0, "fim": 10, "valorUnitario": 1.00},
          {"inicio": 10, "fim": 999999, "valorUnitario": 2.00}
        ]
      }
    ]
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

## 📝 Notas Importantes

- ⚠️ **Apenas uma tabela pode estar ATIVA por vez** (não há validação automática, mas é recomendado)
- ⚠️ **A última faixa deve ter um valor de fim muito alto** (ex: 999999) para cobrir qualquer consumo
- ⚠️ **Não é possível excluir uma tabela ATIVA** - primeiro defina como INATIVA
- ✅ **Todas as alterações no banco refletem imediatamente nos cálculos**

## 🐛 Tratamento de Erros

A API retorna erros padronizados:

- **400 Bad Request**: Validação de dados ou regras de negócio
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

Exemplo de erro:
```json
{
  "erro": "Não existe uma tabela tarifária ativa. Por favor, ative uma tabela primeiro."
}
```

## 📄 Licença

Este projeto foi desenvolvido como parte de um desafio técnico.

## 👤 Autor

Desenvolvido para o Desafio Java - API de Cálculo de Conta de Água

---

**🎯 Resumo**: API REST totalmente configurável via banco de dados para cálculo de contas de água com faixas progressivas por categoria, validações robustas e retorno detalhado dos cálculos.

