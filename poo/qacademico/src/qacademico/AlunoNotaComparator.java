package qacademico;

import java.util.Comparator;

public class AlunoNotaComparator implements Comparator<Aluno> {
    private Turma turma;

    public AlunoNotaComparator(Turma turma) {
        this.turma = turma;
    }

    public int compare(Aluno a1, Aluno a2) {
        double nota1 = turma.calcularNotaFinal(a1);
        double nota2 = turma.calcularNotaFinal(a2);

        if (nota1 != nota2) {
            return Double.compare(nota2, nota1);
        }

        int cmpNome = a1.getNome().compareTo(a2.getNome());
        if (cmpNome != 0) {
            return cmpNome;
        }

        return a1.getMat().compareTo(a2.getMat());
    }
}