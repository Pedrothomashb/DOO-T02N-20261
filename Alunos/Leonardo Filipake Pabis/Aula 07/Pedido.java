
import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido{
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
        setId(id);
        setCliente(cliente);
        setVendedor(vendedor);
        setLoja(loja);
        setDataCriacao();
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

    public String getDataCriacaoString() {
        String dataAtualString = Date.dataAtualString();
        return dataAtualString;
    }

    public LocalDate getDataCriacaoDate(){
        return dataCriacao;
    }

    public void setDataCriacao() {
        this.dataCriacao = Date.dataAtualDate();
    }

    public String getDataPagamentoString() {
        return Date.dateToString(dataPagamento);
    }

    public LocalDate getDataPagamentoDate(){
        return dataPagamento;
    }

    public void setDataPagamento() {
        int escolha=0;
        do { 
            System.out.println("1 - Agendar pagamento\n2 - Pagar Agora");
            escolha = Main.scan.nextInt();
            Main.scan.nextLine();
            if (escolha == 1){
            System.out.println("Para qual data deseja agendar? (dd/mm/aaaa)");
            String data = Main.scan.nextLine();
            LocalDate dataCerta = Date.stringToDate(data);
            this.dataPagamento = dataCerta;
            }
            else if (escolha == 2){
                this.dataPagamento = Date.dataAtualDate();
            }else {
                System.out.println("Digite uma opcão válida:");
            }
        } while (escolha != 1 || escolha != 2);
       
    }

    public String getDataVencimentoReservaString(){
        return Date.dateToString(dataVencimentoReserva);
    }

    public LocalDate getDataVencimentoReservaDate() {
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

    public void getItens() {
        for (int i = 0; i < itens.size(); i++) {
            System.out.print("Item: "+(i+1)+" | " + itens.get(i).gerarDescicao());
        }
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
        System.out.println("Id do pedido: " + id + " | Criação: " + getDataCriacaoString() + " | Total: " + calcularValorTotal());
    }

    


    
}
