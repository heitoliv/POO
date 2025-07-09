package qacademico;

public abstract class Avaliacao {
    protected String nome;
    protected Data dtAplic;
    protected double valor;

    public Avaliacao(String n, Data d, double v) {
        this.nome = n;
        this.dtAplic = d;
        this.valor = v;
    }

    public abstract double nota(String cpf);

    public String getNome() {
        return nome;
    }

    public Data getDtAplic() {
        return dtAplic;
    }

    public double getValor() {
        return valor;
    }
}