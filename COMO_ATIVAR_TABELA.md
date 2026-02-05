# 🔧 Como Ativar uma Tabela Tarifária

Guia rápido e simples para ativar/desativar tabelas tarifárias no sistema.

---

## 🎯 Por que Preciso Ativar uma Tabela?

Para realizar cálculos de consumo, o sistema precisa saber **qual tabela tarifária usar**. Apenas tabelas com status `ATIVA` são utilizadas nos cálculos.

---

## ✅ Método Rápido: Via API (Recomendado)

### Passo 1: Ver todas as tabelas disponíveis

```bash
curl -X GET http://localhost:8080/api/tabelas-tarifarias
```

**Exemplo de resposta:**
```json
[
  {
    "id": 1,
    "nome": "Tabela Tarifária 2024",
    "status": "INATIVA"
  },
  {
    "id": 2,
    "nome": "Tabela Tarifária 2025",
    "status": "INATIVA"
  }
]
```

### Passo 2: Ativar a tabela desejada

Substitua `1` pelo ID da tabela que você quer ativar:

```bash
curl -X PATCH http://localhost:8080/api/tabelas-tarifarias/1/ativar
```

**O que acontece automaticamente:**
- ✅ A tabela escolhida é ativada
- ✅ Todas as outras tabelas são desativadas automaticamente
- ✅ Garante que apenas uma tabela esteja ativa por vez

**Resposta:**
```json
{
  "id": 1,
  "nome": "Tabela Tarifária 2024",
  "status": "ATIVA",
  ...
}
```

### Passo 3: Verificar se está funcionando

Teste um cálculo:

```bash
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "PARTICULAR",
    "consumo": 15
  }'
```

Se retornar o cálculo, está funcionando! ✅

---

## ❌ Desativar uma Tabela

```bash
curl -X PATCH http://localhost:8080/api/tabelas-tarifarias/1/desativar
```

**⚠️ Atenção:** Se você desativar a única tabela ativa, os cálculos vão falhar até você ativar outra.

---

## 🔍 Verificar Qual Tabela Está Ativa

### Via API

```bash
curl -X GET http://localhost:8080/api/tabelas-tarifarias
```

Procure pela tabela com `"status": "ATIVA"` na resposta.

### Via SQL (Alternativa)

```sql
SELECT id, nome, status 
FROM tabela_tarifaria 
WHERE status = 'ATIVA';
```

---

## ⚠️ Problemas Comuns e Soluções

### Erro: "Não existe uma tabela tarifária ativa"

**Causa:** Nenhuma tabela está ativa.

**Solução:**
```bash
# 1. Liste as tabelas
curl -X GET http://localhost:8080/api/tabelas-tarifarias

# 2. Ative uma tabela (substitua 1 pelo ID)
curl -X PATCH http://localhost:8080/api/tabelas-tarifarias/1/ativar
```

### Erro: "Categoria X não encontrada na tabela tarifária ativa"

**Causa:** A tabela ativa não tem a categoria que você está tentando calcular.

**Solução:**
1. Verifique quais categorias a tabela ativa possui
2. Crie uma nova tabela com todas as categorias necessárias
3. Ative a nova tabela

---

## 📋 Exemplo Completo Passo a Passo

```bash
# 1. Ver todas as tabelas
curl -X GET http://localhost:8080/api/tabelas-tarifarias

# 2. Ativar a tabela com ID 1
curl -X PATCH http://localhost:8080/api/tabelas-tarifarias/1/ativar

# 3. Verificar se está ativa
curl -X GET http://localhost:8080/api/tabelas-tarifarias/1

# 4. Testar cálculo
curl -X POST http://localhost:8080/api/calculos \
  -H "Content-Type: application/json" \
  -d '{
    "categoria": "PARTICULAR",
    "consumo": 15
  }'
```

---

## 💡 Dica Importante

**Sempre use o endpoint `/ativar`** em vez de criar tabelas já ativas. Ele garante que apenas uma tabela esteja ativa por vez, evitando conflitos nos cálculos.

---

## 📝 Resumo Rápido

| Ação | Comando |
|------|---------|
| Ver todas as tabelas | `GET /api/tabelas-tarifarias` |
| Ativar tabela | `PATCH /api/tabelas-tarifarias/{id}/ativar` |
| Desativar tabela | `PATCH /api/tabelas-tarifarias/{id}/desativar` |
| Ver tabela específica | `GET /api/tabelas-tarifarias/{id}` |

---

**Pronto!** Agora você sabe como ativar e desativar tabelas. 🚀
