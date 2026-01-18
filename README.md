# StellarIndex API 🌟

## 📋 Sobre o Projeto

**StellarIndex API** é uma aplicação full-stack que gerencia um catálogo completo de objetos celestes, incluindo estrelas, planetas, luas e asteroides. O sistema é composto por uma API REST Spring Boot integrada com a **NASA JPL Small-Body Database** e um frontend Next.js.

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 16** - Banco de dados relacional
- **Elasticsearch 8.15.0** - Motor de busca e análise
- **Kibana 8.14.1** - Visualização de dados
- **Logstash 8.15.0** - Pipeline de ingestão de dados
- **OpenFeign** - Cliente HTTP para integração com APIs externas
- **JWT (jjwt 0.12.5)** - Tokens de autenticação stateless
- **Swagger/OpenAPI** - Documentação da API
- **Bean Validation** - Validação de dados

### Frontend
- **Next.js 16.1.3** 
- **React 19.2.3**
- **TypeScript 5**
- **Tailwind CSS 4**
- **Axios 1.13.2** - Cliente HTTP
- **Material Symbols** - Ícones

### Infraestrutura
- **Docker & Docker Compose** - Containerização e orquestração
- **Node.js 20-alpine**

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

A aplicação utiliza o **ELK Stack** para busca e análise de dados:

- **Elasticsearch**: Motor de busca e análise distribuído
- **Kibana**: Interface de visualização e gerenciamento
- **Logstash**: Pipeline de sincronização de dados do PostgreSQL para Elasticsearch
*Obs: a busca é realizada somente no índice `estrelas`(para fins acadêmicos) do Elasticsearch para buscar por descrição*

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
                            │  Frontend   │
                            │  Next.js    │
                            └─────────────┘
                                   
                    ┌──────────────┐
                    │    Kibana    │◀─── Análise e
                    │ (Dashboard)  │     Visualização
                    └──────────────┘
```

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

---

## 🔍 Validações e Tratamento de Erros

### Bean Validation (Backend)
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

### Validações Frontend
- Validação client-side antes de enviar dados
- Feedback visual de erros
- Mensagens de erro contextuais
- Verificação de autenticação e permissões

---

## 🔐 Segurança e Autenticação JWT

A aplicação implementa **autenticação stateless** usando **JSON Web Tokens (JWT)** com Spring Security.

### Arquitetura de Segurança

```
┌──────────────┐
│   Frontend   │
│   (Next.js)  │
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
       ▼ token JWT (localStorage)
┌──────────────┐
│   Frontend   │ Authorization: Bearer {token}
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

### Componentes de Segurança (Backend)

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
  - `GET /api/**` - Leitura pública
- **Endpoints protegidos por HTTP Method**:
  - `POST /api/**` → `ROLE_USER` ou `ROLE_ADMIN`
  - `PUT /api/**` → `ROLE_USER` ou `ROLE_ADMIN`
  - `PATCH /api/**` → `ROLE_USER` ou `ROLE_ADMIN`
  - `DELETE /api/**` → `ROLE_ADMIN` (apenas administradores)

### Componentes de Segurança (Frontend)

#### 1. **AuthService**
- Gerencia login e registro de usuários
- Armazena token JWT no `localStorage`
- Verifica autenticação do usuário

#### 2. **API Interceptor**
- Adiciona automaticamente token JWT em todas as requisições
- Redireciona para login em caso de token expirado (401)
- Trata erros de permissão (403)
- Timeout de 20 segundos para requisições

#### 3. **Protected Routes**
- Header verifica autenticação e exibe opções adequadas
- Redirecionamento automático para login se não autenticado
- Controle de acesso baseado em roles (exibição de botões)

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

---

## 🎨 Frontend - Interface do Usuário

### Estrutura do Projeto Frontend

```
frontend/
├── src/
│   ├── app/                    # Páginas Next.js (App Router)
│   │   ├── auth/               # Autenticação
│   │   │   ├── login/          # Página de login
│   │   │   └── register/       # Página de registro
│   │   ├── asteroides/         # CRUD de asteroides
│   │   ├── estrelas/           # CRUD de estrelas + busca Elasticsearch
│   │   ├── luas/               # CRUD de luas
│   │   ├── planetas/           # CRUD de planetas
│   │   ├── layout.tsx          # Layout principal
│   │   └── page.tsx            # Página inicial
│   ├── components/             # Componentes reutilizáveis
│   │   ├── common/             # Componentes genéricos (Modal)
│   │   └── layout/             # Header e Footer
│   ├── services/               # Serviços de comunicação com API
│   │   ├── api.ts              # Configuração do Axios
│   │   ├── authService.ts      # Serviço de autenticação
│   │   ├── asteroideService.ts
│   │   ├── estrelaService.ts
│   │   ├── luaService.ts
│   │   └── planetaService.ts
│   ├── types/                  # Tipos TypeScript
│   │   ├── asteroide.ts
│   │   ├── auth.ts
│   │   ├── estrela.ts
│   │   ├── lua.ts
│   │   ├── planeta.ts
│   │   ├── index.ts
│   │   └── enums/              # Enumerações
│   └── utils/
│       └── constants.ts        # Constantes (rotas, endpoints)
├── public/                     # Arquivos estáticos
├── Dockerfile                  # Containerização
├── package.json                # Dependências
└── tsconfig.json               # Configuração TypeScript
```

### Funcionalidades do Frontend

#### 🏠 Página Inicial
- Dashboard com links para todos os módulos

#### 🔐 Autenticação
- **Login**: Formulário com validação
- **Registro**: Cadastro de novos usuários (USER ou ADMIN)
- **Validações**

### Design e UX

- **Tailwind CSS**: Estilização
- **Material Symbols**: Ícones
- **Feedback Visual**:
  - Estados de loading
  - Mensagens de erro contextuais
  - Confirmações de ação
  - Contador de caracteres

---

## 🐳 Docker e Containerização

### Arquitetura de Containers

A aplicação utiliza **Docker Compose** para orquestrar 6 serviços:

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                       │
│                 stellarindex-network                    │
│                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │  PostgreSQL  │  │Elasticsearch │  │   Kibana     │   │
│  │    :5432     │  │    :9200     │  │    :5601     │   │
│  └──────┬───────┘  └──────┬───────┘  └──────────────┘   │
│         │                 │                             │
│         │                 │                             │
│  ┌──────▼───────┐  ┌──────▼───────┐                     │
│  │  Logstash    │  │  Spring Boot │                     │
│  │    :9600     │  │    :8080     │                     │
│  └──────────────┘  └──────┬───────┘                     │
│                           │                             │
│                     ┌─────▼───────┐                     │
│                     │   Next.js   │                     │
│                     │    :3000    │                     │
│                     └─────────────┘                     │
└─────────────────────────────────────────────────────────┘
```

### Serviços Docker

#### 1. **PostgreSQL** (`stellarindex-postgres`)
- **Imagem**: `postgres:16-alpine`
- **Porta**: 5432
- **Variáveis de Ambiente**:
  - `POSTGRES_DB=stellarindexdb`
  - `POSTGRES_USER=stellarindex`
  - `POSTGRES_PASSWORD=stellarindex123`
- **Volume**: `postgres_data` (persistência de dados)
- **Healthcheck**: `pg_isready` a cada 10s

#### 2. **Elasticsearch** (`stellarindex-elasticsearch`)
- **Imagem**: `docker.elastic.co/elasticsearch/elasticsearch:8.15.0`
- **Portas**: 9200 (HTTP), 9300 (Transport)
- **Configurações**:
  - Modo single-node
  - Segurança desabilitada (desenvolvimento)
  - Heap: 512MB
- **Healthcheck**: `curl http://localhost:9200/_cluster/health`

#### 3. **Kibana** (`stellarindex-kibana`)
- **Imagem**: `docker.elastic.co/kibana/kibana:8.14.1`
- **Porta**: 5601
- **Depende de**: Elasticsearch
- **Healthcheck**: `curl http://localhost:5601/api/status`

#### 4. **Logstash** (`stellarindex-logstash`)
- **Imagem**: `docker.elastic.co/logstash/logstash:8.15.0`
- **Porta**: 9600 (API)
- **Configuração**: Pipeline de sincronização PostgreSQL → Elasticsearch
- **Pipeline**: Executa a cada 1 minuto
- **Arquivos**:
  - `docker/logstash/pipeline/estrelas.conf` - Configuração do pipeline
  - Driver PostgreSQL JDBC incluído
- **Healthcheck**: `curl http://localhost:9600`

#### 5. **Spring Boot API** (`stellarindex-api`)
- **Build**: Multi-stage com Maven
- **Porta**: 8080
- **Variáveis de Ambiente**:
  - Conexão PostgreSQL
  - Conexão Elasticsearch
  - Configurações JPA
- **Depende de**: PostgreSQL, Elasticsearch, Logstash
- **Healthcheck**: Spring Actuator `/actuator/health`
- **Tempo de inicialização**: ~60s

#### 6. **Next.js Frontend** (`stellarindex-frontend`)
- **Build**: Multi-stage com Node.js 20-alpine
- **Porta**: 3000
- **Variáveis de Ambiente**:
  - `NEXT_PUBLIC_API_URL=http://localhost:8080/api`
- **Depende de**: API Spring Boot
- **Healthcheck**: `wget http://localhost:3000`
- **Modo**: Standalone (otimizado para produção)

### Dockerfiles

#### Backend Dockerfile (`docker/Dockerfile`)
```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Frontend Dockerfile (`frontend/Dockerfile`)
```dockerfile
# Deps
FROM node:20-alpine AS deps
WORKDIR /app
COPY package*.json ./
RUN npm ci

# Builder
FROM node:20-alpine AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
ENV NEXT_TELEMETRY_DISABLED=1
RUN npm run build

# Runner
FROM node:20-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production
RUN addgroup --system nodejs && adduser --system nextjs
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
USER nextjs
EXPOSE 3000
CMD ["node", "server.js"]
```

### Volumes e Persistência

```yaml
volumes:
  postgres_data:  # Persiste dados do PostgreSQL
```

### Network

```yaml
networks:
  stellarindex-network:
    driver: bridge  # Rede isolada para todos os containers
```

### Healthchecks

Todos os serviços possuem healthchecks configurados:

| Serviço | Endpoint | Intervalo | Timeout | Retries |
|---------|----------|-----------|---------|---------|
| PostgreSQL | `pg_isready` | 10s | 5s | 5 |
| Elasticsearch | `/_cluster/health` | 10s | 5s | 5 |
| Kibana | `/api/status` | 10s | 5s | 10 |
| Logstash | `:9600` | 10s | 5s | 10 |
| API | `/actuator/health` | 30s | 10s | 5 |
| Frontend | `:3000` | 30s | 10s | 3 |

---

## 🚀 Como Executar

### Pré-requisitos
- **Java 17+**
- **Maven 3.6+**
- **Node.js 20+** (para desenvolvimento frontend)
- **Docker & Docker Compose**

### Opção 1: Executar com Docker (Recomendado)

1. **Clone o repositório**
```bash
git clone https://github.com/lanzgit/stellar-index.git
cd stellarindexapi
```

2. **Inicie todos os serviços com Docker Compose**
```bash
cd docker
docker-compose up -d
```

3. **Aguarde os serviços ficarem prontos**
```bash
# Verificar status dos containers
docker-compose ps

# Verificar logs
docker-compose logs -f
```

**Tempo estimado de inicialização**:
- PostgreSQL: ~10-20s
- Elasticsearch: ~30-40s
- Kibana: ~60s após Elasticsearch
- Logstash: ~90s após Elasticsearch
- Spring Boot API: ~60s após PostgreSQL e Elasticsearch
- Frontend Next.js: ~40s após API

4. **Acesse os serviços**
- **Frontend**: http://localhost:3000
- **API Swagger**: http://localhost:8080/swagger-ui.html
- **Kibana**: http://localhost:5601
- **Elasticsearch**: http://localhost:9200

### Opção 2: Executar em Modo Desenvolvimento

#### Backend

1. **Inicie apenas a infraestrutura**
```bash
cd docker
docker-compose up -d postgres elasticsearch kibana logstash
```

2. **Execute a aplicação Spring Boot**
```bash
cd ..
mvn spring-boot:run
```

#### Frontend

1. **Instale as dependências**
```bash
cd frontend
npm install
```

2. **Configure a variável de ambiente**
```bash
# Crie um arquivo .env.local
echo "NEXT_PUBLIC_API_URL=http://localhost:8080" > .env.local
```

3. **Execute o servidor de desenvolvimento**
```bash
npm run dev
```

4. **Acesse**: http://localhost:3000

### Verificar Sincronização Elasticsearch

Acesse o Kibana Dev Tools (http://localhost:5601) e execute:
```json
GET /estrelas/_count
```

Se retornar `count: 0`, aguarde até 5 minutos para o Logstash sincronizar os dados.

---

## 🎯 Principais Funcionalidades

### Backend
✅ **CRUD Completo** para todos os tipos de astros  
✅ **Relacionamento bidirecional** entre Planetas e Luas  
✅ **Validação robusta** de dados com Bean Validation  
✅ **Integração com NASA API** para dados de asteroides  
✅ **Busca full-text com Elasticsearch** - fuzziness e boosting   
✅ **ELK Stack completo** - Elasticsearch + Kibana + Logstash  
✅ **Documentação automática** com Swagger/OpenAPI  
✅ **Tratamento global** de exceções  
✅ **Carga inicial** de dados via arquivos texto  
✅ **Filtros especializados** (constelação, habitabilidade, NEOs)  
✅ **Autenticação JWT** com roles e permissões  

### Frontend
✅ **Interface** com Tailwind CSS  
✅ **Autenticação** (login e registro)  
✅ **CRUD para todos os módulos** com modais  
✅ **Busca Elasticsearch** integrada no módulo de estrelas  
✅ **Validação client-side** em tempo real  
✅ **Feedback visual** de erros e loading states  
✅ **Type-safe** com TypeScript  
✅ **Paginação** de resultados no módulo de estrelas  
✅ **Relacionamentos** entre planetas e luas  
✅ **Indicadores visuais** para propriedades especiais (NEO, PHA, habitável)  

### Infraestrutura
✅ **Containerização completa** com Docker Compose  
✅ **Healthchecks** configurados para todos os serviços  
✅ **Persistência de dados** com volumes Docker  
✅ **Network isolada** para comunicação entre containers  
✅ **Build multi-stage** otimizado para produção  
✅ **Logs centralizados** com Docker Compose  

---

## 📚 Documentação da API

### Swagger/OpenAPI

Acesse a documentação interativa em:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints Principais

#### Autenticação
- `POST /api/auth/login` - Login
- `POST /api/auth/registrar` - Registro

#### Estrelas
- `GET /api/estrelas` - Listar (paginado)
- `POST /api/estrela` - Criar
- `PUT /api/estrela/{id}` - Atualizar
- `DELETE /api/estrela/{id}` - Excluir
- `GET /api/estrelas/search` - Busca Elasticsearch

#### Planetas
- `GET /api/planetas` - Listar
- `POST /api/planeta` - Criar
- `PUT /api/planeta/{id}` - Atualizar
- `DELETE /api/planeta/{id}` - Excluir

#### Luas
- `GET /api/luas` - Listar
- `POST /api/lua` - Criar
- `PUT /api/lua/{id}` - Atualizar
- `DELETE /api/lua/{id}` - Excluir

#### Asteroides
- `GET /api/asteroides` - Listar
- `POST /api/asteroide` - Criar
- `PUT /api/asteroide/{id}` - Atualizar
- `DELETE /api/asteroide/{id}` - Excluir

#### NASA API
- `GET /api/nasa/corpos-celeste/{designacao}` - Buscar por designação

---

## 🤝 Contribuições

Este projeto foi desenvolvido como parte do curso de pós-graduação em Arquitetura Java no INFNET.

**Autor**: Vinicius Vianna  
**Disciplinas**:
  - Desenvolvimento Avançado com Spring e Microsserviços [25E3-25E3]
  - Desenvolvimento Full Stack com React e Spring Boot [25E4-25E4]

**Instituição**: INFNET  
**Período**: 2025-2026

---

## 📝 Licença

Este projeto é de uso acadêmico.