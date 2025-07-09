package qacademico;

public class Professor extends Pessoa {
    private double salario;

    public Professor(String nome, String cpf, double salario) {
        super(nome, cpf);
        this.salario = salario;
    }

    public String getTipo() {
        return "Professor";
    }

    public double getSalario() {
        return salario;
    }
}