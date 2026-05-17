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

# OrderService

---

## BUG-04 - Cliente inativo consegue realizar pedido

### Tipo
Negócio

### Severidade
Alta

### Classe/Método afetado
OrderService.create()

### Pré-condições
Cliente cadastrado com status inativo.

### Passos para reproduzir
1. Criar cliente com `active = false`
2. Criar pedido para esse cliente
3. Executar criação do pedido

### Resultado atual
O sistema permite que cliente inativo realize pedido.

### Resultado esperado
O sistema deveria bloquear o pedido lançando BusinessException.

### Evidências
Teste unitário:
```java
deveRejeitarPedidoParaClienteInativo()
```

Resultado do teste:
```text
Expected BusinessException to be thrown, but nothing was thrown.
```

### Impacto
Permite compras por clientes bloqueados/inativos.

---

## BUG-05 - Total do pedido ignora quantidade do item

### Tipo
Negócio

### Severidade
Alta

### Classe/Método afetado
OrderService.create()

### Pré-condições
Produto ativo com preço unitário 100 e estoque suficiente.

### Passos para reproduzir
1. Criar pedido com produto de valor 100
2. Informar quantidade 2
3. Executar criação do pedido

### Resultado atual
O total do pedido é calculado como 100.

### Resultado esperado
O total deveria ser 200.

### Evidências
Teste unitário:
```java
deveCalcularTotalConsiderandoQuantidade()
```

Resultado do teste:
```text
Esperado: 200
Atual: 100
```

### Impacto
Pedidos com múltiplas unidades são cobrados incorretamente.

---

## BUG-06 - Estoque não é reduzido após compra

### Tipo
Negócio / Dados

### Severidade
Alta

### Classe/Método afetado
OrderService.create()

### Pré-condições
Produto ativo com estoque 10.

### Passos para reproduzir
1. Criar pedido com quantidade 2
2. Finalizar pedido
3. Consultar estoque do produto

### Resultado atual
O estoque permanece 10.

### Resultado esperado
O estoque deveria ser reduzido para 8.

### Evidências
Teste unitário:
```java
deveBaixarEstoqueAposCriarPedido()
```

Resultado do teste:
```text
Esperado: 8
Atual: 10
```

### Impacto
Permite vendas acima do estoque real.

---

## BUG-07 - Cupom expirado é aceito pelo sistema

### Tipo
Negócio

### Severidade
Média

### Classe/Método afetado
OrderService.create()

### Pré-condições
Cupom expirado cadastrado no sistema.

### Passos para reproduzir
1. Criar cupom expirado
2. Aplicar cupom em pedido
3. Finalizar pedido

### Resultado atual
O sistema aplica desconto normalmente.

### Resultado esperado
O sistema deveria rejeitar o cupom expirado lançando BusinessException.

### Evidências
Teste unitário:
```java
deveRejeitarCupomExpirado()
```

Resultado do teste:
```text
Expected BusinessException to be thrown, but nothing was thrown.
```

### Impacto
Permite descontos indevidos.

---

## BUG-08 - Pedido pode ser aprovado com total negativo

### Tipo
Negócio

### Severidade
Alta

### Classe/Método afetado
OrderService.create()

### Pré-condições
Cupom ativo com desconto superior a 100%.

### Passos para reproduzir
1. Criar produto de valor 100
2. Criar cupom com desconto de 120%
3. Aplicar cupom ao pedido
4. Finalizar pedido

### Resultado atual
O pedido é aprovado com total negativo.

### Resultado esperado
O sistema deveria bloquear pedidos com total abaixo de zero.

### Evidências
Teste unitário:
```java
deveRejeitarPedidoComTotalNegativoPorCupomAcimaDeCemPorCento()
```

Resultado do teste:
```text
Expected BusinessException to be thrown, but nothing was thrown.
```

### Impacto
Gera inconsistência financeira e pedidos inválidos.
---

# Considerações Finais

Os testes implementados cobriram cenários críticos relacionados a:
- validação de clientes
- operações financeiras
- criação de pedidos
- cálculo de totais
- estoque
- aplicação de cupons
- tratamento de exceções

Foram identificados defeitos relevantes de negócio e consistência de dados, principalmente relacionados a:
- validações insuficientes
- inconsistência financeira
- regras frágeis de negócio
- integridade de estoque
- aprovação indevida de pedidos

Os bugs encontrados foram documentados utilizando:
- testes unitários automatizados
- evidências reproduzíveis
- mensagens de erro
- comparação entre comportamento esperado e comportamento atual

Os testes priorizaram qualidade de cenários críticos ao invés de quantidade excessiva de casos superficiais.