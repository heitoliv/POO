package qacademico;

import java.util.Comparator;

public class TurmaComparator implements Comparator<Turma> {
    public int compare(Turma t1, Turma t2){
        if (t1.getAno() != t2.getAno()) {
            return Integer.compare(t2.getAno(), t1.getAno());
        }
        if (t1.getSem() != t2.getSem()) {
            return Integer.compare(t2.getSem(), t1.getSem());
        }
        int cmpNome = t1.getNome().compareTo(t2.getNome());
        if (cmpNome != 0) {
            return cmpNome;
        }
        return t1.getProf().getNome().compareTo(t2.getProf().getNome());
    }
}