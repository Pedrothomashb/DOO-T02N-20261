# Atividade Extra

**Nome:** Carlos Eduardo Martini (T02N)

---

### 1. Conceito escolhido: Coroutines
**Timestamp do vídeo que menciona o conceito:** 3:19

**O que é?**
Coroutines são uma solução nativa do Kotlin para gerenciar concorrência e assincronismo. Elas funcionam como "sub-threads" ou linhas de execução extremamente leves. Ao contrário das Threads tradicionais do Java, que consomem muita memória e recursos do sistema operacional, é possível rodar centenas de milhares de corrotinas simultaneamente sem esgotar a memória do computador.

**Pra que serve?**
Servem para executar tarefas que demoram para ser concluídas — como buscar dados em uma API na internet, ler arquivos pesados no disco ou consultar um banco de dados — sem bloquear ou congelar a interface visual do usuário. A corrotina suspende temporariamente a sua execução, permitindo que o processador faça outras tarefas enquanto espera o resultado, e retoma o trabalho de forma limpa quando o download ou processo termina.

**Como é normalmente utilizado?**
O desenvolvedor marca as funções demoradas com a palavra-chave `suspend`. Para iniciar a execução de fato, utilizam-se construtores de escopo como `launch` (para tarefas em segundo plano que não devolvem resultado) ou `async` (quando precisamos que a tarefa retorne um dado futuro).

**Exemplo de código:**
```kotlin
import kotlinx.coroutines.*

suspend fun buscarDadosDoServidor(): String {
    delay(2000L) 
    return "Dados carregados com sucesso"
}

fun main() = runBlocking {
    println("Usuário clicou no botão para atualizar a tela")
    
    launch {
        val resultado = buscarDadosDoServidor()
        println(resultado)
    }
    
    println("A interface gráfica continua fluida e respondendo aos cliques...")
}
```
---

### 2. Conceito escolhido: Extension Functions
**Timestamp do vídeo que menciona o conceito:** 3:28

**O que é?**
As *Extension Functions* (Funções de Extensão) são um recurso do Kotlin que permite adicionar novas funcionalidades a classes que já existem, sem a necessidade de herdar dessas classes ou modificar o seu código-fonte original. É como se pudéssemos "injetar" um método novo dentro de uma classe padrão do sistema (como `String`, `List` ou `Int`).

**Pra que serve?**
Elas servem para tornar o código muito mais limpo, legível e organizado. No Java, quando queríamos fazer algo que uma classe padrão não fazia, éramos obrigados a criar classes utilitárias (ex: `StringUtils.limparTexto(meuTexto)`). Com as funções de extensão, podemos fazer o próprio objeto executar a ação (ex: `meuTexto.limparTexto()`), o que torna a programação muito mais natural e intuitiva.

**Como é normalmente utilizado?**
Para criar uma função de extensão, declaramos uma função comum, mas antes do nome da função, colocamos o nome da classe que queremos estender seguido de um ponto (ex: `String.minhaFuncao()`). Dentro dessa função, a palavra-chave `this` é usada para acessar o conteúdo do objeto que está chamando o método.

**Exemplo de código:**
```kotlin
fun Int.isPar(): Boolean {
    return this % 2 == 0
}

fun main() {
    val numero = 10
    
    if (numero.isPar()) {
        println("O número numero é par!")
    } else {
        println("O número numero é ímpar!")
    }
}