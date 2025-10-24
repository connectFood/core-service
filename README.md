# ConnectFood - Core Service (FIAP Pós-Tech - Fase 1)

Documentação completa do serviço Core do sistema ConnectFood. Este módulo é responsável por usuários e autenticação (login/JWT), seguindo arquitetura hexagonal (ports & adapters), com contrato OpenAPI/Swagger e migrações versionadas com Flyway.


## 🏷️ 1. Nome e Contexto do Projeto
- Nome completo: ConnectFood - Core Service
- Módulo: Core (usuários e autenticação)
- Curso: FIAP Pós-Tech – Software Architecture (ADJ)
- Fase: Tech Challenge 1 (Fase 1)
- Autores: Lucas Santos Mumbarra
- Objetivo do módulo: Gerenciar usuários (donos de restaurante e clientes), autenticação JWT e cadastro, com base em arquitetura hexagonal.


## 🧠 2. Descrição Geral
O Core Service é um microserviço REST construído em Spring Boot 3.5.6, Java 21, PostgreSQL e Docker, utilizando Flyway para versionamento de schema e SpringDoc OpenAPI para documentação (Swagger). Ele é a base do ConnectFood e centraliza:
- Cadastro, consulta, atualização, exclusão e alteração de senha de usuários;
- Autenticação via JWT (login e emissão de bearer token);
- Estrutura modular para expansão futura (ex.: pedidos, restaurantes);
- Padrões de qualidade e boas práticas (camadas, DTOs, validação, RFC 7807).

Principais tecnologias e versões:
- Spring Boot 3.5.6, Java 21
- PostgreSQL 16, Flyway
- Spring Data JPA, Spring Security
- SpringDoc OpenAPI 3.0.4 (Swagger UI)
- Docker e Docker Compose


## ⚙️ 3. Arquitetura e Organização
O projeto segue arquitetura hexagonal (ports & adapters), com clara separação de responsabilidades:

```
Entrypoint (REST Controller)
    ↓
Application (Use Cases)
    ↓
Domain (Entities & Services)
    ↓
Infrastructure (Adapters, JPA, Configs)
```

- domain → regras de negócio puras (entities, services, ports);
- application → orquestração de casos de uso e mapeamentos;
- infrastructure → persistência (JPA), configurações, segurança, adapters;
- entrypoint → controladores REST que implementam as interfaces geradas a partir do OpenAPI.

Benefícios: baixo acoplamento, alta testabilidade e facilidade para trocar tecnologias sem impactar o domínio.


## 📂 4. Estrutura de Pastas (resumo)
Hierarquia resumida do módulo core-service:

- src/main/java/com/connectfood/core
  - CoreServiceApplication.java — classe principal Spring Boot
  - application
    - mapper — mapeamentos de DTOs do nível de aplicação
    - usercase
      - authentication — AuthenticationUseCase
      - users — CreateUserUseCase, GetUserUseCase, ListUsersUseCase, UpdateUserUseCase, ChangedPasswordUseCase, DeleteUserUseCase
  - domain
    - exception — BadRequestException, ConflictException, NotFoundException, UnauthorizedException
    - model — Users, Address, commons/BaseModel
    - repository — ports (UsersRepository, AddressRepository)
    - service — UsersService (porta) e adapter/UsersServiceImpl (impl de domínio)
  - entrypoint
    - rest/controller — UsersController, AuthenticationController
    - rest/handler — tratamento global de erros (ProblemDetails)
  - infrastructure
    - config — configs de OpenAPI/SpringDoc e afins
    - persistence — entities JPA, repositórios, adapters, mappers, specifications
    - security — autenticação/JWT
- src/main/resources
  - application.yml — propriedades
  - db/migration — migrações Flyway (V1__init_core_schema.sql, V2__insert_data.sql, V3__insert_teacher_user.sql)
  - openapi/connectfood.yml — contrato OpenAPI (3.0.4)
- docker-compose.yml, Dockerfile
- pom.xml, mvnw, mvnw.cmd
- src/test/java — testes (JUnit/Spring Boot)


## 🧮 5. Banco de Dados e Migrações
- Banco: PostgreSQL 16
- Versionamento: Flyway (executa automaticamente no startup)
- Schema padrão: core (criado automaticamente se não existir)

Migrações principais:
- V1__init_core_schema.sql: cria schema core e tabelas principais (users, address), índices e triggers de updated_at.
- V2__insert_data.sql: popula 200 usuários demo (roles CUSTOMER/OWNER) e endereços.
- V3__insert_teacher_user.sql: cria usuário “Professor FIAP” com senha 123456 e role OWNER, incluindo endereço comercial.

Tabelas principais:
- core.users: id, uuid, full_name, email, login, password (hash), roles (jsonb), created_at, updated_at, version
- core.address: id, uuid, user_id, street, number, complement, neighborhood, city, state, zip_code, country, address_type, is_default, created_at, updated_at, version

Observações:
- Restrições de unicidade em email, login e uuid.
- Índices para pesquisas e GIN em roles.


## 🐳 6. Execução via Docker Compose
O projeto possui um docker-compose.yml que sobe PostgreSQL + API Core. Para executar:

```bash
docker compose up -d --build
```

- O serviço db possui healthcheck e o core-service aguarda o DB ficar healthy.
- A aplicação ficará disponível em: http://localhost:9090

Parar os serviços:
```bash
docker compose down
```


## 🌍 7. Variáveis de Ambiente
Principais variáveis e valores padrão (ver application.yml):

| Variável | Descrição | Valor padrão |
|-----------|------------|--------------|
| SERVER_PORT | Porta da aplicação | 9090 |
| SPRING_DATASOURCE_URL | URL do banco | jdbc:postgresql://db:5432/connectfood (compose) ou jdbc:postgresql://localhost:5432/connectfood |
| SPRING_DATASOURCE_USERNAME | Usuário | connect (compose) ou root |
| SPRING_DATASOURCE_PASSWORD | Senha | food (compose) ou root |
| SPRING_JPA_DEFAULT_SCHEMA | Schema padrão | core |
| SPRING_FLYWAY_ENABLED | Migrações automáticas | true |
| SPRING_FLYWAY_DEFAULT_SCHEMA | Schema do Flyway | core |
| SPRING_FLYWAY_SCHEMAS | Schemas migrados | core |
| JWT_SECRET | Segredo do token JWT | nqoTpDYVygp3dUsX6CNdTnZgWSuBmWZUNOv/kM8y6go= |
| JWT_EXPIRATION_SECONDS | Expiração do token (s) | 3600 |


## 💻 8. Execução Local (sem Docker)
Pré-requisitos: Java 21, Maven 3.9+, Docker (opcional para subir só o banco).

1) Suba apenas o PostgreSQL (opcional via Docker):
```bash
docker run --name connectfood-db -e POSTGRES_DB=connectfood -e POSTGRES_USER=connect -e POSTGRES_PASSWORD=food -p 5432:5432 -d postgres:16-alpine
```

2) Exporte as variáveis (se necessário) e rode a aplicação:
```bash
mvn spring-boot:run
```
- A aplicação iniciará em http://localhost:9090
- O Flyway executará as migrações automaticamente no startup.

3) Parar o banco (se subiu via Docker):
```bash
docker rm -f connectfood-db
```


## 📘 9. Documentação da API (Swagger)
A documentação é fornecida via SpringDoc OpenAPI.
- Swagger UI → http://localhost:9090/swagger-ui.html
- API Docs JSON → http://localhost:9090/v3/api-docs

Padrão de erros: ProblemDetails (RFC 7807) com content-type application/problem+json, incluindo campos type, title, status, detail, instance e errors[].

Status esperados por rota (exemplos principais):
- POST /v1/users → 201, 400, 409
- GET /v1/users → 200, 400
- GET /v1/users/{uuid} → 200, 404
- PUT /v1/users/{uuid} → 200, 400, 404, 409
- PATCH /v1/users/{uuid}/password → 204, 401, 404
- POST /v1/auth/login → 200, 401


## 📬 10. Collection Postman
Há uma collection com cenários baseados no Swagger para validação ponta a ponta:
- Local: ver docs/postman
- Collection: ConnectFood - Collection (FIAP TC1).postman_collection.json
- Environment: ConnectFood - Environments.postman_environment.json

Como usar:
1. Importe ambos os arquivos no Postman.
2. Selecione o ambiente “ConnectFood - Scenarios Local”.
3. Execute a pasta “0) Run All Scenarios”.
4. Todos os endpoints serão validados automaticamente (201, 400, 401, 404, 409 etc).

Dica: Incluímos um print do Runner com os resultados (ver docs/img/postman-runner.png).


## 🧾 11. Critérios da Fase 1 (Checklist)

| Critério | Descrição | Status |
|-----------|------------|--------|
| CRUD de Usuários | Create, Read, Update, Delete | ✅ |
| Autenticação JWT | Login e bearer token | ✅ |
| Banco relacional | PostgreSQL + Flyway | ✅ |
| Documentação Swagger | OpenAPI 3.0.4 | ✅ |
| Testes via Postman | Todos os cenários (200–409) | ✅ |
| Execução Docker | Compose funcional | ✅ |
| Tratamento de Erros | RFC 7807 (ProblemDetails) | ✅ |
| Arquitetura limpa | Hexagonal (ports & adapters) | ✅ |


## 🧪 12. Testes Automatizados (JUnit)
Há testes JUnit prontos (ex.: CoreServiceApplicationTests). Para executar:
```bash
mvn test
```


## 👨‍💻 13. Autor e Créditos
```
Autores: Lucas Santos Mumbarra
Curso: FIAP Pós-Tech - Software Architecture (ADJ)
Turma: 2025
Fase: Tech Challenge 1
Professor: [Nome do Professor]
```


## 🧩 14. Referências e Links
- Swagger UI: http://localhost:9090/swagger-ui.html
- API Docs: http://localhost:9090/v3/api-docs
- Postman Collection: ConnectFood-swagger-scenarios.postman_collection.json
- Postman Environment: ConnectFood-swagger-scenarios-local.postman_environment.json
- Relatório técnico (PDF): docs/Relatorio_TechChallenge_Fase1.pdf
- Repositório GitHub: [link-do-repo]
