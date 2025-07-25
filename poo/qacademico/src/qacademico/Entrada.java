package qacademico;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe com as rotinas de entrada e saída do projeto
 * @author Hilario Seibel Junior, Pedro Renã da Silva Moreira e Heitor Mendes de Oliveira
 */
public class Entrada {
    private Scanner input;

    /**
     * Construtor da classe Entrada:
     * Se houver um arquivo input.txt, define que o Scanner vai ler deste arquivo.
     * Se o arquivo não existir, define que o Scanner vai ler da entrada padrão (teclado)
     */
    public Entrada() {
        try {
            // Se houver um arquivo input.txt, o Scanner vai ler dele.
            this.input = new Scanner(new FileInputStream("input.txt"));
        } catch (FileNotFoundException e) {
            // Caso contrário, vai ler do teclado.
            this.input = new Scanner(System.in);
        }
    }

    /**
     * Método da classe Entrada:
     * Imprime o menu principal, lê a opção escolhida pelo usuário e retorna a opção selecionada.
     */
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

    /**
     * Lê os dados de um novo Professor e cadastra-o no sistema.
     * @param s: Um objeto da classe Sistema
     */
    public void cadProf(Sistema s) {
        try {
            s.listarProfs();

            String nome = lerLinha("Digite o nome do professor: ");
            String cpf = lerLinha("Digite o CPF do professor: ");

            if (s.cpfExistente(cpf)) {
                throw new IllegalArgumentException("Erro: CPF já cadastrado (como aluno ou professor)!");
            }
            if (cpf.length() < 11) {
                throw new CPFException("Erro: O CPF deve no mínimo 11 digítos númericos!");
            } else if (cpf.length() > 14){
                throw new CPFException("Erro: O CPF não deve ser maior que 14 dígitos!");
            }

            double salario = lerDouble("Digite o salário do professor: R$");
            if (salario <= 0) {
                throw new IllegalArgumentException("Salário deve ser positivo!");
            }

            Professor p = new Professor(nome, cpf, salario);
            s.novoProf(p);
            System.out.println("Professor " + p.getNome() + " adicionado com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lê os dados de um novo Aluno e cadastra-o no sistema.
     * @param s: Um objeto da classe Sistema
     */
    public void cadAluno(Sistema s) {
        try {
            s.listarAlunos();

            String nome = lerLinha("Digite o nome do aluno: ");
            String cpf = lerLinha("Digite o CPF do aluno: ");

            if (s.cpfExistente(cpf)) {
                throw new CPFException("Erro: CPF já cadastrado (como aluno ou professor)!");
            }
            if (cpf.length() < 11) {
                throw new CPFException("Erro: O CPF deve no mínimo 11 digítos númericos!");
            } else if (cpf.length() > 14){
                throw new CPFException("Erro: O CPF não deve ser maior que 14 dígitos!");
            }

            String matricula = lerLinha("Digite a matrícula do aluno: ");
            if (s.encontrarAluno(matricula) != null) {
                throw new IllegalArgumentException("Erro: Matrícula já cadastrada!");
            }

            Aluno a = new Aluno(nome, cpf, matricula);
            s.novoAluno(a);
            System.out.println("Aluno " + a.getNome() + " cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lê os dados de um nova Turma e cadastra-o no sistema.
     * @param s: Um objeto da classe Sistema
     */
    public void cadTurma(Sistema s) {
        try {
            s.listarTurmas();

            String disciplina = lerLinha("Digite o nome da disciplina: ");
            int ano = lerInteiro("Digite o ano da disciplina: ");
            int semestre = lerInteiro("Digite o semestre da disciplina (1 ou 2): ");

            if (semestre != 1 && semestre != 2) {
                throw new IllegalArgumentException("Semestre deve ser 1 ou 2!");
            }

            Professor p = lerProf(s);
            if (p == null) {
                throw new IllegalArgumentException("Professor não encontrado!");
            }

            ArrayList<Aluno> alunosDisciplina = lerAlunos(s);
            if (alunosDisciplina == null || alunosDisciplina.isEmpty()) {
                throw new IllegalArgumentException("Turma deve ter pelo menos um aluno!");
            }

            int nAvaliacoes = lerInteiro("Digite a quantidade de avaliações da disciplina: ");
            if (nAvaliacoes <= 0) {
                throw new IllegalArgumentException("Deve haver pelo menos uma avaliação!");
            }

            ArrayList<Avaliacao> av = new ArrayList<>();
            for (int q = 0; q < nAvaliacoes; q++) {
                System.out.println("\nCadastro de avaliação " + (q+1) + ":");
                int tipoAval = lerInteiro("Escolha o tipo de avaliação:\n1) Prova\n2) Trabalho\nOpção: ");

                if (tipoAval == 1) {
                    av.add(lerProva(s, alunosDisciplina));
                } else if (tipoAval == 2) {
                    av.add(lerTrabalho(s, alunosDisciplina));
                } else {
                    System.out.println("Opção inválida! Avaliação não adicionada.");
                    q--;
                }
            }

            Turma t = new Turma(disciplina, ano, semestre, p, alunosDisciplina, av);
            s.novaTurma(t);
            System.out.println("Turma cadastrada com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Faz a leitura do CPF do professor:
     * @param s: variável correspondente ao sistema.
     * @return Um professor selecionado através da busca pelo CPF.
     */
    private Professor lerProf(Sistema s) {
        String cpf = lerLinha("Digite o CPF do professor: ");
        return s.encontrarProf(cpf);
    }

    /**
     * Faz a leitura dos alunos matriculados na turma:
     * @param s: variável correspondente ao sistema.
     * @return Lista de alunos matrculados na turma.
     */
    private ArrayList<Aluno> lerAlunos(Sistema s) {
        try {
            int quantAluno = lerInteiro("Digite a quantidade de alunos na disciplina: ");
            if (quantAluno <= 0) {
                throw new IllegalArgumentException("Quantidade de alunos deve ser positiva!");
            }
            ArrayList<Aluno> alunosTurma = new ArrayList<>();
            for (int i = 0; i < quantAluno; i++) {
                String mat = lerLinha("Digite a matrícula do aluno " + (i+1) + ": ");
                Aluno aluno = s.encontrarAluno(mat);

                if (aluno == null) {
                    throw new IllegalArgumentException("Aluno com matrícula " + mat + " não encontrado!");
                }
                alunosTurma.add(aluno);
            }
            return alunosTurma;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Faz a leitura dos dados correspondentes ao trabalho.
     * @param s: variável correspondente ao sistema.
     * @return A criação do trabalho e as suas demais informações.
     */
    private Trabalho lerTrabalho(Sistema s, ArrayList<Aluno> alunosTurma) {
        try {
            System.out.println("\nCadastro de Trabalho:");
            String nome = lerLinha("Informe o nome deste trabalho: ");

            int dia = lerInteiro("Digite o dia do trabalho: ");
            int mes = lerInteiro("Digite o mês do trabalho: ");
            int ano = lerInteiro("Digite o ano do trabalho: ");

            if (!Data.valida(dia, mes, ano)) {
                throw new IllegalArgumentException("Data inválida!");
            }

            double valorMaximo = lerDouble("Digite o valor máximo deste trabalho: ");
            if (valorMaximo <= 0) {
                throw new IllegalArgumentException("Valor deve ser positivo!");
            }

            int nIntegrantes = lerInteiro("Digite o número máximo de integrantes: ");
            if (nIntegrantes <= 0) {
                throw new IllegalArgumentException("Número de integrantes deve ser positivo!");
            }

            int nGrupos = lerInteiro("Digite o número de grupos: ");
            if (nGrupos <= 0) {
                throw new IllegalArgumentException("Número de grupos deve ser positivo!");
            }

            ArrayList<GrupoTrabalho> grupos = new ArrayList<>();
            for (int i = 0; i < nGrupos; i++) {
                System.out.println("\nGrupo " + (i+1) + ":");
                GrupoTrabalho grupo = lerGrupoTrabalho(s, alunosTurma, nIntegrantes);
                if (grupo == null) {
                    throw new IllegalArgumentException("Grupo inválido!");
                }
                grupos.add(grupo);
            }

            return new Trabalho(nome, new Data(dia, mes, ano), valorMaximo, nIntegrantes, grupos);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar trabalho: " + e.getMessage());
            return null;
        }
    }

    /**
     * Faz a leitura dos alunos a pertencer no Grupo para o Trabalho.
     * @param s: variável correspondente ao sistema.
     * @return Criação do grupo pertence ao respectivo trabalho.
     */

    private GrupoTrabalho lerGrupoTrabalho(Sistema s, ArrayList<Aluno> alunosTurma, int maxIntegrantes) {
        try {
            int numAlunos = lerInteiro("Digite o número de alunos neste grupo: ");
            if (numAlunos <= 0 || numAlunos > maxIntegrantes) {
                throw new IllegalArgumentException("Número de alunos deve estar entre 1 e " + maxIntegrantes);
            }

            ArrayList<Aluno> alunosGrupo = new ArrayList<>();
            for (int i = 0; i < numAlunos; i++) {
                String mat = lerLinha("Digite a matrícula do aluno " + (i+1) + ": ");
                Aluno aluno = s.encontrarAluno(mat);

                if (aluno == null) {
                    throw new IllegalArgumentException("Aluno com matrícula " + mat + " não encontrado!");
                }
                if (!alunosTurma.contains(aluno)) {
                    throw new IllegalArgumentException("Aluno não está matriculado nesta turma!");
                }
                alunosGrupo.add(aluno);
            }

            double nota = lerDouble("Nota do grupo: ");
            if (nota < 0) {
                throw new IllegalArgumentException("Nota não pode ser negativa!");
            }

            return new GrupoTrabalho(alunosGrupo, nota);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar grupo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Faz a leitura dos dados pertencentes a prova:
     * Ignora linhas começando com #, que vão indicar comentários no arquivo de entrada:
     * @param s: variável correspondente ao sistema.
     * @param alunosTurma: arraylist presente no construtor.
     * @return Criação de uma prova e as suas demais informações
     */

    private Prova lerProva(Sistema s, ArrayList<Aluno> alunosTurma) {
        try {
            System.out.println("\nCadastro de Prova:");
            String nome = lerLinha("Informe o nome desta prova: ");

            int dia = lerInteiro("Digite o dia da prova: ");
            int mes = lerInteiro("Digite o mês da prova: ");
            int ano = lerInteiro("Digite o ano da prova: ");

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
                if (alunoProva != null) {
                    alunosProvas.add(alunoProva);
                } else {
                    return null;
                }
            }

            return new Prova(nome, new Data(dia, mes, ano), valorMaximo, nQuestoes, alunosProvas);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar prova: " + e.getMessage());
            return null;
        }
    }
    /**
     * Faz a leitura da nota do aluno na prova:
     * @param aluno: variável correspondente ao aluno.
     * @param nQuestoes: variável correspondente a quantidade de questões da prova.
     * @return Retorna os resultados do respectivo aluno à prova.
     */
    private AlunoProva lerAlunoProva(Aluno aluno, int nQuestoes) {
        try {
            double[] notas = new double[nQuestoes];
            for (int i = 0; i < nQuestoes; i++) {
                notas[i] = lerDouble("Nota de " + aluno.getNome() + " na questão " + (i+1) + ": ");
                if (notas[i] < 0) {
                    throw new IllegalStateException("Erro: Nota não pode ser negativa!");
                }
            }
            return new AlunoProva(aluno, notas);
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um valor numérico válido para a nota!");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } return null;
    }

    /**
     * Faz a leitura de uma linha inteira
     * Ignora linhas começando com #, que vão indicar comentários no arquivo de entrada:
     * @param msg: Mensagem que será exibida ao usuário
     * @return Uma String contendo a linha que foi lida
     */
    private String lerLinha(String msg) {
        // Imprime uma mensagem ao usuário, lê uma e retorna esta linha
        System.out.print(msg);
        String linha = this.input.nextLine();

        // Ignora linhas começando com #, que vão indicar comentários no arquivo de entrada:
        while (linha.charAt(0) == '#') linha = this.input.nextLine();
        return linha;
    }

    /**
     * Faz a leitura de um número inteiro
     * @param msg: Mensagem que será exibida ao usuário
     * @return O número digitado pelo usuário convertido para int
     */
    private int lerInteiro(String msg) {
        // Imprime uma mensagem ao usuário, lê uma linha contendo um inteiro e retorna este inteiro
        try {
            return Integer.parseInt(lerLinha(msg));
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um número inteiro válido!");
        }
        return -1;
    }

    /**
     * Faz a leitura de um double
     * @param msg: Mensagem que será exibida ao usuário
     * @return O número digitado pelo usuário convertido para double
     */
    private double lerDouble(String msg) {
        // Imprime uma mensagem ao usuário, lê uma linha contendo um double e retorna este double
        try {
            return Double.parseDouble(lerLinha(msg));
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um número inteiro válido!");
        }
        return -1;
    }
}

