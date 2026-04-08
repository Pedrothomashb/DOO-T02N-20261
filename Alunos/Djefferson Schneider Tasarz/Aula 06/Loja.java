public class Loja {
    String nomeFantasia;
    String razaoSocial;
    String cnpj;
    String cidade;
    String bairro;
    String rua;
    Vendedor[] vendedores;
    Cliente[] clientes;
    
    public Loja(String nomeFantasia, String razaoSocial, String cnpj, String cidade, String bairro, String rua) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        vendedores = new Vendedor[2];
        vendedores[0] = new Vendedor("João Silva", 28, this, "Cascavel", "Centro", "Rua das Flores", 2000.0);
        vendedores[1] = new Vendedor("Maria Santos", 35, this, "Cascavel", "Centro", "Rua das Flores", 2200.0);
        clientes = new Cliente[3];
        clientes[0] = new Cliente("Ana Oliveira", 42, "Cascavel", "Centro", "Rua das Flores");
        clientes[1] = new Cliente("Pedro Lima", 29, "Cascavel", "Centro", "Av. Brasil");
        clientes[2] = new Cliente("Carla Souza", 38, "Cascavel", "Centro", "Rua das Flores");
    }
    
    public int contarClientes() {
        return clientes.length;
    }
    
    public int contarVendedores() {
        return vendedores.length;
    }
    
    public void apresentarse() {
        System.out.println("Loja: " + nomeFantasia);
        System.out.println("CNPJ: " + cnpj);
        System.out.println("Endereço: " + cidade + ", " + bairro + ", " + rua);
    }
    
    public static void main(String[] args) {
        Loja myPlant = new Loja("My Plant", "My Plant Ltda", "12.345.678/0001-99", "Cascavel", "Centro", "Rua Principal 123");
        myPlant.apresentarse();
        System.out.println("Total clientes: " + myPlant.contarClientes());
        System.out.println("Total vendedores: " + myPlant.contarVendedores());
        myPlant.vendedores[0].apresentarse();
        System.out.println("Média salários: R$ " + myPlant.vendedores[0].calcularMedia());
        System.out.println("Bônus: R$ " + myPlant.vendedores[0].calcularBonus());
        for (Cliente c : myPlant.clientes) {
            c.apresentarse();
        }
    }
}