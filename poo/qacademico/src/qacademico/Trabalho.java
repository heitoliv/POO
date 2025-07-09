package qacademico;

import java.util.ArrayList;

public class Trabalho extends Avaliacao{
    private int nIntegrantes;
    private ArrayList<GrupoTrabalho> grupos;

    public Trabalho(String nome, Data data, double valor, int ni, ArrayList<GrupoTrabalho> grupos) {
        super(nome, data, valor);
        this.nIntegrantes = ni;
        this.grupos = grupos;
    }

    public double nota(String cpf) {
        for (GrupoTrabalho g : grupos) {
            if (g.alunoNoGrupo(cpf)) {
                return g.getNota();
            }
        }
        return 0;
    }

    public int getnIntegrantes() {return nIntegrantes;}

    public ArrayList<GrupoTrabalho> getGrupos() {return grupos;}
}
