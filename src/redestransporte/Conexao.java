package redestransporte;

public class Conexao {
    
    private int idOrigem;
    private int idDestino;
    private int peso;

    public Conexao(int idOrigem, int idDestino, int peso) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.peso = peso;
    }

    public int getIdOrigem() {
        return idOrigem;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public int getPeso() {
        return peso;
    }
    
    @Override
    public String toString() {
        return "De: " + idOrigem + " -> Para: " + idDestino + " (Peso: " + peso + ")";
    }
}