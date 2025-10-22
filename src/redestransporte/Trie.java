package redestransporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {

    // Nó interno da Trie
    private class NoTrie {
        // Cada caractere mapeia para um novo nó
        Map<Character, NoTrie> filhos;
        boolean fimDePalavra;
        Estacao estacao; // Guarda a estação completa no final da palavra

        public NoTrie() {
            filhos = new HashMap<>();
            fimDePalavra = false;
            estacao = null;
        }
    }

    private NoTrie raiz;

    public Trie() {
        raiz = new NoTrie();
    }

    // --- MÉTODOS CHAMADOS PELO RedesTransporte.java ---

    /**
     * Insere uma estação na Trie.
     * Os nomes são normalizados (minúsculos e sem acentos) para a busca.
     */
    public void inserir(Estacao e) {
        String nome = normalizarString(e.getNome());
        NoTrie noAtual = raiz;

        for (char ch : nome.toCharArray()) {
            // Se o caractere não existe nos filhos, cria um novo nó
            noAtual.filhos.putIfAbsent(ch, new NoTrie());
            // Move para o próximo nó
            noAtual = noAtual.filhos.get(ch);
        }
        
        // Marca o fim da palavra e guarda a estação
        noAtual.fimDePalavra = true;
        noAtual.estacao = e;
    }

    /**
     * Busca todas as estações que começam com um prefixo e formata a saída.
     */
    public String buscarPorPrefixoFormatado(String prefixo) {
        String prefixoNormalizado = normalizarString(prefixo);
        
        List<Estacao> resultados = buscarPorPrefixo(prefixoNormalizado);
        
        if (resultados.isEmpty()) {
            return "Nenhuma estação encontrada com o prefixo: " + prefixo;
        }

        StringBuilder sb = new StringBuilder("Estações encontradas (Trie):\n\n");
        for (Estacao e : resultados) {
            sb.append(e.toString()).append("\n");
        }
        return sb.toString();
    }

    // --- MÉTODOS PRIVADOS ---

    /**
     * Remove acentos e converte para minúsculo.
     */
    private String normalizarString(String str) {
        // Esta é uma forma simples de "normalizar" para busca
        String temp = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        return temp.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
    
    /**
     * Retorna a lista de Estações que correspondem ao prefixo.
     */
    private List<Estacao> buscarPorPrefixo(String prefixo) {
        List<Estacao> resultados = new ArrayList<>();
        NoTrie noAtual = raiz;

        // 1. Navega até o final do prefixo
        for (char ch : prefixo.toCharArray()) {
            NoTrie proximoNo = noAtual.filhos.get(ch);
            if (proximoNo == null) {
                // Prefixo não existe na árvore
                return resultados; // Retorna lista vazia
            }
            noAtual = proximoNo;
        }

        // 2. A partir do nó final do prefixo, coleta todas as "palavras" (estações)
        // O 'noAtual' é o nó que representa a última letra do prefixo
        coletarTodasEstacoes(noAtual, resultados);
        
        return resultados;
    }

    /**
     * Método recursivo que "desce" a árvore a partir de um nó,
     * coletando todas as estações abaixo dele.
     */
    private void coletarTodasEstacoes(NoTrie no, List<Estacao> resultados) {
        // Se este nó é o fim de uma palavra, adiciona a estação
        if (no.fimDePalavra && no.estacao != null) {
            resultados.add(no.estacao);
        }

        // Continua a busca recursivamente para todos os filhos deste nó
        for (NoTrie filho : no.filhos.values()) {
            coletarTodasEstacoes(filho, resultados);
        }
    }
}