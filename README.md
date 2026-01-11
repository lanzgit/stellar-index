# StellarIndex API 🌟

## 📋 Sobre o Projeto

**StellarIndex API** é uma aplicação Spring Boot que gerencia um catálogo completo de objetos celestes, incluindo estrelas, planetas, luas e asteroides. A API integra-se com a **NASA JPL Small-Body Database** para buscar informações sobre asteroides e objetos próximos à Terra (NEOs).

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 16** - Banco de dados relacional
- **Elasticsearch 8.15.0** - Motor de busca e análise
- **Kibana 8.14.1** - Visualização de dados
- **Logstash 8.15.0** - Pipeline de ingestão de dados
- **Docker & Docker Compose** - Containerização
- **OpenFeign** - Cliente HTTP para integração com APIs externas
- **JWT (jjwt 0.12.5)** - Tokens de autenticação stateless
- **Swagger** - Documentação da API
- **Bean Validation** - Validação de dados

---

## 🏗️ Arquitetura

### Modelo de Domínio

```
Astro (classe abstrata)
├── Planeta
│   └── hasMany: Lua (relacionamento bidirecional)
├── Lua
│   └── belongsTo: Planeta
├── Estrela
│   ├── constelacao: ConstelacaoEnum
│   └── luminosidade: LuminosidadeEnum
└── Asteroide
    └── integracao com NASA SBDB API

Usuario (autenticação)
├── username: String (único)
├── senha: String (BCrypt hash)
└── papel: String (ROLE_USER, ROLE_ADMIN)
```

#### Entidades Principais

- **Estrela**: Representa estrelas com luminosidade e constelação
- **Planeta**: Planetas com gravidade e satélites naturais
- **Lua**: Satélites naturais com distância orbital e relacionamento com planetas
- **Asteroide**: Asteroides com designação, classificação orbital e indicadores NEO/PHA

### Padrões Implementados

- **DTO Pattern**: Separação entre entidades de domínio e objetos de transferência
- **Mapper Pattern**: Conversão entre DTOs e entidades usando classes dedicadas
- **Service Layer**: Lógica de negócio centralizada
- **Repository Pattern**: Acesso a dados via Spring Data JPA
- **ApplicationRunner**: Carregamento inicial de dados via arquivos `.txt` em ordem controlada
- **Global Exception Handler**: Tratamento centralizado de exceções com respostas padronizadas
- **JWT Filter**: Interceptação de requisições para validação de tokens
---

### Inicialização de Dados

A aplicação utiliza **ApplicationRunners** ordenados para carregar dados iniciais:

1. **PlanetaLoader** (@Order(1)) - Carrega planetas de `planeta.txt`
2. **LuaLoader** (@Order(2)) - Carrega luas de `lua.txt`
3. **EstrelaLoader** (@Order(3)) - Carrega estrelas de `estrela.txt`
4. **AsteroideLoader** (@Order(4)) - Carrega asteroides de `asteroide.txt`

---

## 🌐 Integração com NASA API

A aplicação integra-se com a **NASA JPL Small-Body Database (SBDB)** via OpenFeign:

- **Cliente**: `NasaSbdbClient`
- **Serviço**: `NasaCorpoCelesteService`
- **Configuração**: `NasaFeignConfig` e `NasaApiConfig`

### Funcionalidades NASA
- Busca de corpos celestes por designação
- Lista de NEOs (Near-Earth Objects) conhecidos
- Detecção de objetos potencialmente perigosos (PHA)
- Informações sobre classificação orbital e características físicas

---

## 🔍 Elasticsearch, Kibana e Logstash (ELK Stack)

A aplicação utiliza o **ELK Stack** para busca e análise avançada de dados:

- **Elasticsearch**: Motor de busca e análise distribuído
- **Kibana**: Interface de visualização e gerenciamento
- **Logstash**: Pipeline de sincronização de dados do PostgreSQL para Elasticsearch

### Arquitetura de Sincronização

```
┌─────────────┐     ┌──────────────┐     ┌────────────────┐
│  PostgreSQL │────▶│   Logstash   │────▶│ Elasticsearch  │
│  (RDBMS)    │     │  (Pipeline)  │     │   (Search)     │
└─────────────┘     └──────────────┘     └────────────────┘
                                                   │
                                                   ▼
                    ┌──────────────────────────────────┐
                    │    API Spring Boot               │
                    │  EstrelaSearchService            │
                    │  /api/estrelas/search            │
                    └──────────────────────────────────┘
                                   │
                                   ▼
                            ┌─────────────┐
                            │   Cliente   │
                            │ (Frontend)  │
                            └─────────────┘
                                   
                    ┌──────────────┐
                    │    Kibana    │◀─── Análise e
                    │ (Dashboard)  │     Visualização
                    └──────────────┘
```

### Endpoint de Busca

**EstrelaSearchController** - `/api/estrelas/search`

```http
GET /api/estrelas/search?texto=brilhante&page=0&size=10
```

**Funcionalidades**:
- Busca full-text no campo `descricao`
- Busca "**fuzziness**" (tolerância a erros de digitação)
- **Boosting** de relevância (2.0x)
- **Highlighting** dos termos encontrados
- Paginação de resultados

### Como Testar

#### 1. Acessar o Kibana

```
http://localhost:5601
```

#### 2. Dev Tools (Console)

Acesse `Management` → `Dev Tools` e execute:

```json
# Verificar se o índice existe
GET /estrelas

# Contar documentos
GET /estrelas/_count

# Buscar todas as estrelas
GET /estrelas/_search
{
  "size": 10,
  "query": {
    "match_all": {}
  }
}

# Busca por descrição (exemplo de query que a API faz)
GET /estrelas/_search
{
  "from": 0,
  "size": 10,
  "query": {
    "match": {
      "descricao": {
        "query": "brilhante",
        "boost": 2.0,
        "fuzziness": "AUTO"
      }
    }
  },
  "highlight": {
    "fields": {
      "descricao": {
        "pre_tags": ["<em>"],
        "post_tags": ["</em>"]
      }
    }
  },
  "sort": [
    {
      "_score": {
        "order": "desc"
      }
    }
  ]
}
```

#### 3. Testar a API via cURL

```bash
curl "http://localhost:8080/api/estrelas/search?texto=brilhante&page=0&size=10"
```

### Verificar Logs do Logstash

```bash
docker logs -f stellarindex-logstash
```

---

## 🔍 Validações e Tratamento de Erros

### Bean Validation
- **Nome**: Min 2, Max 15 caracteres
- **Descrição**: Min 3, Max 100 caracteres (obrigatória)
- **Campos obrigatórios**: Marcados com `@NotNull` e `@NotBlank`

### Exceções Personalizadas
- `EstrelaNaoEncontradaException`
- `PlanetaNaoEncotradoException`
- `LuaNaoEncontradaException`
- `AstroInvalidoException`

### GlobalExceptionHandler
Tratamento centralizado de exceções em `GlobalExceptionHandler`

---
## 🔐 Segurança e Autenticação JWT

A aplicação implementa **autenticação stateless** usando **JSON Web Tokens (JWT)** com Spring Security.

### Arquitetura de Segurança

```
┌──────────────┐
│   Cliente   │
└──────┬───────┘
       │ POST /api/auth/login
       │ {username, senha}
       ▼
┌──────────────────────────┐
│ AutenticacaoController   │
└──────┬───────────────────┘
       │
       ▼
┌──────────────────────────┐
│  AutenticacaoService     │
│  - Valida credenciais    │
│  - BCrypt hash           │
└──────┬───────────────────┘
       │
       ▼
┌──────────────────────────┐
│    TokenService          │
│  - Gera JWT (HS512)      │
│  - Expira em 1 hora      │
└──────┬───────────────────┘
       │
       ▼ token JWT
┌──────────────┐
│   Cliente   │ Authorization: Bearer {token}
└──────┬───────┘
       │
       ▼
┌──────────────────────────┐
│  FiltroAutenticacao      │
│  - Intercepta requests   │
│  - Valida token          │
│  - Injeta autenticação   │
└──────┬───────────────────┘
       │
       ▼
┌──────────────────────────┐
│  SecurityContext         │
│  - Authentication        │
│  - Authorities (roles)   │
└──────┬───────────────────┘
       │
       ▼
┌──────────────────────────┐
│  @PreAuthorize           │
│  - Verifica roles        │
│  - Permite/Nega acesso   │
└──────────────────────────┘
```

### Componentes de Segurança

#### 1. **TokenService**
- Gera tokens JWT assinados com algoritmo **HS512**
- Extrai `username` e `papel` (role) do token
- Valida assinatura e expiração
- Configurável via `application.properties`:
  - `stellar.jwt.chave-secreta`: Chave de assinatura (mínimo 256 bits)
  - `stellar.jwt.tempo-expiracao`: Tempo de validade em ms (padrão: 1 hora)

#### 2. **FiltroAutenticacao**
- Intercepta todas as requisições HTTP
- Lê header `Authorization: Bearer {token}`
- Valida token e injeta `Authentication` no `SecurityContext`
- Permite que controllers verifiquem autorização via `@PreAuthorize`

#### 3. **SecurityConfig**
- **CSRF desabilitado**: API stateless não precisa
- **Session Policy**: `STATELESS` (sem cookies de sessão)
- **Endpoints públicos**:
  - `/api/auth/**` - Login e registro
  - `/swagger-ui/**`, `/v3/api-docs/**` - Documentação
  - `GET /api/**` - Leitura pública (opcional)
- **Endpoints protegidos por HTTP Method**:
  - `POST /api/**` → `ROLE_USER` ou `ROLE_ADMIN`
  - `PUT /api/**` → `ROLE_USER` ou `ROLE_ADMIN`
  - `PATCH /api/**` → `ROLE_USER` ou `ROLE_ADMIN`
  - `DELETE /api/**` → `ROLE_ADMIN` (apenas administradores)


### Roles e Permissões

| Endpoint | Método | Role Necessária |
|----------|--------|-----------------|
| `/api/auth/**` | Todos | Público |
| `/api/*/search` | GET | Público |
| `/api/**` | GET | Público (opcional) |
| `/api/**` | POST | `ROLE_USER` ou `ROLE_ADMIN` |
| `/api/**` | PUT | `ROLE_USER` ou `ROLE_ADMIN` |
| `/api/**` | PATCH | `ROLE_USER` ou `ROLE_ADMIN` |
| `/api/**` | DELETE | `ROLE_ADMIN` |
| `/swagger-ui/**` | GET | Público |
___

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker & Docker Compose

### Passo a Passo

1. **Clone o repositório**
```bash
git clone https://github.com/lanzgit/stellar-index.git
cd stellarindexapi
```

2. **Inicie os serviços com Docker Compose**
```bash
cd docker
docker-compose up -d
```

Isso iniciará:
- PostgreSQL (porta 5432)
- Elasticsearch (porta 9200)
- Kibana (porta 5601)
- Logstash (porta 9600)

3. **Aguarde os serviços ficarem prontos**
```bash
# Verificar status dos containers
docker ps

# Verificar logs
docker logs stellarindex-postgres
docker logs stellarindex-elasticsearch
docker logs stellarindex-kibana
docker logs stellarindex-logstash
```

**Tempo estimado de inicialização**:
- PostgreSQL: ~10-20s
- Elasticsearch: ~30-40s
- Kibana: ~60s após Elasticsearch
- Logstash: ~90s após Elasticsearch

4. **Execute a aplicação**
```bash
cd ..
mvn spring-boot:run
```

5. **Acesse os serviços**

- **API Swagger**: http://localhost:8080/swagger-ui.html
- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200

6. **Verificar sincronização Elasticsearch**

Acesse o Kibana Dev Tools e execute:
```json
GET /estrelas/_count
```

Se retornar `count: 0`, aguarde até 5 minutos para o Logstash sincronizar os dados.

---

## 🎯 Principais Funcionalidades

✅ **CRUD Completo** para todos os tipos de astros  
✅ **Relacionamento bidirecional** entre Planetas e Luas  
✅ **Validação robusta** de dados com Bean Validation  
✅ **Integração com NASA API** para dados de asteroides  
✅ **Busca full-text com Elasticsearch** - fuzziness, boosting e highlighting  
✅ **ELK Stack completo** - Elasticsearch + Kibana + Logstash (somente no Objeto de `Estrela` para fins acadêmicos) 
✅ **Documentação automática** com Swagger/OpenAPI  
✅ **Containerização** com Docker Compose  
✅ **Tratamento global** de exceções  
✅ **Carga inicial** de dados via arquivos texto  
✅ **Filtros especializados** (constelação, habitabilidade, NEOs) 
✅ **Healthchecks** configurados para todos os serviços Docker 
✅ **Autentição** com JWT

___

## 🤝 Contribuições

Este projeto foi desenvolvido como parte do curso de pós-graduação em Arquitetura Java no INFNET.

**Autor**: Vinicius Vianna  
**Disciplina**: Desenvolvimento Full Stack com React e Spring Boot 
**Instituição**: INFNET  
**Período**: 2025-2026