package org.example;

import org.example.config.DatabaseConfig;
import org.example.entities.User;
import org.example.repository.UserRepository;
import java.sql.Connection;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig.initDatabase();

        try (Connection conn = DatabaseConfig.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            UserRepository userRepo = new UserRepository(conn);

            int opcao;
            do {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1 - Cadastrar usuário");
                System.out.println("2 - Listar usuários");
                System.out.println("3 - Buscar usuário por ID");
                System.out.println("4 - Excluir usuário");
                System.out.println("0 - Sair");
                System.out.print("Escolha: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1 -> cadastrarUsuario(userRepo, scanner);
                    case 2 -> listarUsuarios(userRepo);
                    case 3 -> buscarUsuario(userRepo, scanner);
                    case 4 -> excluirUsuario(userRepo, scanner);
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } while (opcao != 0);

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void cadastrarUsuario(UserRepository repo, Scanner scanner) {
        System.out.println("\n--- NOVO USUÁRIO ---");

        try {
            System.out.print("ID (número inteiro): ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            // Verificação otimizada
            if (repo.findById(id).isPresent()) {
                System.out.println("❌ Erro: ID já existe!");
                return;
            }

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            repo.save(new User(id, nome, email, senha));
            System.out.println("✅ Usuário cadastrado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("❌ Erro: ID deve ser um número inteiro!");
            scanner.nextLine(); // Limpar buffer inválido
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
        }
    }

    private static void listarUsuarios(UserRepository repo) {
        List<User> usuarios = repo.findAll();

        if (usuarios.isEmpty()) {
            System.out.println("\n⚠️ Nenhum usuário cadastrado.");
        } else {
            System.out.println("\n--- USUÁRIOS CADASTRADOS ---");
            usuarios.forEach(System.out::println);
        }
    }

    private static void buscarUsuario(UserRepository repo, Scanner scanner) {
        System.out.print("\nDigite o ID do usuário: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        repo.findById(id).ifPresentOrElse(
                usuario -> System.out.println("Usuário encontrado:\n" + usuario),
                () -> System.out.println("Usuário não encontrado!")
        );
    }

    private static void excluirUsuario(UserRepository repo, Scanner scanner) {
        System.out.print("\nDigite o ID do usuário a excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            System.out.println("✅ Usuário excluído!");
        } else {
            System.out.println("❌ Erro: ID não encontrado!");
        }
    }
}