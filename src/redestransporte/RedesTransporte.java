package redestransporte;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.io.IOException;
import java.util.List;

/**
 * Classe principal do sistema de Redes de Transporte
 * @author elian
 */
public class RedesTransporte {
    private static Grafo grafo;
    private static BST bst;
    private static Trie trie;
    
    public static void main(String[] args) {
        grafo = new Grafo();
        bst = new BST();
        trie = new Trie();
        
        // Carrega os dados dos arquivos
        if (!carregarDados()) {
            return;
        }
        
        // Menu principal
        boolean continuar = true;
        while (continuar) {
            String[] opcoes = {
                "1. Ver Representações do Grafo",
                "2. Operações sobre o Grafo",
                "3. Buscar Estações (BST/Trie)",
                "4. Gerenciar Rede (CRUD)", 
                "5. Sair" 
            };
            
            String escolha = (String) JOptionPane.showInputDialog(
                null,
                "=== SISTEMA DE REDES DE TRANSPORTE ===\n\nEscolha uma opção:",
                "Menu Principal",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );
            
            if (escolha == null) {
                continuar = false;
            } else if (escolha.startsWith("1")) {
                menuRepresentacoes();
            } else if (escolha.startsWith("2")) {
                menuOperacoes();
            } else if (escolha.startsWith("3")) {
                menuBuscas();
            } else if (escolha.startsWith("4")) { 
                menuGerenciamento();
            } else if (escolha.startsWith("5")) { 
                continuar = false;
            }
        }
        
        JOptionPane.showMessageDialog(null, "Obrigado por usar o sistema!");
    }
    
    private static boolean carregarDados() {
        try { 
            List<Estacao> estacoes = LeitorArquivos.lerEstacoes("estacoes.txt");
            for (Estacao e : estacoes) {
                grafo.adicionarEstacao(e);
                bst.inserir(e);
                trie.inserir(e);
            }
            List<Conexao> conexoes = LeitorArquivos.lerConexoes("conexoes.txt");
            for (Conexao c : conexoes) {
                grafo.adicionarConexao(c);
            }
            // Gera as matrizes
            grafo.gerarMatrizAdjacencia();
            grafo.gerarMatrizIncidencia();
            JOptionPane.showMessageDialog(
                    null,
                    "Dados carregados com sucesso!\n\n" +
                            "Estações: " + estacoes.size() + "\n" +
                                    "Conexões: " + conexoes.size(),
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
            
        } catch (IOException e) { 
            mostrarTextoGrande(
                "ERRO ao carregar arquivos!\n\n" +
                "Certifique-se de que os arquivos 'estacoes.txt' e 'conexoes.txt'\n" +
                "estão no diretório raiz do projeto.\n\n" +
                "Erro: " + e.getMessage(),
                "Erro de Arquivo"
            );
            return false;
        }
    }
    
    // --- MÉTODOS DOS MENUS (sem alterações) ---
    
    private static void menuRepresentacoes() {
        String[] opcoes = {
            "Matriz de Adjacência",
            "Matriz de Incidência",
            "Lista de Arestas",
            "Lista de Sucessores",
            "Voltar"
        };
        
        String escolha = (String) JOptionPane.showInputDialog(
            null, "Escolha a representação:", "Representações do Grafo",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) return;
        
        String resultado = "";
        switch (escolha) {
            case "Matriz de Adjacência": resultado = grafo.getMatrizAdjacencia(); break;
            case "Matriz de Incidência": resultado = grafo.getMatrizIncidencia(); break;
            case "Lista de Arestas": resultado = grafo.getListaArestas(); break;
            case "Lista de Sucessores": resultado = grafo.getListaSucessores(); break;
        }
        mostrarTextoGrande(resultado, "Representação");
    }
    
    private static void menuOperacoes() {
        String[] opcoes = {
            "Calcular Grau dos Vértices",
            "Caminho Mais Curto (BFS)",
            "Caminho Mais Curto (Dijkstra)",
            "Voltar"
        };
        
        String escolha = (String) JOptionPane.showInputDialog(
            null, "Escolha a operação:", "Operações sobre o Grafo",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) return;
        
        if (escolha.startsWith("Calcular")) {
            mostrarTextoGrande(grafo.calcularGraus(), "Grau dos Vértices");
        } else if (escolha.contains("BFS")) {
            calcularCaminho(false);
        } else if (escolha.contains("Dijkstra")) {
            calcularCaminho(true);
        }
    }
    
    private static void calcularCaminho(boolean usarDijkstra) {
        String origemStr = JOptionPane.showInputDialog(null, "Digite o ID da estação de origem:", "Origem", JOptionPane.QUESTION_MESSAGE);
        if (origemStr == null) return;
        String destinoStr = JOptionPane.showInputDialog(null, "Digite o ID da estação de destino:", "Destino", JOptionPane.QUESTION_MESSAGE);
        if (destinoStr == null) return;
        
        try {
            int origem = Integer.parseInt(origemStr.trim());
            int destino = Integer.parseInt(destinoStr.trim());
            String resultado = usarDijkstra ? grafo.dijkstra(origem, destino) : grafo.bfs(origem, destino);
            String titulo = usarDijkstra ? "Dijkstra (com pesos)" : "BFS (sem pesos)";
            mostrarTextoGrande(resultado, titulo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, digite IDs válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void menuBuscas() {
        String[] opcoes = {
            "Buscar por Nome Completo (BST)",
            "Buscar por Prefixo (Trie)",
            "Listar Todas (BST em ordem)",
            "Voltar"
        };
        
        String escolha = (String) JOptionPane.showInputDialog(
            null, "Escolha o tipo de busca:", "Buscar Estações",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) return;
        
        if (escolha.contains("Nome Completo")) {
            String nome = JOptionPane.showInputDialog(null, "Digite o nome da estação:", "Busca BST", JOptionPane.QUESTION_MESSAGE);
            if (nome != null && !nome.trim().isEmpty()) {
                Estacao resultado = bst.buscar(nome.trim());
                if (resultado != null) {
                    JOptionPane.showMessageDialog(null, "Estação encontrada:\n\n" + resultado.toString(), "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Estação não encontrada: " + nome, "Resultado da Busca", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (escolha.contains("Prefixo")) {
            String prefixo = JOptionPane.showInputDialog(null, "Digite o prefixo para buscar:", "Busca Trie", JOptionPane.QUESTION_MESSAGE);
            if (prefixo != null && !prefixo.trim().isEmpty()) {
                String resultado = trie.buscarPorPrefixoFormatado(prefixo.trim());
                mostrarTextoGrande(resultado, "Resultado da Busca por Prefixo");
            }
        } else if (escolha.contains("Listar")) {
            String resultado = bst.emOrdem();
            mostrarTextoGrande(resultado, "Todas as Estações");
        }
    }
    
    // --- MÉTODOS CRUD (ATUALIZADOS) ---

    /**
     * Menu para Adicionar/Remover Estações e Conexões.
     */
    private static void menuGerenciamento() {
        String[] opcoes = {
            "Adicionar Estação",
            "Adicionar Conexão",
            "Remover Estação",
            "Remover Conexão",
            "Voltar"
        };
        
        String escolha = (String) JOptionPane.showInputDialog(
            null, "Escolha uma operação de gerenciamento:", "Gerenciar Rede (CRUD)",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) return;

        switch (escolha) {
            case "Adicionar Estação": crudAdicionarEstacao(); break;
            case "Adicionar Conexão": crudAdicionarConexao(); break;
            case "Remover Estação": crudRemoverEstacao(); break;
            case "Remover Conexão": crudRemoverConexao(); break;
        }
    }

    // --- MÉTODO ATUALIZADO ---
    private static void crudAdicionarEstacao() {
        try {
            // 1. Pega o maior ID em uso e soma 1
            int id = grafo.getMaiorIdEstacao() + 1;

            // 2. Pede apenas o nome ao usuário, informando qual será o ID
            String nome = JOptionPane.showInputDialog(null, 
                "O ID da nova estação será: " + id + "\n\nDigite o Nome da nova estação:", 
                "Adicionar Estação", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (nome == null || nome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Adição cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // 3. Cria a nova estação
            Estacao novaEstacao = new Estacao(id, nome.trim());
            grafo.adicionarEstacao(novaEstacao);
            
            // 4. Ressincroniza todas as estruturas
            resincronizarEstruturas();
            
            JOptionPane.showMessageDialog(null, 
                "Estação adicionada com sucesso!\n\n" + novaEstacao.toString(), 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    // --- FIM DA ATUALIZAÇÃO ---

    private static void crudAdicionarConexao() {
        try {
            String origemStr = JOptionPane.showInputDialog(null, "Digite o ID da estação de Origem:", "Adicionar Conexão", JOptionPane.QUESTION_MESSAGE);
            if (origemStr == null) return;
            int idOrigem = Integer.parseInt(origemStr.trim());

            String destinoStr = JOptionPane.showInputDialog(null, "Digite o ID da estação de Destino:", "Adicionar Conexão", JOptionPane.QUESTION_MESSAGE);
            if (destinoStr == null) return;
            int idDestino = Integer.parseInt(destinoStr.trim());

            String pesoStr = JOptionPane.showInputDialog(null, "Digite o Peso (distância/custo):", "Adicionar Conexão", JOptionPane.QUESTION_MESSAGE);
            if (pesoStr == null) return;
            int peso = Integer.parseInt(pesoStr.trim());
            
            // Verifica se as estações existem
            if (grafo.getEstacaoPorId(idOrigem) == null || grafo.getEstacaoPorId(idDestino) == null) {
                 JOptionPane.showMessageDialog(null, "Erro: ID de origem ou destino não existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // Adiciona a conexão
            grafo.adicionarConexao(new Conexao(idOrigem, idDestino, peso));
            
            // Ressincroniza (só as matrizes, bst/trie não mudam)
            grafo.gerarMatrizAdjacencia();
            grafo.gerarMatrizIncidencia();
            
            JOptionPane.showMessageDialog(null, "Conexão adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valores inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void crudRemoverEstacao() {
         try {
            String idStr = JOptionPane.showInputDialog(null, "Digite o ID da estação a ser REMOVIDA:", "Remover Estação", JOptionPane.WARNING_MESSAGE);
            if (idStr == null) return;
            int id = Integer.parseInt(idStr.trim());
            
            // Tenta remover
            if (grafo.removerEstacao(id)) {
                // Se removeu, precisa ressincronizar TUDO
                resincronizarEstruturas();
                JOptionPane.showMessageDialog(null, "Estação e suas conexões removidas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro: Estação com ID " + id + " não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void crudRemoverConexao() {
        try {
            String origemStr = JOptionPane.showInputDialog(null, "Digite o ID de Origem da conexão a ser REMOVIDA:", "Remover Conexão", JOptionPane.WARNING_MESSAGE);
            if (origemStr == null) return;
            int idOrigem = Integer.parseInt(origemStr.trim());

            String destinoStr = JOptionPane.showInputDialog(null, "Digite o ID de Destino da conexão a ser REMOVIDA:", "Remover Conexão", JOptionPane.WARNING_MESSAGE);
            if (destinoStr == null) return;
            int idDestino = Integer.parseInt(destinoStr.trim());
            
            // Tenta remover
            if (grafo.removerConexao(idOrigem, idDestino)) {
                // Se removeu, só precisa refazer as matrizes
                grafo.gerarMatrizAdjacencia();
                grafo.gerarMatrizIncidencia();
                JOptionPane.showMessageDialog(null, "Conexão removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                 JOptionPane.showMessageDialog(null, "Erro: Conexão não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valores inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método central para recriar todas as estruturas após uma mudança
     * (principalmente remoção/adição de estação).
     */
    private static void resincronizarEstruturas() {
        // 1. Recria as matrizes no Grafo
        grafo.gerarMatrizAdjacencia();
        grafo.gerarMatrizIncidencia();

        // 2. Recria a BST e a Trie do zero
        bst = new BST();
        trie = new Trie();
        
        // 3. Repopula a BST e a Trie com os dados atualizados do Grafo
        for (Estacao e : grafo.getTodasEstacoes()) {
            bst.inserir(e);
            trie.inserir(e);
        }
        System.out.println("Estruturas ressincronizadas.");
    }

    // --- FIM DOS MÉTODOS CRUD ---

    
    private static void mostrarTextoGrande(String texto, String titulo) {
        JTextArea textArea = new JTextArea(texto);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
        
        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            titulo,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}