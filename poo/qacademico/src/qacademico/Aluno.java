package qacademico;

public class Aluno extends Pessoa{
    private String mat;

    public Aluno(String nome, String cpf, String mat) {
        super(nome, cpf);
        this.mat = mat;
        }
    public String getTipo() {
        return "Aluno";
    }
    public String toString() {
        return nome + " (Matrícula: " + mat + ")";
    }
    public String getMat() {
        return mat;
    }
}
