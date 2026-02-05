# 🧪 Guia de Testes Rápidos

Este arquivo contém exemplos práticos para testar a API rapidamente.

## Pré-requisitos

1. Banco PostgreSQL rodando
2. API iniciada em `http://localhost:8080`
3. Ferramenta para fazer requisições HTTP (cURL, Postman, Insomnia, etc.)

## Teste 1: Criar Tabela Tarifária Completa

### Requisição

```bash
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tabela Tarifária 2024",
    "dataInicioVigencia": "2024-01-01",
    "dataFimVigencia": "2024-12-31",
    "status": "ATIVA",
    "categorias": [
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
        "tipo": "PUBLICO",
        "faixas": [
          {"inicio": 0, "fim": 20, "valorUnitario": 1.25},
          {"inicio": 20, "fim": 40, "valorUnitario": 1.75},
          {"inicio": 40, "fim": 999999, "valorUnitario": 2.25}
        ]
      }
    ]
  }'
```

### Resposta Esperada (201 Created)

```json
{
  "id": 1,
  "nome": "Tabela Tarifária 2024",
  "status": "ATIVA",
  ...
}
```

## Teste 2: Calcular Conta - Consumo de 18 m³ (PARTICULAR)

### Requisição

```bash
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "PARTICULAR",
    "consumo": 18
  }'
```

### Resposta Esperada

```json
{
  "categoria": "PARTICULAR",
  "consumoTotal": 18,
  "valorTotal": 22.00,
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
        "inicio": 10,
        "fim": 20
      },
      "m3Cobrados": 8,
      "valorUnitario": 1.50,
      "subtotal": 12.00
    }
  ]
}
```

**Verificação:**
- 10 m³ × R$ 1,00 = R$ 10,00
- 8 m³ × R$ 1,50 = R$ 12,00
- **Total: R$ 22,00** ✅

## Teste 3: Calcular Conta - Consumo de 25 m³ (INDUSTRIAL)

### Requisição

```bash
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "INDUSTRIAL",
    "consumo": 25
  }'
```

### Resposta Esperada

```json
{
  "categoria": "INDUSTRIAL",
  "consumoTotal": 25,
  "valorTotal": 57.50,
  "detalhamento": [
    {
      "faixa": {
        "inicio": 0,
        "fim": 15
      },
      "m3Cobrados": 15,
      "valorUnitario": 2.00,
      "subtotal": 30.00
    },
    {
      "faixa": {
        "inicio": 15,
        "fim": 30
      },
      "m3Cobrados": 10,
      "valorUnitario": 2.75,
      "subtotal": 27.50
    }
  ]
}
```

**Verificação:**
- 15 m³ × R$ 2,00 = R$ 30,00
- 10 m³ × R$ 2,75 = R$ 27,50
- **Total: R$ 57,50** ✅

## Teste 4: Validar Erro - Faixas com Gap

### Requisição (deve falhar)

```bash
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tabela Inválida",
    "status": "ATIVA",
    "categorias": [
      {
        "tipo": "PARTICULAR",
        "faixas": [
          {"inicio": 0, "fim": 10, "valorUnitario": 1.00},
          {"inicio": 15, "fim": 20, "valorUnitario": 2.00}
        ]
      }
    ]
  }'
```

### Resposta Esperada (400 Bad Request)

```json
{
  "erro": "Gap detectado: há uma lacuna entre [0-10] e [15-20]"
}
```

## Teste 5: Validar Erro - Faixas Sobrepostas

### Requisição (deve falhar)

```bash
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tabela Inválida 2",
    "status": "ATIVA",
    "categorias": [
      {
        "tipo": "PARTICULAR",
        "faixas": [
          {"inicio": 0, "fim": 15, "valorUnitario": 1.00},
          {"inicio": 10, "fim": 20, "valorUnitario": 2.00}
        ]
      }
    ]
  }'
```

### Resposta Esperada (400 Bad Request)

```json
{
  "erro": "Sobreposição detectada: faixa [0-15] sobrepõe com [10-20]"
}
```

## Teste 6: Validar Erro - Primeira Faixa Não Começa em 0

### Requisição (deve falhar)

```bash
curl -X POST http://localhost:8080/api/tabelas-tarifarias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tabela Inválida 3",
    "status": "ATIVA",
    "categorias": [
      {
        "tipo": "PARTICULAR",
        "faixas": [
          {"inicio": 5, "fim": 10, "valorUnitario": 1.00}
        ]
      }
    ]
  }'
```

### Resposta Esperada (400 Bad Request)

```json
{
  "erro": "A primeira faixa deve começar em 0, mas começa em 5"
}
```

## Teste 7: Listar Todas as Tabelas

### Requisição

```bash
curl -X GET http://localhost:8080/api/tabelas-tarifarias
```

## Teste 8: Buscar Tabela por ID

### Requisição

```bash
curl -X GET http://localhost:8080/api/tabelas-tarifarias/1
```

## Teste 9: Excluir Tabela (deve estar INATIVA)

### Requisição

```bash
curl -X DELETE http://localhost:8080/api/tabelas-tarifarias/1
```

### Resposta Esperada (204 No Content)

## Teste 10: Tentar Excluir Tabela ATIVA (deve falhar)

### Requisição (deve falhar)

```bash
curl -X DELETE http://localhost:8080/api/tabelas-tarifarias/1
```

### Resposta Esperada (400 Bad Request)

```json
{
  "erro": "Não é possível excluir uma tabela tarifária ativa"
}
```

## Teste 11: Calcular sem Tabela Ativa (deve falhar)

### Requisição (deve falhar)

Se não houver tabela ativa:

```bash
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "PARTICULAR",
    "consumo": 10
  }'
```

### Resposta Esperada (404 Not Found)

```json
{
  "erro": "Não existe uma tabela tarifária ativa. Por favor, ative uma tabela primeiro."
}
```

## 📝 Notas

- Todos os valores monetários são retornados com 2 casas decimais
- O consumo é sempre em m³ (metros cúbicos)
- A última faixa deve ter um valor de `fim` muito alto (ex: 999999) para cobrir qualquer consumo
- Apenas uma tabela deve estar ATIVA por vez (recomendado)

