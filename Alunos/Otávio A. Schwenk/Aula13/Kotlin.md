# Kotlin:
## *Uma JVM sem verbosidade desnecessária*
### Aqui abordarei dois conceitos sobre ele que NÃO abordamos em aula.  

## Os conceitos abordados:
- **Data Class - 2:15**
- **Coroutines - 3:20**  

## Introdução  
Kotlin é uma linguagem de programação moderna voltada para a JVM criada
para ser mais concisa, segura e produtiva que o Java. Ela traz recursos
como inferência de tipos, null safety, coroutines e sintaxe reduzida,
mantendo total interoperabilidade com Java.

No vídeo da banda Nanowar of Steel, esses conceitos são apresentados de
forma bem humorada, usando uma música para explicar as principais
características da linguagem e como ela se diferencia do Java no
desenvolvimento moderno.

## Data Class

*Conceito exposto aos **2 minutos e 15 segundos (2:15)** de vídeo*

**O que é?**  
É um tipo especial de classe usado para representar dados simples. Seu foco 
principal é armazenar informações, não executar regras de negócio. O Kotlin 
gera automaticamente métodos básicos como comparação, representação em texto 
e cópia de objetos.

**Pra que serve?**  
Serve para reduzir código repetitivo e facilitar o trabalho com estruturas de 
dados. Permite que objetos sejam comparados pelo conteúdo e não pela 
referência, tornando o uso mais previsível em aplicações.

**Como é normalmente utilizado?**  
É muito usada para representar dados que trafegam entre camadas do sistema, 
como respostas de API, requisições, modelos de usuário ou qualquer estrutura 
simples que apenas carrega informações.

**Pontos negativos**  
Não é adequada para lógica complexa, pois pode levar a má organização do 
código. O uso indevido pode causar problemas de consistência quando há 
mutabilidade. Também pode incentivar modelagens simplificadas demais que não 
refletem regras reais do sistema.

### Exemplo:
- **Kotlin:**
```Kotlin
data class Usuario(
    val nome: String,
    val idade: Int
)
```
*Aqui é possível utilizar **equals()**, **hashCode()**, **toString()** e **copy()** mesmo sem ter criado eles.*

---

- **Java (criação manual da classe):**

```Java
public class Usuario {

    private final String nome;
    private final int idade;

    public Usuario(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    @Override
    public String toString() {
        return "Usuario(nome=" + nome + ", idade=" + idade + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;

        Usuario usuario = (Usuario) o;

        return idade == usuario.idade &&
               nome.equals(usuario.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode() + idade;
    }
}
```
*No Java não existe Data Class, então é necessário criar tudo de forma manual.* 

---

## Coroutine:

*Conceito exposto aos **3 minutos e 20 segundos (3:20)** de vídeo*

**O que é?**  
É um mecanismo de programação assíncrona leve usado para executar tarefas sem 
bloquear threads. Permite pausar e retomar execuções de forma controlada, 
mantendo o estado da tarefa.

**Pra que serve?**  
Serve para lidar com operações demoradas sem travar a aplicação, como 
requisições de rede, acesso a banco de dados e processamento paralelo. 
Melhora desempenho e responsividade, principalmente em aplicações com muitas 
operações simultâneas.

**Como é normalmente utilizado?**  
É aplicado em tarefas assíncronas usando escopos controlados e funções 
específicas para execução paralela ou concorrente. Muito comum em Android e 
backend para evitar bloqueio de interface e organizar fluxos assíncronos de 
forma mais legível.

**Pontos negativos**  
Exige controle rigoroso de escopos e concorrência. Uso incorreto pode gerar 
vazamento de tarefas, comportamento imprevisível e dificuldade de depuração. 
Também pode aumentar a complexidade do sistema se mal estruturado.

### Exemplo:
- **Kotlin:**
```Kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        delay(1000)
        println("Tarefa 1 concluída")
    }

    val resultado = async {
        delay(1000)
        "Resultado da tarefa 2"
    }

    println(resultado.await())
}
```
*Com o Kotlin é possível executar tarefas assíncronas (nesse caso, prints) sem 
bloquear a thread principal, usando **launch**, **async**, **delay** e 
**runBlocking**.*

---

- **Java (mesma tarefa, mas usando threads):**
```Java
public class Main {

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Tarefa 1 concluída");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Resultado da tarefa 2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
    }
}
```
*O controle manual de threads substitui as coroutines, exigindo mais 
gerenciamento e não permitindo suspensão leve como no Kotlin.*

---

*"Come and join the army of KOTLIN"*  
-Nanowar Of Steel