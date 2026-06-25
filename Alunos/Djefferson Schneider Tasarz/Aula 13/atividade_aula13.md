# Atividade Extra — Aula 13
### Música: *HelloWorld.java* — Nanowar of Steel

---

## Conceito 1: Garbage Collector

**Nome:** Garbage Collector (Coletor de Lixo)

**Timestamp no vídeo:** ~2:14 — a banda grita literalmente *"Garbage collector!"* como refrão épico.

### O que é?

O **Garbage Collector (GC)** é um mecanismo automático de gerenciamento de memória presente em linguagens como Java. Ele é responsável por identificar e liberar a memória ocupada por objetos que não são mais utilizados pelo programa — os chamados "objetos mortos" ou inacessíveis.

Em linguagens como C e C++, o programador precisa manualmente alocar (`malloc`) e liberar (`free`) memória. Esquecer de liberar causa o famoso *memory leak* (vazamento de memória). O Java elimina esse problema delegando essa responsabilidade ao GC.

### Para que serve?

- Evitar vazamentos de memória.
- Simplificar o desenvolvimento, pois o programador não precisa se preocupar em "desalocar" objetos manualmente.
- Manter a estabilidade da aplicação em execução prolongada.

### Como é normalmente utilizado?

O GC age de forma automática e transparente em segundo plano. O programador não o invoca diretamente (embora seja possível *sugerir* sua execução com `System.gc()`, isso não é garantia nem boa prática).

O JVM decide quando rodar o GC com base no uso de memória. Quando um objeto não possui mais nenhuma referência apontando para ele, ele se torna elegível para coleta.

### Exemplo de código

```java
public class ExemploGC {
    public static void main(String[] args) {
        // Objeto criado e referenciado
        String texto = new String("Hello, World!");

        // A referência é removida — o objeto se torna elegível para GC
        texto = null;

        // Sugestão (não garantia) para o GC rodar
        System.gc();

        System.out.println("Memória possivelmente liberada pelo Garbage Collector!");
    }
}
```

> No exemplo acima, após `texto = null`, o objeto `String` original não possui mais nenhuma referência e pode ser coletado pelo GC na próxima vez que ele rodar.

---

## Conceito 2: NullPointerException

**Nome:** NullPointerException (NPE)

**Timestamp no vídeo:** ~3:02 — a música culmina com *"NullPointerException"*, que "mata" o programa abruptamente, assim como acontece na vida real.

### O que é?

A **NullPointerException** é uma exceção de tempo de execução (*runtime exception*) em Java lançada automaticamente pela JVM quando o programa tenta usar uma referência de objeto que está apontando para `null` — ou seja, para lugar nenhum.

É um dos erros mais comuns e "temidos" entre programadores Java. Na música, a variável `joeyDeCaio` é definida como `null` e depois acessada, causando o crash épico do programa — exatamente o que acontece no mundo real.

### Para que serve (conhecer)?

Entender a NPE é essencial para:
- Depurar erros em aplicações Java.
- Escrever código defensivo que valida referências antes de usá-las.
- Compreender o ciclo de vida de objetos e referências em memória.

### Como é normalmente tratada?

A NPE pode ser prevenida com verificações explícitas de `null`, uso da classe `Optional` (Java 8+), ou boas práticas de inicialização de variáveis.

### Exemplo de código

```java
public class ExemploNPE {
    public static void main(String[] args) {

        // Sem proteção — vai lançar NullPointerException!
        String nome = null;
        // System.out.println(nome.length()); // ← NPE aqui!

        // Forma correta: verificar antes de usar
        if (nome != null) {
            System.out.println("Tamanho: " + nome.length());
        } else {
            System.out.println("A variável 'nome' está nula. Nenhuma ação realizada.");
        }

        // Alternativa moderna com Optional (Java 8+)
        java.util.Optional<String> nomeOpcional = java.util.Optional.ofNullable(nome);
        nomeOpcional.ifPresentOrElse(
            n -> System.out.println("Nome: " + n),
            () -> System.out.println("Valor ausente — Optional protegeu o programa!")
        );
    }
}
```

> A `NullPointerException` ocorre em tempo de execução, não em tempo de compilação. Por isso, o compilador não avisa — o programa simplesmente quebra quando encontra o `null` sendo usado indevidamente.

---

*Referências: [GitHub oficial - NanowarOfSteel/HelloWorld](https://github.com/NanowarOfSteel/HelloWorld) | [LyricsTranslate - anotações técnicas da música](https://lyricstranslate.com/en/nanowar-steel-helloworldjava-lyrics)*
