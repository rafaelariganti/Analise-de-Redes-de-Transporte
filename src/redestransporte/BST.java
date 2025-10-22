package redestransporte;

public class BST {

    // Nó interno da árvore
    private class No {
        Estacao estacao;
        No esquerda;
        No direita;

        public No(Estacao estacao) {
            this.estacao = estacao;
            this.esquerda = null;
            this.direita = null;
        }
    }

    private No raiz; // A raiz da árvore

    public BST() {
        this.raiz = null;
    }

    // --- MÉTODOS CHAMADOS PELO RedesTransporte.java ---

    /**
     * Insere uma nova estação na árvore (método público).
     */
    public void inserir(Estacao estacao) {
        this.raiz = inserirRecursivo(this.raiz, estacao);
    }

    /**
     * Busca uma estação pelo nome (método público).
     */
    public Estacao buscar(String nome) {
        return buscarRecursivo(this.raiz, nome);
    }

    /**
     * Retorna uma string com todas as estações em ordem alfabética (método público).
     */
    public String emOrdem() {
        StringBuilder sb = new StringBuilder("Estações em Ordem Alfabética (BST):\n\n");
        emOrdemRecursivo(this.raiz, sb);
        if(sb.length() == 0) {
            return "Árvore está vazia.";
        }
        return sb.toString();
    }

    // --- MÉTODOS PRIVADOS RECURSIVOS ---

    /**
     * Método auxiliar recursivo para inserir.
     */
    private No inserirRecursivo(No noAtual, Estacao estacao) {
        // Se o nó atual é nulo, encontramos o local para inserir
        if (noAtual == null) {
            return new No(estacao);
        }

        // Compara os nomes para decidir se vai para a esquerda ou direita
        // Usamos o compareTo que definimos na classe Estacao
        int comparacao = estacao.compareTo(noAtual.estacao);

        if (comparacao < 0) {
            // Se for menor, vai para a esquerda
            noAtual.esquerda = inserirRecursivo(noAtual.esquerda, estacao);
        } else if (comparacao > 0) {
            // Se for maior, vai para a direita
            noAtual.direita = inserirRecursivo(noAtual.direita, estacao);
        }
        // (Se for igual, não faz nada, ignora duplicatas)

        return noAtual; // Retorna o nó (com a sub-árvore atualizada)
    }

    /**
     * Método auxiliar recursivo para buscar.
     */
    private Estacao buscarRecursivo(No noAtual, String nome) {
        // Se o nó atual é nulo, não encontrou
        if (noAtual == null) {
            return null;
        }

        // Compara o nome buscado com o nome no nó atual
        int comparacao = nome.compareTo(noAtual.estacao.getNome());

        if (comparacao == 0) {
            // Encontrou!
            return noAtual.estacao;
        } else if (comparacao < 0) {
            // Se for menor, busca na esquerda
            return buscarRecursivo(noAtual.esquerda, nome);
        } else {
            // Se for maior, busca na direita
            return buscarRecursivo(noAtual.direita, nome);
        }
    }

    /**
     * Método auxiliar recursivo para percorrer em-ordem (Esquerda, Raiz, Direita).
     */
    private void emOrdemRecursivo(No noAtual, StringBuilder sb) {
        if (noAtual != null) {
            // 1. Visita a sub-árvore esquerda
            emOrdemRecursivo(noAtual.esquerda, sb);
            
            // 2. Visita o nó atual (Raiz)
            sb.append(noAtual.estacao.toString()).append("\n");
            
            // 3. Visita a sub-árvore direita
            emOrdemRecursivo(noAtual.direita, sb);
        }
    }
}