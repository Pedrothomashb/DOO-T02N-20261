

public class ProcessaPedido {
    Pedido pedido = new Pedido();

    public void processar(int id, Clientes cliente, Vendedor vendedor, Lojas loja) {
        pedido = new Pedido(id, cliente, vendedor, loja);
        pedido.setDataCriacao();
    }

    private boolean confirmarPagamento() {
        if (pedido.getDataVencimentoReserva().isBefore(Date.dataAtualToFormat())){
            return true;
        } else {
            return false;
        }
    }
}
