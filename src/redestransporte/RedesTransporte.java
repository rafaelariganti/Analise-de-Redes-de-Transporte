package redestransporte;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.io.IOException; // Esta importação agora será usada
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
                "4. Sair"
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
                continuar = false;
            }
        }
        
        JOptionPane.showMessageDialog(null, "Obrigado por usar o sistema!");
    }
    
    // --- CORREÇÃO APLICADA AQUI ---
    private static boolean carregarDados() {
        try { // Adiciona o 'try' para capturar erros de IO
            // Lê as estações
            List<Estacao> estacoes = LeitorArquivos.lerEstacoes("estacoes.txt");
            for (Estacao e : estacoes) {
                grafo.adicionarEstacao(e);
                bst.inserir(e);
                trie.inserir(e);
            }
            // Lê as conexões
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
            // Adiciona o 'catch' para tratar o erro se o arquivo não for encontrado
            mostrarTextoGrande(
                "ERRO ao carregar arquivos!\n\n" +
                "Certifique-se de que os arquivos 'estacoes.txt' e 'conexoes.txt'\n" +
                "estão no diretório raiz do projeto.\n\n" +
                "Formato esperado:\n\n" +
                "estacoes.txt:\n" +
                "1;Terminal Central\n" +
                "2;Ecoponto Norte\n\n" +
                "conexoes.txt:\n" +
                "1;2;10\n" +
                "1;3;15\n\n" +
                "Erro: " + e.getMessage(),
                "Erro de Arquivo"
            );
            return false;
        }
    }
    // --- FIM DA CORREÇÃO ---
    
    private static void menuRepresentacoes() {
        String[] opcoes = {
            "Matriz de Adjacência",
            "Matriz de Incidência",
            "Lista de Arestas",
            "Lista de Sucessores",
            "Voltar"
        };
        
        String escolha = (String) JOptionPane.showInputDialog(
            null,
            "Escolha a representação:",
            "Representações do Grafo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) {
            return;
        }
        
        String resultado = "";
        // Usando o seu switch-case
        switch (escolha) {
            case "Matriz de Adjacência":
                resultado = grafo.getMatrizAdjacencia();
                break;
            case "Matriz de Incidência":
                resultado = grafo.getMatrizIncidencia();
                break;
            case "Lista de Arestas":
                resultado = grafo.getListaArestas();
                break;
            case "Lista de Sucessores":
                resultado = grafo.getListaSucessores();
                break;
            default:
                break;
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
            null,
            "Escolha a operação:",
            "Operações sobre o Grafo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) {
            return;
        }
        
        if (escolha.startsWith("Calcular")) {
            String resultado = grafo.calcularGraus();
            mostrarTextoGrande(resultado, "Grau dos Vértices");
        } else if (escolha.contains("BFS")) {
            calcularCaminho(false);
        } else if (escolha.contains("Dijkstra")) {
            calcularCaminho(true);
        }
    }
    
    private static void calcularCaminho(boolean usarDijkstra) {
        String origemStr = JOptionPane.showInputDialog(
            null,
            "Digite o ID da estação de origem:",
            "Origem",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (origemStr == null) return;
        
        String destinoStr = JOptionPane.showInputDialog(
            null,
            "Digite o ID da estação de destino:",
            "Destino",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (destinoStr == null) return;
        
        try {
            int origem = Integer.parseInt(origemStr.trim());
            int destino = Integer.parseInt(destinoStr.trim());
            
            String resultado;
            if (usarDijkstra) {
                resultado = grafo.dijkstra(origem, destino);
            } else {
                resultado = grafo.bfs(origem, destino);
            }
            
            String titulo = usarDijkstra ? "Dijkstra (com pesos)" : "BFS (sem pesos)";
            mostrarTextoGrande(resultado, titulo);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                null,
                "Por favor, digite IDs válidos!",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
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
            null,
            "Escolha o tipo de busca:",
            "Buscar Estações",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );
        
        if (escolha == null || escolha.equals("Voltar")) {
            return;
        }
        
        if (escolha.contains("Nome Completo")) {
            String nome = JOptionPane.showInputDialog(
                null,
                "Digite o nome da estação:",
                "Busca BST",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (nome != null && !nome.trim().isEmpty()) {
                Estacao resultado = bst.buscar(nome.trim());
                if (resultado != null) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Estação encontrada:\n\n" + resultado.toString(),
                        "Resultado da Busca",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Estação não encontrada: " + nome,
                        "Resultado da Busca",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        } else if (escolha.contains("Prefixo")) {
            String prefixo = JOptionPane.showInputDialog(
                null,
                "Digite o prefixo para buscar:",
                "Busca Trie",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (prefixo != null && !prefixo.trim().isEmpty()) {
                String resultado = trie.buscarPorPrefixoFormatado(prefixo.trim());
                mostrarTextoGrande(resultado, "Resultado da Busca por Prefixo");
            }
        } else if (escolha.contains("Listar")) {
            String resultado = bst.emOrdem();
            mostrarTextoGrande(resultado, "Todas as Estações");
        }
    }
    
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