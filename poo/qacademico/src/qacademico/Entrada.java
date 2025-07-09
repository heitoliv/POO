package qacademico;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe com as rotinas de entrada e saída do projeto
 * @author Hilario Seibel Junior, Pedro Renã da Silva Moreira e Heitor Mendes de Oliveira
 */
public class Entrada {
    private Scanner input;

    public Entrada() {
        try {
            this.input = new Scanner(new FileInputStream("dados.txt"));
        } catch (FileNotFoundException e) {
            this.input = new Scanner(System.in);
        }
    }

    public int menu() {
        String msg = "*********************\n" +
                "Escolha uma opção:\n" +
                "1) Cadastrar professor:\n" +
                "2) Cadastrar aluno:\n" +
                "3) Cadastrar turma:\n" +
                "4) Listar turmas:\n" +
                "0) Sair\n";
        int op = this.lerInteiro(msg);
        while (op < 0 || op > 4) {
            System.out.println("Opção inválida. Tente novamente: ");
            op = this.lerInteiro(msg);
        }
        return op;
    }

    public void cadProf(Sistema s) {
        try {
            s.listarProfs();
            String nome = lerLinha("Digite o nome do professor: ");
            String cpf = lerLinha("Digite o CPF do professor: ");

            if (s.encontrarProf(cpf) != null || s.encontrarAlunocpf(cpf) != null) {
                throw new IllegalArgumentException("CPF já cadastrado no sistema!");
            }

            double salario = lerDouble("Digite o salário do professor: R$");
            if (salario <= 0) {
                throw new IllegalArgumentException("Salário deve ser positivo!");
            }
            Professor p = new Professor(nome, cpf, salario);
            s.novoProf(p);
            System.out.println("Professor " + p.getNome() + " adicionado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro de professor cancelado. Motivo: " + e.getMessage());
        }
    }

    public void cadAluno(Sistema s) {
        try {
            s.listarAlunos();
            String nome = lerLinha("Digite o nome do aluno: ");
            String cpf = lerLinha("Digite o CPF do aluno: ");
            if (s.encontrarAlunocpf(cpf) != null || s.encontrarProf(cpf) != null) {
                throw new IllegalArgumentException("CPF já cadastrado no sistema!");
            }
            String matricula = lerLinha("Digite a matrícula do aluno: ");
            if (s.encontrarAluno(matricula) != null) {
                throw new IllegalArgumentException("Matrícula já cadastrada!");
            }
            Aluno a = new Aluno(nome, cpf, matricula);
            s.novoAluno(a);
            System.out.println("Aluno " + a.getNome() + " cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro de aluno cancelado. Motivo: " + e.getMessage());
        }
    }

    public void cadTurma(Sistema s) {
        try {
            s.listarTurmas();
            String disciplina = lerLinha("Digite o nome da disciplina: ");
            int ano = lerInteiro("Digite o ano da disciplina: ");
            if (ano < 1970) {
                throw new IllegalArgumentException("Ano inválido.");
            }
            int semestre = lerInteiro("Digite o semestre da disciplina (1 ou 2): ");
            if (semestre != 1 && semestre != 2) {
                throw new IllegalArgumentException("Semestre deve ser 1 ou 2!");
            }

            Professor p = lerProf(s);
            ArrayList<Aluno> alunosDisciplina = lerAlunos(s);

            int nAvaliacoes = lerInteiro("Digite a quantidade de avaliações da disciplina: ");
            if (nAvaliacoes <= 0) {
                throw new IllegalArgumentException("Deve haver pelo menos uma avaliação!");
            }

            ArrayList<Avaliacao> av = new ArrayList<>();
            for (int q = 0; q < nAvaliacoes; q++) {
                System.out.println("\nCadastro de avaliação " + (q + 1) + ":");
                Avaliacao novaAvaliacao = null;
                while (novaAvaliacao == null) {
                    int tipoAval = lerInteiro("Escolha o tipo de avaliação:\n1) Prova\n2) Trabalho\nOpção: ");
                    if (tipoAval == 1) {
                        novaAvaliacao = lerProva(s, alunosDisciplina, ano, semestre);
                    } else if (tipoAval == 2) {
                        novaAvaliacao = lerTrabalho(s, alunosDisciplina, ano, semestre);
                    } else {
                        System.out.println("Opção inválida! Tente novamente.");
                    }
                }
                av.add(novaAvaliacao);
            }

            Turma t = new Turma(disciplina, ano, semestre, p, alunosDisciplina, av);
            s.novaTurma(t);
            System.out.println("Turma '" + disciplina + "' cadastrada com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println("\n!!! ERRO. Motivo: " + e.getMessage());
        }
    }

    private Professor lerProf(Sistema s) throws IllegalArgumentException {
        s.listarProfs();
        String cpf = lerLinha("Digite o CPF do professor: ");
        Professor p = s.encontrarProf(cpf);

        if (p == null) {
            throw new IllegalArgumentException("Professor com CPF " + cpf + " não encontrado!");
        }
        return p;
    }

    private ArrayList<Aluno> lerAlunos(Sistema s) throws IllegalArgumentException {
        int quantAluno = lerInteiro("Digite a quantidade de alunos na disciplina: ");
        if (quantAluno <= 0) {
            throw new IllegalArgumentException("Quantidade de alunos deve ser positiva!");
        }
        int totalAlunosNoSistema = s.getAlunos().size();
        if (quantAluno > totalAlunosNoSistema) {
            throw new IllegalArgumentException(
                    "A quantidade de alunos solicitada (" + quantAluno + ") é maior que o total de alunos cadastrados (" + totalAlunosNoSistema + ").");
        }
        ArrayList<Aluno> alunosTurma = new ArrayList<>();
        s.listarAlunos();
        for (int i = 0; i < quantAluno; i++) {
            String mat = lerLinha("Digite a matrícula do aluno " + (i + 1) + ": ");
            Aluno aluno = s.encontrarAluno(mat);
            if (aluno == null) {
                throw new IllegalArgumentException("Aluno com matrícula " + mat + " não encontrado!");
            }
            if (alunosTurma.contains(aluno)) {
                throw new IllegalArgumentException("O aluno " + aluno.getNome() + " já foi adicionado a esta turma.");
            }
            alunosTurma.add(aluno);
        }
        return alunosTurma;
    }

    private Trabalho lerTrabalho(Sistema s, ArrayList<Aluno> alunosTurma, int anoTurma, int semestreTurma) throws IllegalArgumentException {
        System.out.println("\nCadastro de Trabalho:");
        String nome = lerLinha("Informe o nome deste trabalho: ");
        int dia = lerInteiro("Digite o dia do trabalho (1 a 31): ");
        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("Dia deve estar entre 1 e 31!");
        }

        int mes = lerInteiro("Digite o mês do trabalho (1 a 12): ");
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês deve estar entre 1 e 12!");
        }

        if (semestreTurma == 1 && mes > 6) {
            throw new IllegalArgumentException("Para o primeiro semestre, o mês deve ser entre 1 e 6");
        } else if (semestreTurma == 2 && mes < 7) {
            throw new IllegalArgumentException("Para o segundo semestre, o mês deve ser entre 7 e 12");
        }

        int ano = lerInteiro("Digite o ano do trabalho: ");
        if (ano != anoTurma) {
            throw new IllegalArgumentException("Ano do trabalho deve ser igual ao ano da turma (" + anoTurma + ")!");
        }

        if (!Data.valida(dia, mes, ano)) {
            throw new IllegalArgumentException("Data inválida!");
        }
        double valorMaximo = lerDouble("Digite o valor máximo deste trabalho: ");
        if (valorMaximo <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo!");
        }
        int totalAlunos = alunosTurma.size();
        int nIntegrantes = lerInteiro("Digite o número máximo de integrantes: ");
        if (nIntegrantes <= 0 || nIntegrantes > totalAlunos) {
            throw new IllegalArgumentException("Número de integrantes deve estar entre 1 e " + totalAlunos);
        }

        int nGrupos = lerInteiro("Digite o número de grupos: ");
        if (nGrupos <= 0) {
            throw new IllegalArgumentException("Número de grupos deve ser positivo!");
        }
        if (nGrupos > totalAlunos) {
            throw new IllegalArgumentException(
                    "Configuração impossível: O número de grupos (" + nGrupos + ") não pode ser maior que o número de alunos na turma (" + totalAlunos + ")."
            );
        }

        int capacidadeMaxima = nGrupos * nIntegrantes;
        if (totalAlunos > capacidadeMaxima) {
            throw new IllegalArgumentException(
                    "Configuração impossível! A turma tem " + totalAlunos + " alunos, mas com " + nGrupos + " grupos de no máximo " + nIntegrantes + " integrantes, só é possível alocar " + capacidadeMaxima + " alunos."
            );
        }
        ArrayList<Aluno> alunosJaAlocados = new ArrayList<>();
        ArrayList<GrupoTrabalho> grupos = new ArrayList<>();
        for (int i = 0; i < nGrupos; i++) {
            System.out.println("\nGrupo " + (i + 1) + ":");
            GrupoTrabalho grupo = lerGrupoTrabalho(s, alunosTurma, nIntegrantes, alunosJaAlocados);
            grupos.add(grupo);
            alunosJaAlocados.addAll(grupo.getAlunos());
        }
        return new Trabalho(nome, new Data(dia, mes, ano), valorMaximo, nIntegrantes, grupos);
    }

    private GrupoTrabalho lerGrupoTrabalho(Sistema s, ArrayList<Aluno> alunosTurma, int maxIntegrantes, ArrayList<Aluno> alunosJaAlocados) throws IllegalArgumentException {
        int alunosDisponiveis = alunosTurma.size() - alunosJaAlocados.size();
        int numAlunos = lerInteiro("Digite o número de alunos neste grupo: ");
        if (numAlunos <= 0 || numAlunos > maxIntegrantes) {
            throw new IllegalArgumentException("Número de alunos deve estar entre 1 e " + maxIntegrantes);
        }
        if (numAlunos > alunosDisponiveis) {
            throw new IllegalArgumentException("Operação inválida. Apenas " + alunosDisponiveis + " alunos estão disponíveis.");
        }
        ArrayList<Aluno> alunosGrupo = new ArrayList<>();
        for (int i = 0; i < numAlunos; i++) {
            String mat = lerLinha("Digite a matrícula do aluno " + (i + 1) + ": ");
            Aluno aluno = s.encontrarAluno(mat);
            if (aluno == null) throw new IllegalArgumentException("Aluno com matrícula " + mat + " não encontrado!");
            if (alunosJaAlocados.contains(aluno)) throw new IllegalArgumentException("O aluno " + aluno.getNome() + " já foi alocado em outro grupo.");
            if (alunosGrupo.contains(aluno)) throw new IllegalArgumentException("O aluno " + aluno.getNome() + " já foi adicionado a ESTE grupo.");
            if (!alunosTurma.contains(aluno)) throw new IllegalArgumentException("Aluno não está matriculado nesta turma!");
            alunosGrupo.add(aluno);
        }
        double nota = lerDouble("Nota do grupo: ");
        if (nota < 0) {
            throw new IllegalArgumentException("Nota não pode ser negativa!");
        }
        return new GrupoTrabalho(alunosGrupo, nota);
    }

    private Prova lerProva(Sistema s, ArrayList<Aluno> alunosTurma, int anoTurma, int semestreTurma) throws IllegalArgumentException {
        System.out.println("\nCadastro de Prova:");
        String nome = lerLinha("Informe o nome desta prova: ");
        int dia = lerInteiro("Digite o dia da prova: ");
        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("Dia deve estar entre 1 e 31!");
        }

        int mes = lerInteiro("Digite o mês da prova: ");
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês deve estar entre 1 e 12!");
        }

        if (semestreTurma == 1 && mes > 6) {
            throw new IllegalArgumentException("Para o primeiro semestre, o mês deve ser entre 1 e 6");
        } else if (semestreTurma == 2 && mes < 7) {
            throw new IllegalArgumentException("Para o segundo semestre, o mês deve ser entre 7 e 12");
        }

        int ano = lerInteiro("Digite o ano da prova: ");
        if (ano != anoTurma) {
            throw new IllegalArgumentException("Ano da prova deve ser igual ao ano da turma (" + anoTurma + ")!");
        }

        if (!Data.valida(dia, mes, ano)) {
            throw new IllegalArgumentException("Data inválida!");
        }

        double valorMaximo = lerDouble("Digite o valor máximo desta avaliação: ");
        if (valorMaximo <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo!");
        }

        int nQuestoes = lerInteiro("Digite o número de questões: ");
        if (nQuestoes <= 0) {
            throw new IllegalArgumentException("Número de questões deve ser positivo!");
        }

        ArrayList<AlunoProva> alunosProvas = new ArrayList<>();
        for (Aluno aluno : alunosTurma) {
            AlunoProva alunoProva = lerAlunoProva(aluno, nQuestoes);
            alunosProvas.add(alunoProva);
        }
        return new Prova(nome, new Data(dia, mes, ano), valorMaximo, nQuestoes, alunosProvas);
    }

    private AlunoProva lerAlunoProva(Aluno aluno, int nQuestoes) throws IllegalArgumentException {

        double[] notas = new double[nQuestoes];
        for (int i = 0; i < nQuestoes; i++) {
            notas[i] = lerDouble("Nota de " + aluno.getNome() + " na questão " + (i + 1) + ": ");
            if (notas[i] < 0) {
                throw new IllegalArgumentException("Nota não pode ser negativa!");
            }
        }
        return new AlunoProva(aluno, notas);
    }


    private String lerLinha(String msg) {
        System.out.print(msg);
        String linha = this.input.nextLine();
        while (linha.isEmpty() || linha.charAt(0) == '#') {
            if (linha.isEmpty()) System.out.print("Entrada não pode ser vazia. " + msg);
            linha = this.input.nextLine();
        }
        return linha;
    }

    private int lerInteiro(String msg) {
        String linha = lerLinha(msg);
        try {
            return Integer.parseInt(linha.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Entrada inválida. '" + linha + "' não é um número inteiro válido.");
        }
    }

    private double lerDouble(String msg) {
        String linha = lerLinha(msg);
        try {
            return Double.parseDouble(linha.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Entrada inválida. '" + linha + "' não é um número válido.");
        }
    }
}