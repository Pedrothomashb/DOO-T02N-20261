# Garbage Collector em Java

**Nome:** Henrique  
**Conceito escolhido:** Garbage Collector  
**Timestamp do vídeo que menciona o conceito:** [2:14](https://www.youtube.com/watch?v=BsfXZjKLT9AA&t=134s) — *"Garbage collector!"*

---

## O que é?

O **Garbage Collector** (GC) é um componente automático da JVM (Java Virtual Machine) responsável por liberar memória que não está mais sendo utilizada pelo programa.

Em linguagens como C ou C++, o programador precisa manualmente alocar e desalocar memória (usando `malloc`/`free` ou `new`/`delete`). Esquecer de liberar memória causa o chamado *memory leak* — a memória vai sendo ocupada progressivamente até o programa travar ou ser encerrado pelo sistema operacional.

Java resolve esse problema com o Garbage Collector: quando um objeto não possui mais nenhuma referência apontando para ele, o GC identifica automaticamente que aquele espaço na memória pode ser liberado e o recupera para uso futuro.

---

## Para que serve?

- Evitar **memory leaks** (vazamentos de memória)
- Simplificar o desenvolvimento, pois o programador não precisa se preocupar com desalocação manual
- Aumentar a **segurança** do programa, eliminando uma classe inteira de bugs perigosos relacionados a acesso indevido à memória

---

## Como é normalmente utilizado?

O Garbage Collector age **de forma transparente** — o programador não precisa chamá-lo diretamente. Basta parar de usar um objeto (ou seja, não manter nenhuma variável referenciando-o) e, em algum momento, o GC irá coletá-lo.

É possível *sugerir* ao GC que ele rode chamando `System.gc()`, mas isso **não garante** execução imediata — é apenas uma dica à JVM.

```java
public class ExemploGC {
    public static void main(String[] args) {
        // Cria um objeto e atribui uma referência a ele
        String texto = new String("Olá, mundo!");

        // A partir daqui, "texto" ainda aponta para o objeto — ele NÃO será coletado
        System.out.println(texto);

        // Removendo a referência: o objeto agora é elegível para coleta
        texto = null;

        // O GC poderá liberar a memória ocupada pelo objeto anterior em algum momento
        System.gc(); // sugestão, não garante execução imediata
    }
}
```

> **Observação:** Na prática, raramente se usa `System.gc()` em código de produção. O GC da JVM moderna (como o G1GC ou ZGC) é altamente otimizado e gerencia a memória com muito mais eficiência do que chamadas manuais.
