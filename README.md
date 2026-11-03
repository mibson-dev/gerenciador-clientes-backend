# Cadastro de Clientes

Sistema web para registrar e consultar clientes, com histórico de compras e atendimentos. Feito para pequenos negócios como salões, barbearias, lojas e prestadores de serviço, que hoje controlam isso em fichas de papel ou planilhas.

É **multi-empresa**: uma única instalação atende vários negócios, e cada admin só enxerga os clientes da própria empresa.

> Projeto de portfólio, feito para rodar localmente.

---

## Funcionalidades

- **Login do administrador** com JWT (token válido por 8 horas)
- **Dashboard** com total de clientes e aniversariantes do mês
- **CRUD de clientes**: nome, telefone/WhatsApp, e-mail, data de nascimento e observações
- **Busca** por nome ou telefone
- **Histórico de atendimentos e compras** de cada cliente, com data, descrição e valor opcional
- **Isolamento entre empresas**: tentar acessar o cliente de outra empresa retorna `403`

## Stack

| Camada       | Tecnologias                                                                             |
| ------------ | --------------------------------------------------------------------------------------- |
| Backend      | Java 21, Spring Boot 3.3, Spring Security, Spring Data JPA / Hibernate, Bean Validation |
| Autenticação | JWT (Auth0 java-jwt), senhas com BCrypt                                                 |
| Banco        | PostgreSQL                                                                              |
| Frontend     | HTML, CSS e JavaScript puro                                                             |
| Outros       | dotenv-java, Docker (Dockerfile multi-stage)                                            |

## Modelo de dados

```
Empresa ──┬── Admin (login)
          │
          └── Cliente ── Atendimento (histórico)
```

## Segurança

Algumas decisões que valem destacar:

- **Validação em duas camadas.** O frontend bloqueia dados inválidos para a experiência do usuário, e o backend valida tudo de novo (`@Email`, `@Past`, `@PastOrPresent`, `@PositiveOrZero`, `@Size`, `@Digits`), porque a API pode ser chamada direto, sem passar pelo front.
- **Proteção contra XSS.** Tudo que vem da API é inserido na página com `textContent`, nunca concatenado em HTML.
- **Cadastro de empresas protegido por chave mestra.** O `POST /empresas` só funciona com o header `X-Chave-Mestra` correto. Se a variável não estiver configurada, o endpoint fica desativado. A comparação da chave é feita em tempo constante (`MessageDigest.isEqual`).
- **Sem credenciais no código.** O admin de demonstração é criado a partir de variáveis de ambiente e a senha nunca aparece no log.
- **CORS restrito** às origens configuradas (padrão: `localhost` e `127.0.0.1`).
- **Respostas de erro padronizadas**: `400` para dados inválidos, `401` sem token, `403` sem permissão e `404` para recurso inexistente, sempre no formato `{ "mensagem": "..." }`.

## Como rodar

### Pré-requisitos

- Java 21
- PostgreSQL
- Maven (ou o wrapper da IDE)

### 1. Banco de dados

Crie o banco:

```sql
CREATE DATABASE cadastro_clientes;
```

As tabelas são criadas automaticamente pelo Hibernate na primeira execução.

### 2. Variáveis de ambiente

Na pasta `cadastro-clientes-backend/cadastro-clientes`, copie o `.env.example` para `.env` e preencha:

| Variável                                | Para que serve                                                          |
| --------------------------------------- | ----------------------------------------------------------------------- |
| `DB_URL`                                | URL JDBC do Postgres                                                    |
| `DB_USERNAME` / `DB_PASSWORD`           | Credenciais do banco                                                    |
| `JWT_SECRET`                            | Segredo para assinar os tokens                                          |
| `CHAVE_MESTRA`                          | Chave exigida para criar empresas. Vazia = cadastro desativado          |
| `DEMO_ADMIN_EMAIL` / `DEMO_ADMIN_SENHA` | Admin de demonstração, criado só com o banco vazio. Sem senha, não cria |
| `CORS_ORIGENS`                          | Opcional. Origens liberadas, separadas por vírgula                      |

### 3. Backend

```bash
cd cadastro-clientes-backend/cadastro-clientes
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. Na primeira execução, o log mostra o e-mail do admin demo criado.

### 4. Dados de demonstração (opcional)

Com a aplicação já iniciada uma vez, rode o script `popular-dados-demo.sql` no banco `cadastro_clientes`. Ele cria 24 clientes e 41 atendimentos fictícios na empresa demo, com aniversariantes sempre no mês atual. Pode ser executado mais de uma vez sem duplicar dados.

### 5. Frontend

Abra `cadastro-clientes-frontend/login.html` **por um servidor local**, como o navegador integrado do IntelliJ ou a extensão Live Server do VS Code.

> Abrir o arquivo com duplo clique (`file://`) não funciona, porque o CORS só aceita origens `localhost`.

Entre com o `DEMO_ADMIN_EMAIL` e a `DEMO_ADMIN_SENHA` definidos no `.env`.

## Endpoints

Todos exigem `Authorization: Bearer <token>`, exceto os dois primeiros.

| Método   | Rota                       | Descrição                                               |
| -------- | -------------------------- | ------------------------------------------------------- |
| `POST`   | `/auth/login`              | Autentica e retorna o token                             |
| `POST`   | `/empresas`                | Cria empresa e admin (exige header `X-Chave-Mestra`)    |
| `GET`    | `/clientes?busca=`         | Lista clientes, com busca opcional por nome ou telefone |
| `POST`   | `/clientes`                | Cadastra cliente                                        |
| `PUT`    | `/clientes/{id}`           | Atualiza cliente                                        |
| `DELETE` | `/clientes/{id}`           | Remove cliente e o histórico dele                       |
| `GET`    | `/clientes/dashboard`      | Total de clientes e aniversariantes do mês              |
| `GET`    | `/atendimentos?clienteId=` | Histórico de um cliente                                 |
| `POST`   | `/atendimentos`            | Registra atendimento ou compra                          |
| `DELETE` | `/atendimentos/{id}`       | Remove atendimento                                      |

<details>
<summary>Exemplos de requisição</summary>

**Login**

```json
POST /auth/login
{
  "email": "admin@demo.com",
  "senha": "sua_senha_demo"
}
```

**Criar empresa**

```
POST /empresas
X-Chave-Mestra: <sua chave>
```

```json
{
  "nomeEmpresa": "Barbearia Exemplo",
  "telefoneEmpresa": "(75) 99999-0000",
  "adminNome": "João",
  "adminEmail": "joao@exemplo.com",
  "adminSenha": "senhaForte123"
}
```

**Cadastrar cliente**

```json
POST /clientes
{
  "nome": "Renata Lopes Amorim",
  "telefone": "(75) 99147-2583",
  "email": "renata.amorim@exemplo.com",
  "dataNascimento": "1994-11-14",
  "observacoes": "Prefere ser lembrada dos horários pelo WhatsApp."
}
```

**Registrar atendimento**

```json
POST /atendimentos
{
  "data": "2026-11-03",
  "descricao": "Corte e hidratação",
  "valor": 120.00,
  "clienteId": 1
}
```

</details>

## Estrutura

```
cadastro-clientes-backend/cadastro-clientes/
└── src/main/java/com/example/cadastroclientes/
    ├── config/        # seeder do admin demo
    ├── controller/    # endpoints REST
    ├── DTO/           # entrada e saída da API, com validações
    ├── exceptions/    # exceções de negócio e handler global
    ├── model/         # entidades JPA
    ├── repositories/  # Spring Data JPA
    ├── security/      # JWT, filtro, CORS e configuração do Spring Security
    └── services/      # regras de negócio

cadastro-clientes-frontend/
├── login.html
├── clientes.html
├── css/style.css
└── js/
    ├── login.js
    └── clientes.js
```

## Demonstração

<!-- Coloque aqui o link do vídeo e alguns prints -->

- Vídeo: _https://youtu.be/OBm-iM5pDI4?si=nW5PjVeUUIDURDfM_

## Autor

**Mibson Ferreira**
[mibson.dev](https://mibson.dev) · [LinkedIn](https://linkedin.com/in/mibsonferreira) · [GitHub](https://github.com/mibson-dev)
