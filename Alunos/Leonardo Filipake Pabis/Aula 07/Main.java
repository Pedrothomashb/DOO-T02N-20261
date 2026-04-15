
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scan = new Scanner(System.in);
    static int opcao = 1000;
    static ArrayList <Vendas> vendas = new ArrayList<>();
    static double valor_de_venda_total = 0;
    
    public static void main(String[] args){
        Lojas loja1 = new Lojas("Lojinha", "LOJINHARAZAOSOCIAL", "12345676543211", "Cafelandia", "Bairro nobre", "Rua rapida");
        loja1.apresentarse();
        System.out.println(loja1.getEndereco());
        loja1.getEndereco().apresentarLogradouro();

        Lojas loja2 = new Lojas("Lojão", "LOJÃORAZAOSOCIAL", "19283740594837", "Cascavel", "Bairro pobre", "Rua devagar");
        loja2.apresentarse();
        System.out.println(loja2.getEndereco());
        loja2.getEndereco().apresentarLogradouro();

        Vendedor vend1 = new Vendedor("Leo", 18, loja2, 1200.0, "Cascavel", "Bairro pobre 2", "rua devagar 2");
        vend1.receberSalario(1200);
        vend1.receberSalario(1250);
        vend1.receberSalario(1300);
        vend1.apresentarse();
        System.out.println(vend1.calcularMediaSalarial());
        System.out.println(vend1.calcularBonusSalarial());
        loja2.vendedores.add(vend1);
        

        Vendedor vend2 = new Vendedor("Patrick", 22, loja1, 1500.0, "cidade jghfi", "bairro nbfd", "rua popo");
        vend2.receberSalario(1500);
        vend2.receberSalario(1550);
        vend2.receberSalario(1600);
        vend2.apresentarse();
        System.out.println(vend2.calcularMediaSalarial());
        System.out.println(vend2.calcularBonusSalarial());
        loja1.vendedores.add(vend2);
        
        Clientes cliente10 = new Clientes("Eduardo", 30, "NY", "bairro country", "rua da lapa");

        Clientes cliente20 = new Clientes("Jemer", 45, "Fortaleza", "bairro cinema", "rua abelha");

        loja1.clientes.add(cliente20);
        loja2.clientes.add(cliente10);
        
        Main.dadosLojas.add(loja1);
        Main.dadosLojas.add(loja2);

        Gerente gerente1 = new Gerente("Gerentinho", 50, loja2, 3500.0, "Cidade grande", "bairro grande", "rua grande");

        gerente1.receberSalario(4000);
        gerente1.receberSalario(4250);
        gerente1.receberSalario(4500);
        gerente1.apresentarse();
        System.out.println(gerente1.calcularMediaSalarial());
        System.out.println(gerente1.calcularBonusSalarial());

        Gerente gerente2 = new Gerente("Gerentão", 70, loja1, 5000.0, "cidade pequena", "bairro pequeno", "rua pequena");
        gerente2.receberSalario(5000);
        gerente2.receberSalario(5250);
        gerente2.receberSalario(5500);
        gerente2.apresentarse();
        System.out.println(gerente2.calcularMediaSalarial());
        System.out.println(gerente2.calcularBonusSalarial());

        Item item1 = new Item(1, "Rosácea", "flor rosa", 10.0);
        Item item2 = new Item(10, "Girassol", "flor amarela", 25.0);

        System.out.println(item1.gerarDescicao());
        System.out.println(item2.gerarDescicao());
        
        Pedido pedido5 = ProcessaPedido.processar(5, cliente10, vend2, loja1);
        pedido5.setDataVencimentoReserva("12/12/2026");
        pedido5.adicionarItem(item1);
        pedido5.adicionarItem(item2);
        pedido5.getItens();
        pedido5.gerarDescricaoVenda();
        System.out.println(pedido5.getDataVencimentoReservaString());
        ProcessaPedido.pagamentoConfirmado(pedido5);
        pedido5.setDataVencimentoReserva("14/04/2026");
        System.out.println(pedido5.getDataVencimentoReservaString());
        ProcessaPedido.pagamentoConfirmado(pedido5);

        menu();

    }
    
    public static void menu(){
        //apagar a vendas.java - criar (1 - Criar pedido) - imprimir a lista de itens e escolher o numero dele e perguntar a quantidade, ir adicionando no pedido, após isso, ir para o pagamento, onde vai pagar agora ou agendar - 2, da pra continuar - 3, refatorar para adicionar o pedido na lista - 4 e 5, acho que tirar, pois os proprios pedidos tem data de criacao, refatorar o 5 para poder buscar no dia - refatorar o 6, para mostar tudo certinho
        // apos processar o pagamento, colocar a chave do dia e o valor no hashmap do Date, e a busca acho que serve já
        System.out.println("********");
        System.out.println("* MENU *");
        System.out.println("********");
        System.out.println("1 - Cálculo de preço total");
        System.out.println("2 - Cálculo de troco");
        System.out.println("3 - Consultar vendas");
        System.out.println("4 - Alocar vendas totais");
        System.out.println("5 - Buscar vendas do dia");
        System.out.println("6 - Listar dados (loja, vendedores e clientes)");
        System.out.println("7 - Sair");
        
        do{
            System.out.println("Digite uma opção válida");
            opcao = scan.nextInt();
            scan.nextLine();
            if (opcao == 1){
                CalculoPrecoTotal();
            }else if (opcao == 2){
                CalculoDeTroco();
            }else if (opcao == 3){
                ConsultarVendas();
            }else if (opcao == 4){
                Date.EscolherDia(valor_de_venda_total);
            }else if (opcao == 5){
                Date.BuscarVendaDia();
            }else if (opcao == 6){
                listarDados();
            }
            else if (opcao == 7){
                Sair();
            }
        }while (opcao != 7);

    }
    public static void CalculoPrecoTotal(){
        Vendas venda = new Vendas();
        System.out.println("Digite a quantidade da planta vendida:");
        int quantidade = scan.nextInt();
        scan.nextLine();
        venda.setQuantidade(quantidade);
        System.out.println("Digite o preço unitário da planta:");
        double preco = scan.nextDouble();
        scan.nextLine();
        double preco_total = preco * quantidade;
        venda.setPreco(preco_total);
        if (quantidade > 10){
            double valorDesconto = preco_total * 0.05;
            preco_total -= valorDesconto;
            venda.setvalorDescontoRecebido(valorDesconto);
        }
        System.out.printf("Preço Total: %.2f\n", preco_total);
        System.out.println("Venda cadastrada no sistema\n");
        vendas.add(venda);
        valor_de_venda_total += preco_total;
        VoltarMenu();
    }
    public static void CalculoDeTroco(){
        System.out.println("Valor recebido pelo cliente:");
        double valor = scan.nextDouble();
        scan.nextLine();
        System.out.println("Valor total da compra:");
        double total = scan.nextDouble();
        scan.nextLine();
        if (valor-total < 0){
            System.out.println("Valor insuficiênte, tente novamente");
            CalculoDeTroco();
        }else{
            System.out.printf("Troco a ser devolvido: %.2f\n", valor-total);
            VoltarMenu();
        }
    }

    public static void VoltarMenu(){
        System.out.println("Pressione enter para voltar ao menu");
        scan.nextLine();
        menu();
    }

    public static void ConsultarVendas(){
        System.out.println("Número da venda - quantidade - preço total - desconto recebido - preço descontado\n");
        for (int i = 0; i < vendas.size(); i++){
            System.out.println(i+1+" - "+vendas.get(i).getQuantidade()+" - "+vendas.get(i).getPreco()+" - "+ vendas.get(i).getvalorDescontoRecebido()+" - "+(vendas.get(i).getPreco()-vendas.get(i).getvalorDescontoRecebido())+"\n");
        }
        System.out.printf("Vendas totais(não alocadas): %.2f\n\n", valor_de_venda_total);
        VoltarMenu();
    }

    public static void Sair(){
        System.out.println("Finalizando o systema");
    }


    public static ArrayList<Lojas> dadosLojas = new ArrayList<>(); 

    public static void listarDados(){
        for (int i = 0; i < dadosLojas.size(); i++){
            dadosLojas.get(i).apresentarse();
            for (int j = 0; j < dadosLojas.get(i).vendedores.size(); j++){
                System.out.printf(" -");
                dadosLojas.get(i).vendedores.get(j).apresentarse();
            }
            for (int k = 0; k < dadosLojas.get(i).clientes.size(); k++){
                System.out.printf(" -");
                dadosLojas.get(i).clientes.get(k).apresentarse();
            }
        }
        VoltarMenu();
    }

}


