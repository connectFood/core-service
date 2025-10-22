# Core Service — ConnectFood

Documentação do serviço Core do sistema ConnectFood. Este módulo expõe APIs para gestão de usuários (clientes e donos de restaurante) e validação de login, seguindo uma arquitetura limpa, com camadas bem definidas, OpenAPI para contrato e migrações com Flyway.


## Sumário
- Visão Geral
- Arquitetura e Organização
- Estrutura de Pastas
- Fluxo de Requisição (end to end)
- Tecnologias e Versões
- Execução do Projeto (Local)
- Banco de Dados e Migrações (Flyway)
- Documentação da API (OpenAPI/Swagger)
- Padrões, Boas Práticas e Convenções
- Testes
- Diretrizes de Contribuição


## Visão Geral
Este serviço é um Spring Boot 3 que implementa endpoints de usuários de acordo com um contrato OpenAPI. O projeto está estruturado em camadas para favorecer coesão, separação de responsabilidades, testabilidade e evolução.

Principais conceitos:
- Separação clara entre domínios (camada domain) e tecnologia (camada infrastructure);
- Casos de uso como orquestradores de regras de aplicação (camada application);
- Entrypoints finos (REST controllers) que delegam para casos de uso;
- Persistência isolada em adapters e repositórios JPA;
- Contrato OpenAPI como fonte de verdade para a API pública.


## Arquitetura e Organização
O projeto adota princípios de Clean Architecture/DDD em uma organização por camadas:

- entrypoint (adaptadores de entrada)
  - Ex.: REST controllers (UsersController) que implementam interfaces geradas a partir do OpenAPI.
- application (regras de aplicação)
  - Casos de uso (CreateUserUseCase, GetUserUseCase, ListUsersUseCase, UpdateUserUseCase) e mappers da aplicação.
- domain (núcleo do negócio)
  - Models (Users, Address, BaseModel), ports (UsersRepository, AddressRepository), regras de negócio e serviços de domínio (UsersService/UsersServiceImpl).
- infrastructure (adaptadores de saída)
  - Persistência (entities JPA, repositórios JPA e adapter UsersRepositoryImpl), mappers para infra, e configurações (OpenApiConfig).

Esta separação reduz acoplamento com frameworks, facilita testes e mudanças de tecnologia.


## Estrutura de Pastas
Caminho raiz: core-service

- src/main/java/com/connectfood/core
  - CoreServiceApplication.java — classe principal Spring Boot.
  - application
    - mapper/UsersMapper.java — mapeamentos do nível de aplicação.
    - usercase/users — casos de uso: CreateUserUseCase, GetUserUseCase, ListUsersUseCase, UpdateUserUseCase.
  - domain
    - exception — exceções de domínio (BadRequestException, ConflictException, NotFoundException, UnauthorizedException).
    - model — modelos do domínio (Users, Address, commons/BaseModel).
    - repository — ports do domínio (UsersRepository, AddressRepository).
    - service — portas de serviço de domínio (UsersService) e sua implementação em adapter/UsersServiceImpl.
  - entrypoint
    - rest/controller — UsersController implementa a interface gerada UsersApi.
  - infrastructure
    - config — OpenApiConfig (metadados do Swagger/OpenAPI).
    - persistence
      - adapter — UsersRepositoryImpl (adapter que implementa UsersRepository usando JPA).
      - entity — entidades JPA (UsersEntity, AddressEntity, commons/BaseEntity).
      - jpa — repositórios Spring Data (JpaUsersRepository, JpaAddressRepository).
      - mapper — UsersInfrastructureMapper (conversão entre domain e infra).
- src/main/resources
  - application.yml — configurações (porta, datasource, JPA, Flyway, SpringDoc).
  - db/migration — migrações Flyway (V1__init_core_schema.sql, V2__insert_data.sql).
  - openapi/connectfood.yml — contrato OpenAPI usado para geração de código.


## Fluxo de Requisição (end to end)
1. O contrato openapi/connectfood.yml define os endpoints e modelos.
2. O plugin openapi-generator gera as interfaces de API (ex.: com.connectfood.api.UsersApi) e modelos (com.connectfood.model.*).
3. UsersController implementa UsersApi e delega a chamada para o caso de uso adequado.
4. Os casos de uso usam o serviço de domínio e/ou os ports de repositório para cumprir as regras de aplicação.
5. UsersRepositoryImpl (adapter) traduz as chamadas de port para JPA usando Entities, JpaRepositories e mappers de infraestrutura.
6. Respostas são convertidas para modelos expostos pela API (gerados pelo OpenAPI) e retornadas ao cliente.


## Tecnologias e Versões
- Java 21
- Spring Boot 3.5.6
  - spring-boot-starter-web, validation, data-jpa, actuator
- SpringDoc OpenAPI Starter (2.8.11)
- OpenAPI Generator Maven Plugin (7.8.0)
- PostgreSQL (driver runtime)
- Flyway (Core + Postgres)
- Lombok (1.18.34)
- Jackson (incl. jsr310)
- JJWT (0.12.5) para utilidades de token (crypto)
- Testes: spring-boot-starter-test, spring-security-test
- Maven Surefire Plugin 3.2.5; Maven Compiler Plugin 3.13.0

Porta padrão do serviço: 9090 (configurado em application.yml)


## Execução do Projeto (Local)
Pré-requisitos:
- Java 21 instalado (JAVA_HOME apontando para JDK 21+)
- Maven 3.9+ (ou uso do wrapper mvnw/mvnw.cmd)
- PostgreSQL em execução

Passos:
1. Configure o banco local (ver seção Banco de Dados).
2. Build do projeto:
   - Windows: mvnw.cmd clean package
   - Linux/Mac: ./mvnw clean package
3. Executar a aplicação:
   - Windows: mvnw.cmd spring-boot:run
   - Ou executar o jar gerado em target: java -jar target/core-0.0.1-SNAPSHOT.jar
4. Acesse: http://localhost:9090

Perfis: o projeto usa application.yml único; você pode adicionar profiles e sobrepor propriedades conforme necessidade (ex.: application-dev.yml, application-prod.yml).

## Execução com Docker
Há um Dockerfile e um docker-compose.yml na raiz do projeto.

- Subir tudo com Docker Compose (API + Postgres) em modo daemon:
  - docker compose up --build -d
  - Após subir, a aplicação estará em http://localhost:9090.
  - Ver logs (tempo real): docker compose logs -f core-service
  - Parar e remover containers e volume do banco: docker compose down -v
  - Rebuild forçado (se mudou o código/Dockerfile): docker compose build --no-cache && docker compose up -d

Observações importantes:
- O docker-compose já provisiona o Postgres e a API com dependência de saúde (healthcheck), então não é necessário subir o banco manualmente.
- Credenciais do banco usadas pelo compose: DB_NAME=connectfood, DB_USER=connect, DB_PASSWORD=food, host=db, port=5432.
- O schema padrão é "core" e as migrações Flyway rodam automaticamente ao subir a API.

- Buildar imagem manualmente e rodar container da app (opcional):
  - docker build -t connectfood/core-service:local .
  - docker run --rm -p 9090:9090 --env-file .env connectfood/core-service:local

Variáveis úteis (podem ser passadas via ambiente):
- SERVER_PORT=9090
- DB_HOST=localhost, DB_PORT=5432, DB_NAME=connectfood, DB_USER=root, DB_PASSWORD=root
- SPRING_FLYWAY_ENABLED=true


## Banco de Dados e Migrações (Flyway)
- Datasource padrão (application.yml):
  - URL: jdbc:postgresql://localhost:5432/connectfood
  - Usuário: root
  - Senha: root
  - Schema: core
- Flyway está habilitado e roda na inicialização, aplicando as migrações em src/main/resources/db/migration.
- V1__init_core_schema.sql: cria o schema e tabelas iniciais.
- V2__insert_data.sql: insere dados de exemplo.

Dicas:
- Certifique-se de que o banco postgres tenha o database connectfood criado e as credenciais root/root, ou altere application.yml conforme seu ambiente.
- ddl-auto está como none; a evolução de schema deve ser feita via scripts Flyway.


## Documentação da API (OpenAPI/Swagger)
- OpenAPI/Swagger UI: http://localhost:9090/swagger-ui/index.html
- OpenAPI JSON: http://localhost:9090/v3/api-docs
- Contrato fonte: src/main/resources/openapi/connectfood.yml
- Código gerado em build: target/generated-sources/openapi (interfaces de API e modelos expostos)

Geração automática (mvn generate-sources/package) cria as interfaces com.connectfood.api.* e modelos com.connectfood.model.*. O controller UsersController implementa UsersApi, garantindo aderência ao contrato.

## Esquema Resumido das APIs
Abaixo um resumo dos endpoints definidos em src/main/resources/openapi/connectfood.yml. Todos os retornos de sucesso usam application/json; os erros usam application/problem+json com o schema ProblemDetails.

- POST /v1/auth/login
  - Público (sem bearer)
  - Request: LoginValidationRequest { login, password }
  - Responses:
    - 200 BaseResponseOfJwtTokenResponse { success, content: { accessToken, tokenType, expiresIn } }
    - 401 ProblemDetails (credenciais inválidas)

- POST /v1/users
  - Autenticado (Bearer JWT)
  - Request: UserCreateRequest { fullName, email, login, password, roles[], addresses[] }
  - Responses:
    - 201 BaseResponseOfUserResponse { success, content: UserResponse }
    - 400 ProblemDetails (dados inválidos)
    - 409 ProblemDetails (e-mail já cadastrado)

- GET /v1/users
  - Autenticado (Bearer JWT)
  - Query params: name (string, opcional), role (CUSTOMER|OWNER, opcional), page (int, default 0), size (int, default 20)
  - Responses:
    - 200 PageResponseOfUserResponse { success, content: UserResponse[], totalElements, page, size }
    - 400 ProblemDetails (parâmetros inválidos)

- GET /v1/users/{uuid}
  - Autenticado (Bearer JWT)
  - Path: uuid (string)
  - Responses:
    - 200 BaseResponseOfUserResponse { success, content: UserResponse }
    - 404 ProblemDetails (usuário não encontrado)

- PUT /v1/users/{uuid}
  - Autenticado (Bearer JWT)
  - Path: uuid (string)
  - Request: UserUpdateRequest { fullName?, email?, login?, roles[]?, addresses[]? }
  - Responses:
    - 200 BaseResponseOfUserResponse { success, content: UserResponse }
    - 400 ProblemDetails (dados inválidos)
    - 404 ProblemDetails (usuário não encontrado)
    - 409 ProblemDetails (e-mail já existente)

- DELETE /v1/users/{uuid}
  - Autenticado (Bearer JWT)
  - Path: uuid (string)
  - Responses:
    - 204 No Content
    - 404 ProblemDetails (usuário não encontrado)
    - 409 ProblemDetails (restrição de deleção)

- PATCH /v1/users/{uuid}/password
  - Autenticado (Bearer JWT)
  - Path: uuid (string)
  - Request: ChangePasswordRequest { currentPassword, newPassword }
  - Responses:
    - 204 No Content (senha alterada)
    - 401 ProblemDetails (senha atual incorreta)
    - 404 ProblemDetails (usuário não encontrado)

Observações de modelos principais:
- BaseResponseOfUserResponse: { success: boolean, content: UserResponse }
- BaseResponseOfJwtTokenResponse: { success: boolean, content: JwtTokenResponse }
- PageResponseOfUserResponse: { success: boolean, content: UserResponse[], totalElements, page, size }
- ProblemDetails (RFC 7807): { type, title, status, detail, instance, errors[]? }


## Padrões, Boas Práticas e Convenções
- DRY (Don't Repeat Yourself)
  - Mappers centralizados (application e infrastructure) evitam duplicação de conversões.
  - Entidades base (BaseEntity, BaseModel) concentram atributos/comportos comuns.
- SOLID
  - Single Responsibility: cada classe com foco único (ex.: UseCases orquestram regras de aplicação; controller só recebe/retorna HTTP; adapters cuidam de persistência).
  - Open/Closed: classes abertas a extensão via interfaces/ports; fechadas para modificação desnecessária.
  - Liskov Substitution: ports de repositório (domain) podem ter múltiplas implementações (ex.: JPA, outra fonte) sem quebrar contratos.
  - Interface Segregation: ports e serviços focados; evitam interfaces "inchadas".
  - Dependency Inversion: domínio depende de abstrações (repositories/services), implementações concretas ficam na infraestrutura.
- Convenções Spring Boot
  - Anotações: @RestController, @Service, @Repository (ou @Component) para estereótipos; @Configuration para configs; @Validated/@Valid para validação.
  - Open-in-view desabilitado (open-in-view: false) para evitar N+1 e vazamentos transacionais; transações devem ser controladas no serviço/adapters conforme necessidade.
  - application.yml como fonte de config; use perfis quando necessário.
- Exceções e Tratamento de Erros
  - Exceções de domínio específicas (NotFoundException, ConflictException, etc.) são mapeadas para respostas HTTP coerentes via @ControllerAdvice (GlobalExceptionHandler). Os erros seguem o padrão Problem Details (RFC 7807) com media type application/problem+json, podendo incluir a lista de errors[field, message] para validações.
- Validação
  - Bean Validation (jakarta.validation) com @Valid nos endpoints e constraints nos DTOs gerados conforme OpenAPI.
- Mapeamentos/DTOs
  - O contrato OpenAPI define os modelos expostos (com.connectfood.model.*); a camada de aplicação e infraestrutura convertem para/desde os modelos de domínio e entidades JPA. Mantém o domínio isolado de detalhes de transporte e persistência.
- Migrações
  - Flyway controla evolução de schema de forma rastreável e reprodutível.
- Logging/Observabilidade
  - Spring Boot Actuator incluso; endpoints de saúde/infos podem ser habilitados conforme necessidade.


## Testes
- Testes de unidade e integração podem ser adicionados sob src/test/java (já existe CoreServiceApplicationTests.java).
- Execute: mvnw.cmd test
- Boas práticas:
  - Mock dos ports/repositórios no domínio.
  - Testes de repositório com banco em memória/container (futuro, via Testcontainers) ou perfil específico.


## Diretrizes de Contribuição
- Siga a organização por camadas existente; adicione novas funcionalidades iniciando pelo contrato (OpenAPI), gere as interfaces e então implemente o controller + use cases + adapters.
- Respeite os princípios DRY e SOLID descritos.
- Toda mudança de schema deve passar por nova migration Flyway (V3__..., V4__...).
- Nomes autoexplicativos, métodos curtos, e cobertura de testes quando possível.
- Mantenha a compatibilidade com o contrato OpenAPI. Se o contrato mudar, atualize connectfood.yml e gere novamente.


---
Qualquer dúvida, consulte o OpenApiConfig para informações expostas no Swagger e o application.yml para configurações padrão (porta 9090, datasource, Flyway e SpringDoc).


## Segurança e Autenticação (JWT)
Este serviço utiliza autenticação stateless baseada em JWT.

- Header esperado: Authorization: Bearer <token>
- Configurações principais (application.yml):
  - security.jwt.secret: segredo usado para assinar o token (HMAC)
  - security.jwt.expiration-seconds: tempo de expiração do token em segundos
- Principais componentes:
  - SecurityConfig, JwtAuthenticationFilter, JwtService, RestAuthEntryPoint, RestAccessDeniedHandler, UserDetailsService
  - Controllers: AuthenticationController (login) e UsersController (CRUD de usuários)

Fluxo de autenticação (exemplos):
1) Obter token
- Endpoint: POST http://localhost:9090/v1/auth/login
- Body:
  {
    "login": "<email-ou-login>",
    "password": "<senha>"
  }
- cURL:
  curl -s -X POST "http://localhost:9090/v1/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"login":"admin@connectfood.io","password":"admin123"}'

Resposta de sucesso (200):
  {
    "success": true,
    "content": {
      "accessToken": "<jwt>",
      "tokenType": "Bearer",
      "expiresIn": 3600
    }
  }

2) Consumir endpoints protegidos
- Ex.: GET /v1/users
- cURL:
  TOKEN="<jwt>"
  curl -H "Authorization: Bearer $TOKEN" http://localhost:9090/v1/users

Erros comuns:
- 401 Unauthorized: credenciais inválidas no login, token ausente/expirado.
- 403 Forbidden: token válido, porém sem permissão.
Os erros seguem o padrão Problem Details (RFC 7807), tratados por GlobalExceptionHandler e handlers de segurança.


## Healthcheck / Actuator
- Actuator está incluído e o health endpoint é usado pelo docker-compose para healthcheck da aplicação.
- Endpoints relevantes:
  - GET http://localhost:9090/actuator/health (UP quando saudável)
- Em produção (compose), a aplicação aguarda o Postgres ficar saudável antes de iniciar (depends_on + healthcheck), e o container da API só é considerado saudável quando o actuator/health retorna UP.


## Variáveis de Ambiente e Perfis
Principais variáveis que você pode ajustar via ambiente (CLI, compose, .env):
- SERVER_PORT (padrão: 9090)
- Datasource:
  - SPRING_DATASOURCE_URL (ex.: jdbc:postgresql://localhost:5432/connectfood)
  - SPRING_DATASOURCE_USERNAME (padrão: root)
  - SPRING_DATASOURCE_PASSWORD (padrão: root)
- JPA/Flyway:
  - SPRING_JPA_DEFAULT_SCHEMA (padrão: core)
  - SPRING_FLYWAY_ENABLED (padrão: true)
  - SPRING_FLYWAY_BASELINE_ON_MIGRATE (padrão: true)
  - SPRING_FLYWAY_DEFAULT_SCHEMA (padrão: core)
  - SPRING_FLYWAY_SCHEMAS (padrão: core)
- JWT:
  - JWT_SECRET (padrão no application.yml; altere em produção)
  - JWT_EXPIRATION_SECONDS (padrão: 3600)
- Outros:
  - SPRING_PROFILES_ACTIVE (compose usa "prod")
  - TZ (ex.: America/Sao_Paulo)

Observações:
- O docker-compose já define os valores adequados para executar localmente com Postgres no container.
- Em ambientes produtivos, SEMPRE defina um JWT_SECRET forte via variável de ambiente/secret.


## Troubleshooting (rápido)
- Swagger não abre? Verifique se a aplicação está em http://localhost:9090 e acesse /swagger-ui/index.html.
- Erro de conexão com DB local? Ajuste SPRING_DATASOURCE_URL/USERNAME/PASSWORD ou suba via docker compose.
- Flyway falhou? Verifique se o schema/database existem e as permissões do usuário; rode com SPRING_FLYWAY_BASELINE_ON_MIGRATE=true se iniciando em base existente.
- 401/403 nos endpoints? Gere novo token via /v1/auth/login e passe o header Authorization corretamente.
