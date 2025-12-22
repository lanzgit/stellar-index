# StellarIndex API 🌟

## 📋 Sobre o Projeto

**StellarIndex API** é uma aplicação Spring Boot que gerencia um catálogo completo de objetos celestes, incluindo estrelas, planetas, luas e asteroides. A API integra-se com a **NASA JPL Small-Body Database** para buscar informações sobre asteroides e objetos próximos à Terra (NEOs).

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 16** - Banco de dados relacional
- **Docker & Docker Compose** - Containerização
- **OpenFeign** - Cliente HTTP para integração com APIs externas
- **Swagger** - Documentação da API
- **Bean Validation** - Validação de dados

---

## 🏗️ Arquitetura

### Modelo de Domínio

A aplicação segue um **modelo hierárquico** com a classe abstrata `Astro` como base:

```
Astro (classe abstrata)
├── Planeta
├── Lua
├── Estrela
└── Asteroide
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
- **ApplicationRunner**: Carregamento inicial de dados via arquivos `.txt`, conforme utilizado na disciplina anterior
---

## 📁 Estrutura do Projeto

```
src/main/java/br/edu/infnet/stellarindexapi/
├── config/                    # Configurações (Swagger, Feign, NASA API)
├── controller/                # Controllers REST
│   └── exception/            # Tratamento global de exceções
├── model/
│   ├── domain/               # Entidades JPA
│   │   └── exceptions/       # Exceções personalizadas
│   ├── dto/                  # Data Transfer Objects
│   ├── enums/                # Enumerações (Constelação, Luminosidade)
│   ├── repository/           # Interfaces JPA Repository
│   ├── service/              # Camada de serviços
│   └── clients/              # Clientes Feign para APIs externas
├── *Loader.java              # Carregadores de dados iniciais
└── StellarindexapiApplication.java
```

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

2. **Inicie o PostgreSQL**
```bash
cd docker
docker-compose up -d
```

3. **Execute a aplicação**
```bash
cd ..
mvn spring-boot:run
```

4. **Acesse a documentação Swagger**
```
http://localhost:8080/swagger-ui.html
```
---

## 🎯 Principais Funcionalidades

✅ **CRUD Completo** para todos os tipos de astros  
✅ **Relacionamento bidirecional** entre Planetas e Luas  
✅ **Validação robusta** de dados com Bean Validation  
✅ **Integração com NASA API** para dados de asteroides  
✅ **Documentação automática** com Swagger/OpenAPI  
✅ **Containerização** com Docker Compose  
✅ **Tratamento global** de exceções  
✅ **Carga inicial** de dados via arquivos texto  
✅ **Filtros especializados** (constelação, habitabilidade, NEOs)  