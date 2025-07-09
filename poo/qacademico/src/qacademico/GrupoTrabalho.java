package qacademico;

import java.util.ArrayList;

public class GrupoTrabalho {
    ArrayList<Aluno> alunos;
    private double nota;

    public GrupoTrabalho(ArrayList<Aluno> alunos, double nota) {
        this.alunos = alunos;
        this.nota = nota;
    }

    public double getNota() {
        return nota;
    }
    public boolean alunoNoGrupo(String cpf) {
        for (Aluno a : alunos) {
            if (a.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Aluno> getAlunos() {return alunos;}
}
