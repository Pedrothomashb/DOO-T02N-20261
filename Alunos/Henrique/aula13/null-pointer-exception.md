# NullPointerException em Java

**Nome:** Henrique  
**Conceito escolhido:** NullPointerException  
**Timestamp do vídeo que menciona o conceito:** [3:48](https://www.youtube.com/watch?v=BsfXZjKLT9AA&t=228s) — *"Yoow! Null pointer exception!"*

---

## O que é?

`NullPointerException` (NPE) é uma exceção em tempo de execução (*runtime exception*) lançada pela JVM quando o programa tenta utilizar uma referência que está apontando para `null` — ou seja, para nenhum objeto.

Em Java, variáveis de referência (como objetos, Strings, arrays) podem receber o valor especial `null`, que indica a **ausência de objeto**. Se o programa tentar acessar um método ou atributo através dessa referência nula, a JVM não sabe para onde ir na memória e lança a `NullPointerException`, abortando a execução do trecho de código.

Na música, isso acontece quando a variável `joeyDeCaio` é declarada como `null` e em seguida é acessada — causando o crash dramático que interrompe o "Hello World" no meio da frase.

---

## Para que serve (entender o conceito)?

Compreender a NPE é essencial porque ela é uma das exceções **mais comuns** no dia a dia de um programador Java. Saber identificar sua causa permite:

- Depurar erros com mais rapidez
- Escrever código defensivo que verifica `null` antes de usar uma referência
- Utilizar recursos modernos do Java (como `Optional`) para evitar NPEs por design

---

## Como é normalmente utilizado / evitado?

```java
public class ExemploNPE {
    public static void main(String[] args) {

        // Declarando uma String como null
        String nome = null;

        // ERRO: tenta chamar um método em uma referência nula
        // Isso lança NullPointerException em tempo de execução!
        // System.out.println(nome.length());

        // FORMA CORRETA: verificar se é null antes de usar
        if (nome != null) {
            System.out.println("Tamanho: " + nome.length());
        } else {
            System.out.println("A variável nome está nula!");
        }

        // FORMA MODERNA (Java 8+): usar Optional para evitar NPE por design
        java.util.Optional<String> nomeOpcional = java.util.Optional.ofNullable(nome);
        nomeOpcional.ifPresentOrElse(
            n -> System.out.println("Nome: " + n),
            () -> System.out.println("Nenhum nome informado.")
        );
    }
}
```

> **Dica:** A partir do **Java 14**, as mensagens de `NullPointerException` ficaram muito mais detalhadas, indicando exatamente qual variável era `null` — o que facilita bastante a depuração.
