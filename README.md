# Projeto Prático em Java – Representação e Análise de Redes de Transporte

# Integrantes: 
## Isadora Carvalho, Maria Julia Loureiro, Mike Willy, Rafaela Riganti e Sophia Araujo
## 🎯 Objetivo Geral

Desenvolver um sistema em Java para representar e analisar uma rede de transporte urbano (ônibus, coleta de recicláveis ou logística), utilizando estruturas de grafos e árvores, leitura de arquivos `.txt`, representação gráfica da rede e pesquisa sobre análise de complexidade.

## 📂 Arquivos de Entrada

**estacoes.txt**
Formato: `<id>;<nomeLocal>`
Exemplo:

```
1;Terminal Central
2;Ecoponto Norte
3;Ecoponto Sul
```

**conexoes.txt**
Formato: `<idOrigem>;<idDestino>;<peso>`
Exemplo:

```
1;2;10
1;3;15
2;4;12
```

## 📌 Representação Gráfica

Diagrama visual obrigatório contendo:

* Nós (estações/pontos)
* Arestas (conexões)
* Pesos (tempo / distância / custo)
  Será incluído no relatório para validar os dados implementados.

## ✅ Funcionalidades Obrigatórias

* Leitura de arquivos `.txt`
* Representação do grafo em:

  * Matriz de Adjacência
  * Matriz de Incidência
  * Lista de Arestas
  * Lista de Sucessores
* Operações:

  * Grau de cada ponto
  * Caminho mais curto (BFS ou Dijkstra)
* Árvores:

  * BST (busca por nome do ponto)
  * Trie (busca por prefixo)
* Relatório com:

  * Diagrama gráfico
  * Pesquisa sobre complexidade
  * Classificação dos algoritmos
  * Exemplos de execução

## 🌟 Funcionalidades Diferenciadas (mín. 2)

***

## 📊 Entregáveis

* Código-fonte em Java
* Arquivos `.txt`
* Relatório (até 5 páginas)
* Apresentação com demonstração (slides)

## 📅 Prazo Final

Entrega e apresentação: **23/10**
Formato: **pacote .zip** contendo código + arquivos .txt + relatório + slides.

