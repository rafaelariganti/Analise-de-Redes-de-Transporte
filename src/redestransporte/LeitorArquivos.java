package redestransporte;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivos {

    /**
     * Lê o arquivo de estações (ex: "estacoes.txt").
     * Formato esperado: ID;Nome da Estacao
     */
    public static List<Estacao> lerEstacoes(String nomeArquivo) throws IOException {
        List<Estacao> estacoes = new ArrayList<>();

        // Usamos try-with-resources para garantir que o arquivo seja fechado
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) { // Ignora linhas em branco
                    continue;
                }

                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    try {
                        int id = Integer.parseInt(partes[0].trim());
                        String nome = partes[1].trim();
                        estacoes.add(new Estacao(id, nome));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter número na linha: " + linha);
                    }
                } else {
                    System.err.println("Linha mal formatada (Estacao): " + linha);
                }
            }
        }
        return estacoes;
    }

    /**
     * Lê o arquivo de conexões (ex: "conexoes.txt").
     * Formato esperado: ID_Origem;ID_Destino;Peso
     */
    public static List<Conexao> lerConexoes(String nomeArquivo) throws IOException {
        List<Conexao> conexoes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(";");
                if (partes.length == 3) {
                    try {
                        int idOrigem = Integer.parseInt(partes[0].trim());
                        int idDestino = Integer.parseInt(partes[1].trim());
                        int peso = Integer.parseInt(partes[2].trim());
                        conexoes.add(new Conexao(idOrigem, idDestino, peso));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter número na linha: " + linha);
                    }
                } else {
                    System.err.println("Linha mal formatada (Conexao): " + linha);
                }
            }
        }
        return conexoes;
    }
}