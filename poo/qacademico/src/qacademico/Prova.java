package qacademico;

import java.util.ArrayList;

public class Prova extends Avaliacao{
    private int nQuestoes;
    private ArrayList<AlunoProva> notas;

    public Prova(String nome, Data data, double valor, int nq, ArrayList<AlunoProva> notas) {
        super(nome, data, valor);
        this.nQuestoes = nq;
        this.notas = notas;
    }

    public double nota(String cpf) {
        for (AlunoProva ap : notas) {
            if (ap.getAluno().getCpf().equals(cpf)) {
                return ap.notaTotal();
            }
        }
        return 0;
    }

    public ArrayList<AlunoProva> getNotas() {
        return notas;
    }

    public int getnQuestoes() {
        return nQuestoes;
    }

}
