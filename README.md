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
- SpringDoc OpenAPI Starter (2.5.0)
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
  - Exceções de domínio específicas (NotFoundException, ConflictException, etc.) simplificam mapeamento para respostas HTTP coerentes (pode-se adicionar um @ControllerAdvice conforme evolução).
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
