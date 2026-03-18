import java.util.Scanner;
import java.util.ArrayList;

public class CalculadoraMenu {
    static Scanner scan = new Scanner(System.in);
    static ArrayList<Historico> historicoVendas = new ArrayList<>();
    

    public static void main(String[] args) {
        Menu();
        scan.close();
    }

    public static void ValidarResp(int resp) {
        if (resp == 1) {
            PreçoTotal();
        } else if (resp == 2) {
            CalcularTroco();
        } else if (resp == 3) {
            exibirHistorico();
        }
    }

    public static void Menu() {
        int resp = 1000;
        do {
            System.out.println("-----------Menu-----------");
            System.out.println("[1] - Calcular Preço Total");
            System.out.println("[2] - Calcular Troco");
            System.out.println("[3] - Exibir Historico");
            System.out.println("[4] - Sair");
            resp = scan.nextInt();
            scan.nextLine();
            ValidarResp(resp);
        } while (resp != 4);
        System.out.println("Obrigado por utilizar a calculadora!!!");
    }

    public static void exibirHistorico() {
        System.out.println("-----Historico-de-Vendas-----");
        if(historicoVendas.isEmpty()){
            System.out.println("nenhuma venda registrada");
        }else{
            for(Historico novaVenda : historicoVendas){
                System.out.printf("Quantidade: %d, Total: %.2f, Desconto: %.2f%n", novaVenda.quantPlant, novaVenda.resultTotal, novaVenda.valorDesc);
            }
        }
    }

    public static void PreçoTotal() {

        System.out.println("-----Calcular-Preço-Total-----");
        System.out.println("Adicione a quantidade de plantas vendidas");
        int quantPlant = scan.nextInt();
        scan.nextLine();
        System.out.println("Adicione o preco da plantas vendida");
        double precoPlant = scan.nextDouble();
        scan.nextLine();
        double resultTotal = quantPlant * precoPlant;
        double valorDesc;
        if(quantPlant < 10){
            System.out.printf("O preco total da venda sera %.2f%n", resultTotal);
            valorDesc = 0;
        }else{
            valorDesc = resultTotal * 0.05;
            valorDesc = resultTotal - valorDesc;
            System.out.printf("O original da venda seria %.2f%n", resultTotal);
            System.out.printf("Com desconto fica %.2f%n", valorDesc);
        }

        registrarVenda(quantPlant, resultTotal, valorDesc);
    }

    public static void CalcularTroco() {

        System.out.println("-----Calcular-Troco-----");
        System.out.println("O preco que o cliente pagou");
        double precoCliente = scan.nextDouble();
        scan.nextLine();
        System.out.println("adicione o total da venda");
        double precoTotal = scan.nextDouble();
        scan.nextLine();

        double troco = precoCliente - precoTotal;
        if (troco > 0) {
            System.out.printf("Voce deve %.2f de troco!%n", troco);
        } else if (troco < 0) {
            System.out.printf("O cliente ainda deve %.2f%n", troco);
        } else if (troco == 0) {
            System.out.println("Ninguem deve nada!!!");
        }
    }

    public static void registrarVenda(int quantPlant, double resultTotal, double valorDesc) {
        Historico novaVenda = new Historico(quantPlant, resultTotal, valorDesc);
        historicoVendas.add(novaVenda);
    }
}