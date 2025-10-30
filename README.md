# 🧩 ConnectFood - Core Service

**Core Service** é o módulo responsável por **usuários e autenticação** do sistema **ConnectFood**, desenvolvido como parte da Pós-Tech FIAP em Arquitetura e Desenvolvimento Java.  
O serviço implementa **arquitetura hexagonal (ports & adapters)**, usa **Spring Boot 3.5.6**, **PostgreSQL**, **JWT**, **Flyway** e **OpenAPI/Swagger** para documentação e contrato.

---

## ⚙️ Stack e Tecnologias

| Categoria | Tecnologias |
|------------|--------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.6 |
| Banco de dados | PostgreSQL 16 |
| Versionamento de schema | Flyway |
| Segurança | Spring Security + JWT |
| Documentação | SpringDoc OpenAPI 3.0.4 |
| Build | Maven 3.9+ |
| Containerização | Docker / Docker Compose |

---

## 🧱 Arquitetura

O projeto segue **arquitetura hexagonal**, isolando regras de negócio da infraestrutura:

```
Entrypoint (REST Controllers)
    ↓
Application (Use Cases, Mappers)
    ↓
Domain (Entities, Ports, Services)
    ↓
Infrastructure (JPA, Adapters, Configs, Security)
```

**Benefícios:**
- Baixo acoplamento e alta testabilidade
- Facilita substituição de tecnologias
- Domínio limpo e reutilizável

---

## 📂 Estrutura do Projeto

```
src/
 └── main/java/com/connectfood/core
     ├── CoreServiceApplication.java
     ├── application/
     │    ├── mapper/
     │    └── usecase/
     │         ├── authentication/
     │         └── users/
     ├── domain/
     │    ├── exception/
     │    ├── factory/
     │    ├── model/
     │    ├── repository/
     │    ├── service/
     │    └── utils/
     ├── entrypoint/
     │    ├── rest/controller/
     │    └── rest/handler/
     └── infrastructure/
          ├── config/
          ├── persistence/
          │    ├── adapter/
          │    ├── entity/
          │    ├── jpa/
          │    ├── mapper/
          │    └── specification/
          └── security/
```

**Principais módulos:**
- `domain` → entidades, regras de negócio e interfaces (ports)
- `application` → casos de uso (use cases) e mapeamento entre camadas
- `infrastructure` → JPA, configs, segurança e adapters
- `entrypoint` → controladores REST (implementações OpenAPI)

---

## 🗄️ Banco de Dados e Migrações

**Banco:** PostgreSQL 16  
**Versionamento:** Flyway (executado no startup)  
**Schema:** `core`

### Tabelas principais
| Tabela | Descrição |
|--------|------------|
| `core.users` | Usuários do sistema (clientes e donos de restaurante) |
| `core.address` | Endereços vinculados aos usuários |

**Scripts:**
- `V1__init_core_schema.sql` — Criação do schema e tabelas
- `V2__insert_data.sql` — Usuários e endereços de exemplo
- `V3__insert_teacher_user.sql` — Usuário “Professor FIAP” (role OWNER)

---

## 🐳 Execução com Docker Compose

### Subir o ambiente
```bash
docker compose up -d --build
```

A API estará disponível em [http://localhost:9090](http://localhost:9090)

### Parar o ambiente
```bash
docker compose down
```

**Serviços disponíveis:**
- `db` → PostgreSQL com healthcheck
- `core-service` → API Spring Boot aguardando DB estar `healthy`

---

## 💻 Execução Local (sem Docker)

**Pré-requisitos:**  
Java 21, Maven 3.9+, PostgreSQL local (porta 5432)

### 1️⃣ Subir banco (opcional via Docker)
```bash
docker run --name connectfood-db -e POSTGRES_DB=connectfood -e POSTGRES_USER=connect -e POSTGRES_PASSWORD=food -p 5432:5432 -d postgres:16-alpine
```

### 2️⃣ Rodar a aplicação
```bash
mvn spring-boot:run
```

Swagger UI → [http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)

### 3️⃣ Encerrar o banco (se rodando via Docker)
```bash
docker rm -f connectfood-db
```

---

## 🌍 Variáveis de Ambiente

| Variável | Descrição | Default |
|-----------|------------|----------|
| `SERVER_PORT` | Porta da aplicação | 9090 |
| `SPRING_DATASOURCE_URL` | URL do banco | `jdbc:postgresql://db:5432/connectfood` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `connect` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `food` |
| `JWT_SECRET` | Chave secreta JWT | `nqoTpDYVygp3dUsX6CNdTnZgWSuBmWZUNOv/kM8y6go=` |
| `JWT_EXPIRATION_SECONDS` | Expiração do token (s) | 3600 |

---

## 📘 Documentação da API (Swagger)

- **Swagger UI:** [http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)
- **API Docs (JSON):** [http://localhost:9090/v3/api-docs](http://localhost:9090/v3/api-docs)

**Endpoints principais:**
| Método | Rota | Descrição |
|---------|------|------------|
| POST | `/v1/users` | Cria usuário |
| GET | `/v1/users` | Lista usuários |
| GET | `/v1/users/{uuid}` | Consulta usuário |
| PUT | `/v1/users/{uuid}` | Atualiza dados |
| PATCH | `/v1/users/{uuid}/password` | Altera senha |
| DELETE | `/v1/users/{uuid}` | Remove usuário |
| POST | `/v1/auth/login` | Autentica e gera token JWT |

**Padrão de erro:** RFC 7807 — `application/problem+json`  
Campos: `type`, `title`, `status`, `detail`, `instance`, `errors[]`

---

## 📬 Postman Collection

**Arquivos disponíveis em `docs/postman`:**
- `ConnectFood - Collection (FIAP TC1).postman_collection.json`
- `ConnectFood - Environments.postman_environment.json`

**Como usar:**
1. Importar ambos no Postman
2. Selecionar o ambiente “ConnectFood - Scenarios Local”
3. Executar a pasta `0) Run All Scenarios` para validar todos os endpoints

---

## 🩺 Healthcheck e Actuator

**Endpoints disponíveis:**
| Endpoint | Descrição |
|-----------|------------|
| `/actuator/health` | Status da aplicação |
| `/actuator/info` | Informações básicas |

**Exemplo:**
```json
{"status": "UP"}
```

**Docker Compose:** inclui healthcheck automático com base no `/actuator/health`.

---

## 🧪 Testes

Executar testes com:
```bash
mvn test
```

Inclui testes básicos de inicialização do contexto Spring Boot.

---

## 👥 Autores

| Nome |
|------|
| Lucas Santos Mumbarra |
| Suelen Peres |
| Beatriz Ribeiro | 
| Pilar Calderón | 
| Caio Teles | 

---

## 🔗 Links úteis

- Swagger UI → http://localhost:9090/swagger-ui.html
- API Docs → http://localhost:9090/v3/api-docs
- Postman Collection → `/docs/postman`
- Banco (Docker) → `localhost:5432` (connect/food)

---

## ✅ Status do Projeto — Fase 1

### 🔹 Requisitos Obrigatórios

| Categoria | Requisito | Status |
|------------|------------|--------|
| **Funcionalidade** | CRUD completo de usuários (criar, listar, atualizar, excluir) | ✅ |
|  | Endpoint separado para troca de senha | ✅ |
|  | Endpoint distinto para atualização dos demais dados | ✅ |
|  | Registro da data da última alteração | ✅ |
|  | Busca de usuários por nome | ✅ |
|  | Validação de login (login e senha válidos) | ✅ |
|  | Garantia de e-mail único no cadastro | ✅ |
|  | Dois tipos de usuários: **CLIENTE** e **OWNER (dono de restaurante)** | ✅ |
| **Qualidade do Código** | Uso de boas práticas (Spring Boot, SOLID, OO, camadas claras) | ✅ |
| **Documentação** | Endpoints documentados com Swagger/OpenAPI 3.0.4 | ✅ |
|  | Exemplos de requisições e respostas (sucesso e erro) | ✅ |
| **Banco de Dados** | Banco relacional (PostgreSQL) versionado com Flyway | ✅ |
|  | Banco e app rodando via Docker Compose | ✅ |
| **Coleções de Teste** | Postman Collection (.json) com cenários válidos e inválidos | ✅ |
|  | Cobertura de: cadastro, erro, senha, atualização, busca, login | ✅ |
| **Repositório** | Código, Swagger e Postman no GitHub público | ✅ |

---

### 🟦 Requisitos Opcionais (Desafio Extra)

| Categoria | Requisito | Status |
|------------|------------|--------|
| **Segurança** | Implementar autenticação com Spring Security e JWT | ✅ (implementado) |
| **Testes Automatizados** | Testes unitários com JUnit + Mockito | ✅ (básicos de contexto) |
