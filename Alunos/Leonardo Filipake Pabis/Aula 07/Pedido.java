
import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido {
    private int id;
    private LocalDate dataCriacao;
    private LocalDate dataPagamento;
    private LocalDate dataVencimentoReserva;
    private Clientes cliente;
    private Vendedor vendedor;
    private Lojas loja;
    public ArrayList<Item> itens = new ArrayList<>();

    public Pedido(){

    }

    public Pedido(int id, Clientes cliente, Vendedor vendedor, Lojas loja) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            System.out.println("Digite um id válido:");
            int novoId = Main.scan.nextInt();
            Main.scan.nextLine();
            setId(novoId);
        }
        
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacaoDate) {
        this.dataCriacao = dataCriacaoDate;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamentoString) {
        LocalDate dataPagamentoDate = Date.stringToDate(dataPagamentoString);
        this.dataPagamento = dataPagamentoDate;
    }

    public LocalDate getDataVencimentoReserva() {
        return dataVencimentoReserva;
    }

    public void setDataVencimentoReserva(String dataVencimentoReservaString) {
        LocalDate dataVencimentoReservaDate = Date.stringToDate(dataVencimentoReservaString);
        this.dataVencimentoReserva = dataVencimentoReservaDate;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Lojas getLoja() {
        return loja;
    }

    public void setLoja(Lojas loja) {
        this.loja = loja;
    }

    public ArrayList<Item> getItens() {
        return itens;
    }

    public void adicionarItem(Item item) {
        this.itens.add(item);
    }

    public double calcularValorTotal() {
        double soma = 0;
        for (int i = 0; i < itens.size(); i++) {
            soma += itens.get(i).getValor();
        }
        return soma;
    }

    public void gerarDescricaoVenda() {
        System.out.println("Criação: " + dataCriacao + " | Total: " + calcularValorTotal());
    }

    


    
}
