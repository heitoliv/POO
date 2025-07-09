package qacademico;

public abstract class Pessoa {
    protected String nome;
    protected String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public abstract String getTipo();

    public String toString() {
        return nome + " (" + getTipo() + " - CPF: " + cpf + ")";
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }
}