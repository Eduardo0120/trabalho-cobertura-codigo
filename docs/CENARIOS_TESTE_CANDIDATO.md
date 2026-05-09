# Cenarios sugeridos para cobertura

Este roteiro ajuda o candidato a planejar testes que cubram caminhos felizes, erros de negocio, erros tecnicos e respostas HTTP. Ele nao substitui a analise do codigo: os cenarios abaixo sao pontos de partida, e o candidato pode propor casos adicionais.

## Como usar

- Priorize testes unitarios nos services para validar regras de negocio.
- Use testes de controller ou integracao quando o objetivo for validar status HTTP e payload de erro.
- Para cada bug encontrado, registre a evidencia no relatorio.
- Cubra pelo menos um cenario de sucesso e um cenario de erro para cada area principal.
- Informe cobertura de linhas, branches e metodos quando possivel.

## Clientes e saldo

| ID | Cenario | Tipo | Entrada sugerida | Resultado esperado pelo negocio | Ponto avaliado |
| --- | --- | --- | --- | --- | --- |
| CUS-01 | Listar clientes cadastrados | Acerto | `GET /api/customers` | Retorna clientes iniciais | Caminho feliz de consulta |
| CUS-02 | Buscar cliente existente | Acerto | `customerId=1` | Retorna Ana Silva | `CustomerService.findById` |
| CUS-03 | Buscar cliente inexistente | Erro | `customerId=999` | Retorna erro de nao encontrado | `NotFoundException` e HTTP 404 |
| CUS-04 | Criar cliente com email valido | Acerto | `novo@email.com` | Cria cliente ativo com saldo inicial | `CustomerService.create` |
| CUS-05 | Criar cliente sem saldo inicial | Acerto | `initialBalance=null` | Cria com saldo `0.00` | Valor padrao |
| CUS-06 | Criar cliente com email sem `@` | Erro | `email=invalido` | Rejeita como email invalido | Validacao basica |
| CUS-07 | Criar cliente com email incompleto | Erro | `email=a@` | Deveria rejeitar, mas pode aceitar | Email fraco |
| CUS-08 | Criar cliente com email duplicado igual | Erro | `ana@email.com` | Rejeita duplicidade | Regra de unicidade |
| CUS-09 | Criar cliente com email duplicado variando maiusculas | Erro | `ANA@email.com` | Deveria rejeitar duplicidade | Unicidade case-insensitive |
| CUS-10 | Depositar valor positivo | Acerto | `customerId=1`, `amount=25.00` | Saldo aumenta | Caminho feliz de deposito |
| CUS-11 | Depositar valor negativo | Erro | `amount=-10.00` | Deveria rejeitar | Deposito negativo |
| CUS-12 | Sacar valor com saldo suficiente | Acerto | `customerId=1`, `amount=50.00` | Saldo diminui | Caminho feliz de saque |
| CUS-13 | Sacar valor acima do saldo | Erro | `customerId=2`, `amount=100.00` | Deveria rejeitar saldo insuficiente | Saldo negativo indevido |
| CUS-14 | Depositar ou sacar para cliente inexistente | Erro | `customerId=999` | Retorna nao encontrado | Propagacao de erro |

## Produtos

| ID | Cenario | Tipo | Entrada sugerida | Resultado esperado pelo negocio | Ponto avaliado |
| --- | --- | --- | --- | --- | --- |
| PRO-01 | Listar produtos | Acerto | `GET /api/products` | Retorna produtos iniciais | Consulta simples |
| PRO-02 | Buscar produto existente | Acerto | `productId=1` | Retorna Mouse QA | `ProductService.findById` |
| PRO-03 | Buscar produto inexistente | Erro | `productId=999` | Retorna nao encontrado | HTTP 404 |
| PRO-04 | Usar produto inativo em pedido | Erro | `productId=4` | Rejeita produto inativo | Regra de produto ativo |
| PRO-05 | Usar produto sem estoque em pedido | Erro | `productId=3`, `quantity=1` | Rejeita estoque insuficiente | Validacao de estoque |

## Pedidos e cupons

| ID | Cenario | Tipo | Entrada sugerida | Resultado esperado pelo negocio | Ponto avaliado |
| --- | --- | --- | --- | --- | --- |
| ORD-01 | Criar pedido simples sem cupom | Acerto | Cliente ativo, produto 1, quantidade 1 | Pedido aprovado com total do item | Caminho feliz |
| ORD-02 | Criar pedido com quantidade maior que 1 | Erro | Produto 1, quantidade 2 | Total deveria ser `199.80` | Subtotal ignora quantidade |
| ORD-03 | Criar pedido com dois itens | Erro | Produto 1 qtd 1 + produto 2 qtd 1 | Total soma os itens | Soma de itens |
| ORD-04 | Criar pedido com cliente inexistente | Erro | `customerId=999` | Retorna nao encontrado | Validacao de cliente |
| ORD-05 | Criar pedido com cliente inativo | Erro | `customerId=3` | Deveria rejeitar cliente inativo | Regra de cliente ativo |
| ORD-06 | Criar pedido com produto inexistente | Erro | `productId=999` | Retorna nao encontrado | Validacao de produto |
| ORD-07 | Criar pedido com quantidade maior que estoque | Erro | Produto 2, quantidade 6 | Rejeita estoque insuficiente | Limite de estoque |
| ORD-08 | Criar pedido e verificar baixa de estoque | Erro | Produto 1, quantidade 2 | Estoque deveria cair de 10 para 8 | Estoque nao baixado |
| ORD-09 | Aplicar cupom valido | Acerto | `couponCode=QA10` | Aplica 10% de desconto | Caminho feliz de cupom |
| ORD-10 | Aplicar cupom inexistente | Erro | `couponCode=NAOEXISTE` | Rejeita cupom invalido | Excecao de negocio |
| ORD-11 | Aplicar cupom expirado | Erro | `couponCode=EXPIRADO50` | Deveria rejeitar ou nao aplicar desconto | Cupom expirado |
| ORD-12 | Aplicar cupom inativo | Erro | `couponCode=INATIVO20` | Nao deve aplicar desconto | Cupom inativo |
| ORD-13 | Aplicar cupom acima de 100% | Erro | `couponCode=SUPER120` | Deveria rejeitar total negativo | Total negativo |
| ORD-14 | Criar pedido com lista vazia | Erro | `items=[]` | Deveria rejeitar pedido sem itens | Pedido vazio aprovado |
| ORD-15 | Criar pedido com lista nula | Erro tecnico | `items=null` | Deveria retornar erro controlado | 500 inesperado |
| ORD-16 | Criar pedido com quantidade zero | Erro | `quantity=0` | Deveria rejeitar quantidade invalida | Falta de validacao |
| ORD-17 | Criar pedido com quantidade negativa | Erro | `quantity=-1` | Deveria rejeitar quantidade invalida | Falta de validacao |

## Relatorios e seguranca

| ID | Cenario | Tipo | Entrada sugerida | Resultado esperado pelo negocio | Ponto avaliado |
| --- | --- | --- | --- | --- | --- |
| REP-01 | Consultar resumo administrativo | Acerto | `GET /api/admin/reports/summary` | Retorna totalizadores | `ReportService.summary` |
| REP-02 | Validar receita apos pedido | Acerto | Criar pedido e consultar resumo | Receita soma pedidos | Agregacao |
| REP-03 | Acessar relatorio sem autenticacao | Erro de seguranca | Sem token/credencial | Deveria bloquear acesso administrativo | Falta de autenticacao |
| REP-04 | Verificar exposicao de nota interna | Erro de dados | `internalNote` na resposta | Nao deveria expor dado interno | Vazamento de informacao |

## Tratamento de erros HTTP

| ID | Cenario | Tipo | Entrada sugerida | Resultado esperado pelo negocio | Ponto avaliado |
| --- | --- | --- | --- | --- | --- |
| HTTP-01 | Recurso inexistente | Acerto | Cliente/produto inexistente | HTTP 404 com `ErrorResponse` | `ApiExceptionHandler` |
| HTTP-02 | Erro de negocio | Acerto | Produto inativo ou cupom invalido | HTTP 400 com `ErrorResponse` | `BusinessException` |
| HTTP-03 | Payload incompleto em saldo | Erro tecnico | `{}` em deposito | Deveria retornar 400 controlado | Nulo sem tratamento |
| HTTP-04 | Payload malformado | Erro tecnico | JSON invalido | Deveria retornar 400 | Robustez da API |
| HTTP-05 | Excecao inesperada | Erro tecnico | Pedido com `items=null` | Deveria retornar resposta padronizada | Falta handler generico |

## Pontos de acerto esperados

- Testes nomeados pelo comportamento, por exemplo `deveRejeitarSaqueQuandoSaldoInsuficiente`.
- Separacao clara entre caminho feliz, erro de negocio e erro tecnico.
- Uso de `assertThrows` para services e validacao de status/payload para controllers.
- Verificacao de valores monetarios com `BigDecimal.compareTo` ou escala controlada.
- Massa de dados pequena e explicita no teste.
- Cobertura de regras com branches relevantes, nao apenas chamada de metodos.
- Relatorio de bugs com passos reproduziveis e evidencia automatizada.
- Identificacao de severidade coerente com impacto no negocio.

## Pontos de erro comuns

- Testar apenas `contextLoads` ou apenas endpoints sem validar regra de negocio.
- Considerar comportamento atual defeituoso como esperado sem questionar a regra.
- Nao validar efeitos colaterais, como baixa de estoque e saldo atualizado.
- Nao testar limites: zero, negativo, nulo, vazio, inexistente e duplicado.
- Informar cobertura sem anexar evidencia ou sem indicar linhas/branches/metodos.
- Misturar bug de desenvolvimento com bug de negocio sem justificar.
- Criar testes dependentes de ordem entre si ou de dados alterados por teste anterior.
- Corrigir o codigo da aplicacao quando a tarefa era identificar e evidenciar bugs.
