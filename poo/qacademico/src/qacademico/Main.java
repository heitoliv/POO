package qacademico;

import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Sistema s = Persistencia.carregar();
        Entrada io = new Entrada();
        int op = -1;

        while (op != 0) {
            try {
                op = io.menu();

                if (op == 1) {
                    io.cadProf(s);
                }
                else if (op == 2) {
                    io.cadAluno(s);
                }
                else if (op == 3) {
                    io.cadTurma(s);
                }
                else if (op == 4) {
                    s.listarTurmas();
                }
                else if (op == 0) {
                    Persistencia.salvar(s);
                    System.out.println("Dados salvos. Saindo...");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números válidos.");
                op = -1;
            }
            catch (StringIndexOutOfBoundsException e) {
                System.out.println("Erro: Entrada vazia ou inválida.");
                op = -1;
            }
            catch (InputMismatchException e) {
                System.out.println("Erro: Formato de entrada incorreto.");
                op = -1;
            }
            catch (IllegalArgumentException e) {
                System.out.println("Erro: Opção inválida! Digite um número entre 0 e 4.");
                op = -1;
            }
            catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
                op = -1;
            }
        }
    }
}
