# Relatório de Bugs

## Informações Gerais

Projeto utilizado para avaliação técnica QA Backend Java 21.

Os testes unitários foram desenvolvidos com foco em:
- validação de regras de negócio
- cenários positivos e negativos
- análise de comportamento incorreto
- operações financeiras
- tratamento de exceções

---

# Bugs Encontrados

## BUG-01 - Validação de email insuficiente

### Tipo
Negócio

### Severidade
Média

### Classe/Método afetado
CustomerService.create()

### Pré-condições
Tentativa de cadastro utilizando email inválido.

### Passos para reproduzir
1. Criar cliente utilizando email inválido.
2. Executar cadastro.

### Resultado atual
O sistema realiza apenas validação básica verificando presença do caractere "@".

### Resultado esperado
O sistema deveria validar formato completo do email.

### Evidências
Teste unitário:
deveRetornarEmailInvalido()

Trecho responsável:
```java
if (request.email() == null || !request.email().contains("@"))
```

### Impacto
Permite cadastro de emails inválidos e inconsistência de dados.

---

## BUG-02 - Sistema permite depósito negativo

### Tipo
Negócio

### Severidade
Alta

### Classe/Método afetado
CustomerService.deposit()

### Pré-condições
Cliente cadastrado com saldo positivo.

### Passos para reproduzir
1. Criar cliente com saldo 100.
2. Realizar depósito de -50.
3. Verificar saldo final.

### Resultado atual
O sistema aceita depósito negativo e reduz saldo do cliente.

### Resultado esperado
O sistema deveria bloquear depósitos negativos lançando BusinessException.

### Evidências
Teste unitário:
deveRejeitarDepositoNegativo()

Trecho responsável:
```java
customer.setBalance(customer.getBalance().add(amount));
```

### Impacto
Permite inconsistência financeira e possível fraude de saldo.

---

## BUG-03 - Sistema permite saque acima do saldo disponível

### Tipo
Negócio

### Severidade
Alta

### Classe/Método afetado
CustomerService.withdraw()

### Pré-condições
Cliente com saldo menor que o valor solicitado para saque.

### Passos para reproduzir
1. Criar cliente com saldo 100.
2. Realizar saque de 500.
3. Verificar saldo final.

### Resultado atual
O sistema permite saque mesmo sem saldo suficiente.

### Resultado esperado
O sistema deveria bloquear operação lançando BusinessException.

### Evidências
Teste unitário:
deveRejeitarSaqueMaiorQueSaldo()

Trecho responsável:
```java
if (customer.getBalance().compareTo(amount) < -1)
```

### Impacto
Permite saldo negativo e inconsistência financeira.

---

# Cenários Positivos Implementados

## CustomerService

### Testes implementados
- deveCriarClienteComSucesso
- deveCriarClienteComSaldoZeroQuandoSaldoInicialForNulo
- deveBuscarClientePorIdComSucesso
- deveRealizarDepositoComSucesso
- deveRealizarSaqueComSucesso
- deveRejeitarEmailDuplicado

### Objetivo
Validar funcionamento correto das principais operações do serviço de clientes.

---

# Considerações Finais

Os testes implementados cobriram:
- criação de clientes
- validação de email
- saldo inicial
- depósito
- saque
- busca de cliente
- tratamento de exceções

Foram identificados defeitos relacionados principalmente a:
- inconsistência financeira
- validação insuficiente
- regras frágeis de negócio

Os bugs encontrados foram documentados utilizando evidências reproduzíveis através de testes unitários automatizados.