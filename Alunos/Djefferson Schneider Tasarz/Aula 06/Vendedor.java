public class Vendedor {
    String nome;
    int idade;
    Loja loja;
    String cidade;
    String bairro;
    String rua;
    double salarioBase;
    double[] salarioRecebido = new double[3];
    
    public Vendedor(String nome, int idade, Loja loja, String cidade, String bairro, String rua, double salarioBase) {
        this.nome = nome;
        this.idade = idade;
        this.loja = loja;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.salarioBase = salarioBase;
        salarioRecebido[0] = 2500.0;
        salarioRecebido[1] = 2800.0;
        salarioRecebido[2] = 2650.0;
    }
    
    public void apresentarse() {
        System.out.println("Vendedor: " + nome + ", Idade: " + idade + ", Loja: " + loja.nomeFantasia);
    }
    
    public double calcularMedia() {
        double soma = 0;
        for (double salario : salarioRecebido) {
            soma += salario;
        }
        return soma / 3;
    }
    
    public double calcularBonus() {
        return salarioBase * 0.2;
    }
}