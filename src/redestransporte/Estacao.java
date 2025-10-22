package redestransporte;

public class Estacao implements Comparable<Estacao> {
    
    private int id;
    private String nome;

    public Estacao(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome;
    }

    // Usado pela BST para comparar estações pelo nome
    @Override
    public int compareTo(Estacao outra) {
        return this.nome.compareTo(outra.getNome());
    }
}