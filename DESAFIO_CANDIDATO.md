# Desafio do Candidato

## Objetivo

Voce recebeu uma API backend em Java 21 que simula regras de clientes, saldo, produtos, pedidos, cupons e relatorios. Seu trabalho e avaliar a qualidade tecnica e de negocio do sistema.

## Tarefas obrigatorias

1. Fazer fork do projeto.
2. Criar testes unitarios para os servicos e regras de negocio.
3. Rodar a suite de testes.
4. Gerar relatorio de cobertura com JaCoCo.
5. Informar o percentual de cobertura obtido.
6. Documentar erros de desenvolvimento e erros de negocio encontrados.
7. Para cada erro, incluir evidencias e cenarios de reproducao.

## Escopo minimo esperado

- Validacao de cliente e email.
- Operacoes de saldo: deposito e saque.
- Criacao de pedido.
- Calculo de total com quantidade.
- Aplicacao de cupom.
- Validacao de estoque.
- Regras de cliente ativo/inativo.
- Relatorios administrativos.
- Tratamento de erros HTTP.

## Cenarios sugeridos

Use [docs/CENARIOS_TESTE_CANDIDATO.md](docs/CENARIOS_TESTE_CANDIDATO.md) como guia para montar sua suite. O guia contem exemplos de cenarios de acerto, erro de negocio, erro tecnico, seguranca e validacao HTTP.

O candidato nao precisa se limitar ao guia. Casos adicionais bem justificados contam positivamente, especialmente quando cobrem limites, efeitos colaterais e regras de negocio nao obvias.

## Entregaveis

- Link do fork.
- Pull request ou branch com os testes criados.
- Relatorio de cobertura.
- Documento com bugs encontrados.

## Criterios de avaliacao

- Cobertura de codigo em services, controllers e handlers de erro.
- Qualidade dos cenarios escolhidos, incluindo caminhos felizes e negativos.
- Clareza dos asserts e independencia entre testes.
- Capacidade de identificar comportamento incorreto mesmo quando o codigo executa sem excecao.
- Evidencias objetivas para cada bug reportado.
- Organizacao do relatorio e severidade coerente com o impacto.

## Formato esperado para bugs

Para cada problema encontrado, informe:

- Titulo.
- Tipo: desenvolvimento, negocio, seguranca, usabilidade de API ou dados.
- Severidade sugerida.
- Classe/metodo ou endpoint afetado.
- Pre-condicoes.
- Passos para reproduzir.
- Resultado atual.
- Resultado esperado.
- Evidencias: teste unitario, print do response, log, payload ou trecho do relatorio.

## Observacoes

Nao e necessario corrigir os bugs. O foco e demonstrar capacidade de analise, criacao de testes, leitura de regras e comunicacao tecnica.
