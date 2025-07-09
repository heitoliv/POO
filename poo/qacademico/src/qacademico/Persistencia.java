package qacademico;

import java.io.*;
import java.util.ArrayList;

public class Persistencia {
    public static void salvar(Sistema sistema) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter("dados.txt"))) {
            salvarProfessores(w, sistema.getProfs());
            salvarAlunos(w, sistema.getAlunos());
            salvarTurmas(w, sistema.getTurmas());
            w.write("FIM");
            w.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private static void salvarProfessores(BufferedWriter w, ArrayList<Professor> professores) throws IOException {
        for (Professor p : professores) {
            w.write("PROF");
            w.newLine();
            w.write(p.getNome());
            w.newLine();
            w.write(p.getCpf());
            w.newLine();
            w.write(String.valueOf(p.getSalario()));
            w.newLine();
        }
    }

    private static void salvarAlunos(BufferedWriter w, ArrayList<Aluno> alunos) throws IOException {
        for (Aluno a : alunos) {
            w.write("ALU");
            w.newLine();
            w.write(a.getNome());
            w.newLine();
            w.write(a.getCpf());
            w.newLine();
            w.write(a.getMat());
            w.newLine();
        }
    }

    private static void salvarTurmas(BufferedWriter w, ArrayList<Turma> turmas) throws IOException {
        for (Turma t : turmas) {
            w.write("TUR");
            w.newLine();
            salvarDadosBasicosTurma(w, t);
            salvarAlunosTurma(w, t.getAlunos());
            salvarAvaliacoesTurma(w, t.getAvs());
        }
    }

    private static void salvarDadosBasicosTurma(BufferedWriter w, Turma t) throws IOException {
        w.write(t.getNome());
        w.newLine();
        w.write(String.valueOf(t.getAno()));
        w.newLine();
        w.write(String.valueOf(t.getSem()));
        w.newLine();
        w.write(t.getProf().getCpf());
        w.newLine();
    }

    private static void salvarAlunosTurma(BufferedWriter w, ArrayList<Aluno> alunos) throws IOException {
        w.write(String.valueOf(alunos.size()));
        w.newLine();
        for (Aluno a : alunos) {
            w.write(a.getMat());
            w.newLine();
        }
    }

    private static void salvarAvaliacoesTurma(BufferedWriter w, ArrayList<Avaliacao> avaliacoes) throws IOException {
        w.write(String.valueOf(avaliacoes.size()));
        w.newLine();
        for (Avaliacao av : avaliacoes) {
            if (av instanceof Prova) {
                salvarProva(w, (Prova) av);
            } else if (av instanceof Trabalho) {
                salvarTrabalho(w, (Trabalho) av);
            }
        }
    }

    private static void salvarProva(BufferedWriter w, Prova p) throws IOException {
        w.write("PROV");
        w.newLine();
        w.write(p.getNome());
        w.newLine();
        w.write(String.valueOf(p.getDtAplic().getDia()));
        w.newLine();
        w.write(String.valueOf(p.getDtAplic().getMes()));
        w.newLine();
        w.write(String.valueOf(p.getDtAplic().getAno()));
        w.newLine();
        w.write(String.valueOf(p.getValor()));
        w.newLine();
        w.write(String.valueOf(p.getnQuestoes()));
        w.newLine();

        for (AlunoProva ap : p.getNotas()) {
            for (Double nota : ap.getNotas()) {
                w.write(String.valueOf(nota));
                w.newLine();
            }
        }
    }

    private static void salvarTrabalho(BufferedWriter w, Trabalho tr) throws IOException {
        w.write("TRAB");
        w.newLine();
        w.write(tr.getNome());
        w.newLine();
        w.write(String.valueOf(tr.getDtAplic().getDia()));
        w.newLine();
        w.write(String.valueOf(tr.getDtAplic().getMes()));
        w.newLine();
        w.write(String.valueOf(tr.getDtAplic().getAno()));
        w.newLine();
        w.write(String.valueOf(tr.getValor()));
        w.newLine();
        w.write(String.valueOf(tr.getnIntegrantes()));
        w.newLine();
        w.write(String.valueOf(tr.getGrupos().size()));
        w.newLine();

        for (GrupoTrabalho g : tr.getGrupos()) {
            w.write(String.valueOf(g.getAlunos().size()));
            w.newLine();
            for (Aluno a : g.getAlunos()) {
                w.write(a.getMat());
                w.newLine();
            }
            w.write(String.valueOf(g.getNota()));
            w.newLine();
        }
    }

    public static Sistema carregar() {
        Sistema sistema = new Sistema();
        try (BufferedReader r = new BufferedReader(new FileReader("dados.txt"))) {
            String linha;
            while ((linha = r.readLine()) != null && !linha.equals("FIM")) {
                switch (linha) {
                    case "PROF":
                        sistema.novoProf(carregarProfessor(r));
                        break;
                    case "ALU":
                        sistema.novoAluno(carregarAluno(r));
                        break;
                    case "TUR":
                        sistema.novaTurma(carregarTurma(r, sistema));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo de dados não encontrado. Criando novo sistema.");
        }
        return sistema;
    }

    private static Professor carregarProfessor(BufferedReader r) throws IOException {
        String nome = r.readLine();
        String cpf = r.readLine();
        double salario = Double.parseDouble(r.readLine());
        return new Professor(nome, cpf, salario);
    }

    private static Aluno carregarAluno(BufferedReader r) throws IOException {
        String nome = r.readLine();
        String cpf = r.readLine();
        String mat = r.readLine();
        return new Aluno(nome, cpf, mat);
    }

    private static Turma carregarTurma(BufferedReader r, Sistema sistema) throws IOException {
        String nome = r.readLine();
        int ano = Integer.parseInt(r.readLine());
        int sem = Integer.parseInt(r.readLine());

        String cpfProf = r.readLine();
        Professor prof = sistema.encontrarProf(cpfProf);
        if (prof == null) {
            throw new IOException("Professor não encontrado para a turma " + nome);
        }

        int numAlunos = Integer.parseInt(r.readLine());
        ArrayList<Aluno> alunosTurma = new ArrayList<>();
        for (int i = 0; i < numAlunos; i++) {
            String matAluno = r.readLine();
            Aluno aluno = sistema.encontrarAluno(matAluno);
            if (aluno != null) {
                alunosTurma.add(aluno);
            }
        }

        int numAvaliacoes = Integer.parseInt(r.readLine());
        ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
        for (int i = 0; i < numAvaliacoes; i++) {
            String tipoAval = r.readLine();
            if (tipoAval.equals("PROV")) {
                avaliacoes.add(carregarProva(r, sistema, alunosTurma));
            } else if (tipoAval.equals("TRAB")) {
                avaliacoes.add(carregarTrabalho(r, sistema, alunosTurma));
            }
        }

        return new Turma(nome, ano, sem, prof, alunosTurma, avaliacoes);
    }

    private static Prova carregarProva(BufferedReader r, Sistema sistema, ArrayList<Aluno> alunosTurma) throws IOException {
        String nome = r.readLine();
        int dia = Integer.parseInt(r.readLine());
        int mes = Integer.parseInt(r.readLine());
        int ano = Integer.parseInt(r.readLine());
        double valor = Double.parseDouble(r.readLine());
        int numQuestoes = Integer.parseInt(r.readLine());

        ArrayList<AlunoProva> alunosProvas = new ArrayList<>();
        for (Aluno aluno : alunosTurma) {
            double[] notas = new double[numQuestoes];
            for (int i = 0; i < numQuestoes; i++) {
                notas[i] = Double.parseDouble(r.readLine());
            }
            alunosProvas.add(new AlunoProva(aluno, notas));
        }

        return new Prova(nome, new Data(dia, mes, ano), valor, numQuestoes, alunosProvas);
    }

    private static Trabalho carregarTrabalho(BufferedReader r, Sistema sistema, ArrayList<Aluno> alunosTurma) throws IOException {
        String nome = r.readLine();
        int dia = Integer.parseInt(r.readLine());
        int mes = Integer.parseInt(r.readLine());
        int ano = Integer.parseInt(r.readLine());
        double valor = Double.parseDouble(r.readLine());
        int numIntegrantes = Integer.parseInt(r.readLine());
        int numGrupos = Integer.parseInt(r.readLine());

        ArrayList<GrupoTrabalho> grupos = new ArrayList<>();
        for (int i = 0; i < numGrupos; i++) {
            int numAlunosGrupo = Integer.parseInt(r.readLine());
            ArrayList<Aluno> alunosGrupo = new ArrayList<>();
            for (int j = 0; j < numAlunosGrupo; j++) {
                String matAluno = r.readLine();
                Aluno aluno = sistema.encontrarAluno(matAluno);
                if (aluno != null) {
                    alunosGrupo.add(aluno);
                }
            }
            double notaGrupo = Double.parseDouble(r.readLine());
            grupos.add(new GrupoTrabalho(alunosGrupo, notaGrupo));
        }

        return new Trabalho(nome, new Data(dia, mes, ano), valor, numIntegrantes, grupos);
    }
}
