package redestransporte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Grafo {

    // Guarda as estações usando o ID como chave (acesso rápido)
    private Map<Integer, Estacao> estacoes;
    // Guarda todas as conexões (arestas)
    private List<Conexao> conexoes;
    // Mapas para os IDs (útil para matrizes)
    private Map<Integer, Integer> idParaIndice; // Mapeia ID da Estacao para o índice da matriz (0, 1, 2...)
    private Map<Integer, Integer> indiceParaId; // Mapeia o índice da matriz de volta para o ID da Estacao
    
    // Representações do grafo
    private int[][] matrizAdjacencia;
    private int[][] matrizIncidencia;

    public Grafo() {
        this.estacoes = new HashMap<>();
        this.conexoes = new ArrayList<>();
        this.idParaIndice = new HashMap<>();
        this.indiceParaId = new HashMap<>();
    }

    // --- MÉTODOS DE INICIALIZAÇÃO ---

    public void adicionarEstacao(Estacao estacao) {
        estacoes.put(estacao.getId(), estacao);
    }

    public void adicionarConexao(Conexao conexao) {
        // Verifica se as estações da conexão existem
        if (estacoes.containsKey(conexao.getIdOrigem()) && estacoes.containsKey(conexao.getIdDestino())) {
            conexoes.add(conexao);
        } else {
            System.err.println("Conexão inválida: Estação não encontrada. " + conexao);
        }
    }

    // --- MÉTODOS DE GERAÇÃO DAS MATRIZES (Chamados no carregarDados) ---

    private void construirMapeamentoIndices() {
        // Limpa mapas antigos
        idParaIndice.clear();
        indiceParaId.clear();
        
        // Pega os IDs das estações, ordena e cria os mapas
        List<Integer> idsOrdenados = new ArrayList<>(estacoes.keySet());
        Collections.sort(idsOrdenados);
        
        int indice = 0;
        for (int id : idsOrdenados) {
            idParaIndice.put(id, indice);
            indiceParaId.put(indice, id);
            indice++;
        }
    }

    public void gerarMatrizAdjacencia() {
        construirMapeamentoIndices(); // Garante que os mapas de índice estão criados
        int numEstacoes = estacoes.size();
        matrizAdjacencia = new int[numEstacoes][numEstacoes];

        // Inicializa com 0 (sem conexão)
        for (int i = 0; i < numEstacoes; i++) {
            Arrays.fill(matrizAdjacencia[i], 0);
        }

        // Preenche com os pesos
        for (Conexao c : conexoes) {
            int idxOrigem = idParaIndice.get(c.getIdOrigem());
            int idxDestino = idParaIndice.get(c.getIdDestino());
            
            // Adiciona o peso nos dois sentidos (grafo não direcionado)
            matrizAdjacencia[idxOrigem][idxDestino] = c.getPeso();
            matrizAdjacencia[idxDestino][idxOrigem] = c.getPeso(); 
        }
    }

    public void gerarMatrizIncidencia() {
        if (conexoes.isEmpty() || estacoes.isEmpty()) return;
        
        construirMapeamentoIndices(); // Garante que os mapas de índice estão criados
        int numEstacoes = estacoes.size();
        int numConexoes = conexoes.size();
        matrizIncidencia = new int[numEstacoes][numConexoes];

        // Inicializa com 0
        for (int i = 0; i < numEstacoes; i++) {
            Arrays.fill(matrizIncidencia[i], 0);
        }

        // Preenche a matriz
        for (int j = 0; j < numConexoes; j++) {
            Conexao c = conexoes.get(j);
            int idxOrigem = idParaIndice.get(c.getIdOrigem());
            int idxDestino = idParaIndice.get(c.getIdDestino());

            // 1 para saída (origem), -1 para entrada (destino)
            // (Ou ambos 1 se não quiser indicar direção)
            matrizIncidencia[idxOrigem][j] = 1; 
            matrizIncidencia[idxDestino][j] = -1; 
        }
    }

    // --- MÉTODOS PARA O MENU 1 (Representações) ---

    public String getMatrizAdjacencia() {
        return formatarMatriz("Matriz de Adjacência (IDs)", matrizAdjacencia);
    }

    public String getMatrizIncidencia() {
        // Cabeçalho para a Matriz de Incidência (mostrando as conexões)
        StringBuilder header = new StringBuilder("        "); // Espaço para os IDs das estações
        for(int j = 0; j < conexoes.size(); j++) {
            header.append(String.format("C%02d   ", j+1)); // C01, C02...
        }
        header.append("\n");
        
        String matrizFormatada = formatarMatriz("", matrizIncidencia, false);
        return "Matriz de Incidência\n\n" + header.toString() + matrizFormatada;
    }

    public String getListaArestas() {
        StringBuilder sb = new StringBuilder("Lista de Arestas (Conexões):\n\n");
        for (Conexao c : conexoes) {
            String nomeOrigem = estacoes.get(c.getIdOrigem()).getNome();
            String nomeDestino = estacoes.get(c.getIdDestino()).getNome();
            sb.append(String.format("[%d] %s <--(Peso: %d)--> [%d] %s\n",
                c.getIdOrigem(), nomeOrigem, c.getPeso(), c.getIdDestino(), nomeDestino));
        }
        return sb.toString();
    }

    public String getListaSucessores() {
        StringBuilder sb = new StringBuilder("Lista de Sucessores (Adjacências):\n\n");
        int numEstacoes = estacoes.size();

        for (int i = 0; i < numEstacoes; i++) {
            int idOrigem = indiceParaId.get(i);
            String nomeOrigem = estacoes.get(idOrigem).getNome();
            sb.append(String.format("[%d] %s -> ", idOrigem, nomeOrigem));

            boolean temSucessor = false;
            for (int j = 0; j < numEstacoes; j++) {
                if (matrizAdjacencia[i][j] > 0) { // Se há conexão
                    int idDestino = indiceParaId.get(j);
                    String nomeDestino = estacoes.get(idDestino).getNome();
                    sb.append(String.format("[%d] %s (Peso: %d) | ", idDestino, nomeDestino, matrizAdjacencia[i][j]));
                    temSucessor = true;
                }
            }
            if (!temSucessor) {
                sb.append("(Nenhum)");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // --- MÉTODOS PARA O MENU 2 (Operações) ---

    public String calcularGraus() {
        StringBuilder sb = new StringBuilder("Grau dos Vértices (Estações):\n\n");
        int numEstacoes = estacoes.size();
        
        for (int i = 0; i < numEstacoes; i++) {
            int grau = 0;
            for (int j = 0; j < numEstacoes; j++) {
                if (matrizAdjacencia[i][j] > 0) {
                    grau++;
                }
            }
            int id = indiceParaId.get(i);
            String nome = estacoes.get(id).getNome();
            sb.append(String.format("[%d] %s: Grau %d\n", id, nome, grau));
        }
        return sb.toString();
    }

    // BFS (Busca em Largura) - Caminho mais curto em número de paradas
    public String bfs(int idOrigem, int idDestino) {
        if (!idParaIndice.containsKey(idOrigem) || !idParaIndice.containsKey(idDestino)) {
            return "Erro: ID de origem ou destino não encontrado.";
        }

        int idxOrigem = idParaIndice.get(idOrigem);
        int idxDestino = idParaIndice.get(idDestino);
        int numEstacoes = estacoes.size();

        Queue<Integer> fila = new LinkedList<>();
        int[] anterior = new int[numEstacoes]; // Guarda o "pai" de cada nó no caminho
        boolean[] visitado = new boolean[numEstacoes];

        Arrays.fill(anterior, -1);
        Arrays.fill(visitado, false);

        fila.add(idxOrigem);
        visitado[idxOrigem] = true;

        while (!fila.isEmpty()) {
            int idxAtual = fila.poll();

            if (idxAtual == idxDestino) {
                break; // Achou o destino
            }

            // Para cada vizinho do nó atual
            for (int idxVizinho = 0; idxVizinho < numEstacoes; idxVizinho++) {
                // Se existe conexão (peso > 0) e ainda não foi visitado
                if (matrizAdjacencia[idxAtual][idxVizinho] > 0 && !visitado[idxVizinho]) {
                    visitado[idxVizinho] = true;
                    anterior[idxVizinho] = idxAtual;
                    fila.add(idxVizinho);
                }
            }
        }

        // Monta o caminho de volta
        return construirCaminho(idxOrigem, idxDestino, anterior, null, false);
    }
    
    // Dijkstra - Caminho mais curto por peso (distância)
    public String dijkstra(int idOrigem, int idDestino) {
        if (!idParaIndice.containsKey(idOrigem) || !idParaIndice.containsKey(idDestino)) {
            return "Erro: ID de origem ou destino não encontrado.";
        }
        
        int idxOrigem = idParaIndice.get(idOrigem);
        int idxDestino = idParaIndice.get(idDestino);
        int numEstacoes = estacoes.size();

        int[] distancias = new int[numEstacoes];
        int[] anterior = new int[numEstacoes];
        boolean[] visitado = new boolean[numEstacoes];
        
        // Fila de prioridade para pegar sempre o nó com menor distância
        // Guarda um array [idEstacao, distancia]
        PriorityQueue<int[]> filaPrioridade = new PriorityQueue<>((a, b) -> a[1] - b[1]);

        Arrays.fill(distancias, Integer.MAX_VALUE);
        Arrays.fill(anterior, -1);
        Arrays.fill(visitado, false);

        distancias[idxOrigem] = 0;
        filaPrioridade.add(new int[]{idxOrigem, 0});

        while (!filaPrioridade.isEmpty()) {
            int[] parAtual = filaPrioridade.poll();
            int idxAtual = parAtual[0];

            if (visitado[idxAtual]) {
                continue;
            }
            visitado[idxAtual] = true;

            if (idxAtual == idxDestino) {
                break; // Achou o destino
            }

            // Para cada vizinho
            for (int idxVizinho = 0; idxVizinho < numEstacoes; idxVizinho++) {
                int peso = matrizAdjacencia[idxAtual][idxVizinho];
                
                // Se existe conexão (peso > 0)
                if (peso > 0 && !visitado[idxVizinho]) {
                    int novaDistancia = distancias[idxAtual] + peso;
                    
                    if (novaDistancia < distancias[idxVizinho]) {
                        distancias[idxVizinho] = novaDistancia;
                        anterior[idxVizinho] = idxAtual;
                        filaPrioridade.add(new int[]{idxVizinho, novaDistancia});
                    }
                }
            }
        }
        
        // Monta o caminho de volta
        return construirCaminho(idxOrigem, idxDestino, anterior, distancias, true);
    }


    // --- MÉTODOS AUXILIARES ---

    // Formata uma matriz para exibição
    private String formatarMatriz(String titulo, int[][] matriz) {
        return formatarMatriz(titulo, matriz, true);
    }
    
    private String formatarMatriz(String titulo, int[][] matriz, boolean mostrarHeaderIds) {
        if (matriz == null) {
            return titulo + "\nMatriz ainda não gerada.";
        }
        
        StringBuilder sb = new StringBuilder(titulo + "\n\n");
        int numLinhas = matriz.length;
        if (numLinhas == 0) return sb.toString();
        int numColunas = matriz[0].length;

        // Cabeçalho das colunas (IDs das Estações)
        if(mostrarHeaderIds) {
            sb.append("        "); // Espaço para o ID da linha
            for (int j = 0; j < numColunas; j++) {
                sb.append(String.format("ID %-3d ", indiceParaId.get(j)));
            }
            sb.append("\n");
        }

        // Linhas
        for (int i = 0; i < numLinhas; i++) {
            // Cabeçalho da linha (ID da Estação)
            sb.append(String.format("ID %-3d | ", indiceParaId.get(i)));
            
            // Valores
            for (int j = 0; j < numColunas; j++) {
                sb.append(String.format("%-6d ", matriz[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    // Constroi a string do caminho (usado por BFS e Dijkstra)
    private String construirCaminho(int idxOrigem, int idxDestino, int[] anterior, int[] distancias, boolean usarPeso) {
        List<Integer> caminhoIndices = new ArrayList<>();
        int idxAtual = idxDestino;

        if (anterior[idxAtual] == -1 && idxAtual != idxOrigem) {
            return "Caminho não encontrado da estação " + estacoes.get(indiceParaId.get(idxOrigem)).getNome() +
                   " para " + estacoes.get(indiceParaId.get(idxDestino)).getNome() + ".";
        }

        // Reconstrói o caminho de trás para frente
        while (idxAtual != -1) {
            caminhoIndices.add(idxAtual);
            idxAtual = anterior[idxAtual];
        }
        Collections.reverse(caminhoIndices); // Inverte para (Origem -> Destino)

        // Monta a string de resultado
        StringBuilder sb = new StringBuilder();
        String tipoCaminho = usarPeso ? "Caminho por Distância (Dijkstra)" : "Caminho por Paradas (BFS)";
        sb.append(tipoCaminho + "\n\n");
        
        int custoTotal = 0;
        
        for (int i = 0; i < caminhoIndices.size(); i++) {
            int idx = caminhoIndices.get(i);
            int idEstacao = indiceParaId.get(idx);
            Estacao estacao = estacoes.get(idEstacao);
            sb.append(String.format("[%d] %s", idEstacao, estacao.getNome()));

            if (i < caminhoIndices.size() - 1) {
                if(usarPeso) {
                    int idxProximo = caminhoIndices.get(i+1);
                    int peso = matrizAdjacencia[idx][idxProximo];
                    custoTotal += peso;
                    sb.append(String.format(" --(Peso: %d)-->\n", peso));
                } else {
                     sb.append(" -->\n");
                }
            }
        }
        
        sb.append("\n\n--- RESUMO ---\n");
        if(usarPeso) {
             sb.append("Custo Total (Distância): " + custoTotal + "\n");
        }
        sb.append("Número de Paradas: " + (caminhoIndices.size() - 1) + "\n");

        return sb.toString();
    }
}