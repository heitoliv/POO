package qacademico;

import java.util.ArrayList;

public class AlunoProva {
    private Aluno aluno;
    private ArrayList<Double> notas;

    public AlunoProva(Aluno a, double[] notasArray) {
        this.aluno = a;
        this.notas = new ArrayList<>();

        for (double nota : notasArray) {
            this.notas.add(nota);
        }
    }

    public double notaTotal() {
        double total = 0;
        for (Double nota : notas) {
            total += nota;
        }
        return total;
    }
    public Aluno getAluno() {
        return aluno;
    }

    public ArrayList<Double> getNotas() {
        return notas;
    }

}
