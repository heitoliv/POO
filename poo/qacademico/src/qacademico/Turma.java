package qacademico;

import java.util.ArrayList;
import java.util.Collections;

public class Turma {
    private String nome;
    private int ano;
    private int sem;
    private Professor prof;
    private ArrayList<Aluno> alunos;
    private ArrayList<Avaliacao> avs;

    public Turma(String nome, int ano, int sem, Professor prof, ArrayList<Aluno> alunos, ArrayList<Avaliacao> avs){
        this.nome = nome;
        this.ano = ano;
        this.sem = sem;
        this.prof = prof;
        this.alunos = alunos;
        this.avs = avs;
    }

    public double calcularNotaFinal(Aluno aluno) {
        double soma = 0;
        for (Avaliacao av : avs) {
            soma += av.nota(aluno.getCpf());
        }
        return Math.min(soma, 100); // Limita a 100 pontos
    }
    public void medias() {
        Collections.sort(alunos, new AlunoNotaComparator(this));

        System.out.println("Médias da turma " + nome + " (" + ano + "/" + sem + "):");
        double somaNotasTurma = 0;
        int numAlunos = 0;

        for (Aluno a : alunos) {
            double notaFinal = calcularNotaFinal(a);
            String notasStr = "";

            for (Avaliacao av : avs) {
                double nota = av.nota(a.getCpf());
                notasStr += String.format("%.1f ", nota);
            }

            System.out.printf("%s: %s= %.1f%n", a.toString(), notasStr, notaFinal);
            somaNotasTurma += notaFinal;
            numAlunos++;
        }

        if (numAlunos > 0) {
            double mediaTurma = somaNotasTurma / numAlunos;
            System.out.printf("Média da turma: %.2f%n%n", mediaTurma);
        } else {
            System.out.println("Nenhum aluno na turma para calcular média.\n");
        }
    }
    public String getNome() {
        return nome;
    }

    public int getAno() {
        return ano;
    }

    public int getSem() {
        return sem;
    }

    public Professor getProf() {
        return prof;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public ArrayList<Avaliacao> getAvs() {
        return avs;
    }

}
