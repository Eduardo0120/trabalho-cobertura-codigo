# QA Backend Java 21

Projeto backend em Java 21 criado para avaliacao de candidatos de QA. A aplicacao expoe uma API REST simples de clientes, produtos, pedidos, saldo e relatorios administrativos.

O sistema contem erros intencionais de desenvolvimento e de negocio. O candidato deve fazer fork do projeto, criar testes unitarios, analisar cobertura de codigo, informar o percentual coberto e documentar os problemas encontrados com evidencias.

## Stack

- Java 21
- Spring Boot 3.3
- Maven
- JUnit 5
- JaCoCo

## Como executar

```powershell
cd C:\Desenvolvimento\qa-backend-java21
mvn spring-boot:run
```

A API sobe em:

```text
http://localhost:8081
```

## Endpoints principais

```text
GET  /api/customers
POST /api/customers
POST /api/customers/{id}/deposit
POST /api/customers/{id}/withdraw

GET  /api/products
POST /api/orders
GET  /api/orders

GET  /api/admin/reports/summary
```

## Rodar testes

```powershell
mvn test
```

## Gerar cobertura

```powershell
mvn verify
```

Relatorio HTML:

```text
target/site/jacoco/index.html
```

Arquivo CSV para obter percentual:

```text
target/site/jacoco/jacoco.csv
```

O candidato deve informar no relatorio a porcentagem de codigo coberto, preferencialmente separando linhas, branches e metodos quando possivel.

## Exemplos de payload

Criar cliente:

```json
{
  "name": "Novo Cliente",
  "email": "novo@email.com",
  "initialBalance": 100.00
}
```

Saque:

```json
{
  "amount": 75.00
}
```

Criar pedido:

```json
{
  "customerId": 1,
  "couponCode": "QA10",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

## Atividade do candidato

Leia [DESAFIO_CANDIDATO.md](DESAFIO_CANDIDATO.md) antes de iniciar. Use [docs/MODELO_RELATORIO_BUGS.md](docs/MODELO_RELATORIO_BUGS.md) como sugestao de formato para documentar as evidencias.
