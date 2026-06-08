import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class MainStreamAPI {
    public static void main(String[] args) {
        System.out.println("\n=== Exercicios Stream ===");

        // Exercicio 1
        Scanner scanner = new Scanner(System.in);
        List<Integer> numeros = new ArrayList<>();
        System.out.println("\n=== Atv 1 ===");
        System.out.println("Digte os numeros a serem analisados ou 'Sair' para encerrar");
        while (scanner.hasNextInt()) {
            numeros.add(scanner.nextInt());
        }

        List<Integer> pares = numeros.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
                // Lista Mutavel
                //.toList()
                // Lista Imutavel

        System.out.println("Todos os Numeros: " + numeros);
        System.out.println("Todos os Pares: " + pares);

        // Exercicio 2
        List<String> nomes = List.of("roberto", "josé", "caio", "vinicius");
        List<String> nomesMaiusculos = nomes.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println("\n=== Atv 2 ===");
        System.out.println("=== Nomes em maiúsculas ===");
        System.out.println("Nomes originais: " + nomes);
        System.out.println("Nomes em maiusculo: " + nomesMaiusculos);
        System.out.println();

        // Exercicio 3
        List<String> palavras = List.of("se", "talvez", "hoje", "sábado", "se", "quarta", "sábado");
        Map<String, Long> frequencia = new HashMap<>();
        palavras.forEach(p->frequencia.merge(p,1L,Long::sum));

        System.out.println("\n=== Atv 3 ===");
        System.out.println("=== Frequência das palavras em uma lista ===");
        System.out.println("Lista = " + palavras);
        frequencia.forEach((palavra, quantidade) ->
                System.out.printf("%s → %d vez\n", palavra, quantidade));
        System.out.println();

        class Produto {
            private String nome;
            private double preco;
            public Produto(String nome, double preco) {
                this.nome = nome;
                this.preco = preco;
            }

            public String getNome()  {
                return nome;
            }
            public double getPreco() {
                return preco;
            }

            @Override
            public String toString() {
                return String.format("nome= %s, preco= R$%.2f", nome, preco);
            }
        }

        // Exercicio 4
        List<Produto> produtos = List.of(new Produto("Teclado Mecânico",350.00),
            new Produto("Mouse Pad",50.00),
            new Produto("Monitor 24",1000.00),
            new Produto("Cabo USB",30.00)
        );
        List<Produto> produtosCaros = produtos.stream()
                .filter(produto -> produto.getPreco() > 100.00)
                .collect(Collectors.toList());
        System.out.println("\n=== Atv 4 ===");
        System.out.println("=== Produtos acima de 100 reais ===");
        System.out.println("Produtos = "+ produtosCaros);
        produtosCaros.forEach(produto -> System.out.println("  " + produto));
        System.out.println();

        // Exercicio 5 (continuação do 4)
        double totalProdutos = produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();
        System.out.println("\n=== Atv 5 ===");
        System.out.println("=== Soma total dos produtos ===");
        System.out.println("Produtos = " + produtos);
        System.out.printf("Total dos produtos: R$%.2f%n", totalProdutos);
        System.out.println();

        // Exercicio 6
        List<String> linguagens = List.of("Java", "Python", "C", "JavaScript", "Ruby");
        List<String> linguagensemordem = linguagens.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());

        System.out.println("\n=== Atv 6 ===");
        System.out.println("=== Linguagens ordenadas por tamanho ===");
        System.out.println("Original: " + linguagens);
        System.out.println("Ordenadas: " + linguagensemordem);
    }
}