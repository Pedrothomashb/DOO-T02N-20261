**Nome:
Conceito escolhido:
Timestamp do vídeo que menciona o conceito:
O que é? Pra que serve? Como é normalmente utilizado? Exemplo de código.**

# João Vitor Cavalheiro

## Elvis Operators
Timestamp: 0:52 à 0:56

### O que é?
O operador Elvis (representado pelo símbolo ?:) é um recurso de tratamento de valores null nativo do Kotlin. Ele recebeu esse nome porque, se você olhar o símbolo de lado, os dois pontos lembram os olhos e a interrogação lembra o topete do cantor Elvis Presley.

### Pra que serve?
Ele serve como ferramenta de segurança e contingência no código, sua função principal é fornecer um valor reserva a uma variável que tenha seu valor como null, para que o programa não trave ou gere erros inesperados.

### Como é normalmente utilizado?

Ele é utilizado logo após a chamada de uma variável que aceita null. O Kotlin avalia a expressão da esquerda: se ela possuir um valor válido, o operador simplesmente ignora tudo o que está à sua direita e mantém o valor original, mas se a variável da esquerda for null, a rede de segurança é acionada e o valor definido à direita é o que será atribuído ou exibido.

### Exemplo em Código
     
    fun main(){

        val nomeUsuario: String? = null
        val exibicao = nomeUsuario ?: "Visitante"

        //Exibição: Olá, Visitante
        println("Olá, $exibicao!")
    }

Acima está um exemplo em código, funciona da seguinte maneira, a variável auxiliar atribui-se à sua variável que pode conter valor null juntamente com o operador Elvis(?:) e logo após ele atribui-se o valor de contingência, no caso do código acima a exibição do mesmo seria "Olá, Visitante", pois a variável nomeUsuario não possui valor e Visitante foi o valor escolhido para contingência. É possível também codificá-lo sem a necessidade de uma variável auxiliar como no código abaixo.

    fun main(){

        val nomeUsuario: String? = null

        println("Olá, ${nomeUsuario ?: "Visitante"}!")
    }

## Coroutines
Timestamp: 3:16 à 3:20

### O que é?
As Coroutines (ou corrotinas) são threads leves gerenciadas diretamente pelo framework do Kotlin, e não pelo sistema operacional. Elas são componentes de código projetados para executar tarefas de forma cooperativa e assíncrona.

### Pra que serve?
Elas servem para executar tarefas demoradas ou pesadas em segundo plano, sem travar a tela do aplicativo para o usuário. Elas resolvem o problema de lentidão e congelamento de sistemas com um consumo de memória infinitamente menor do que o uso de threads tradicionais.

### Como é normalmente é utilizado?
No dia a dia, as corrotinas são utilizadas principalmente para tirar tarefas demoradas e pesadas da tela principal do aplicativo e jogá-las para rodar em segundo plano de forma assíncrona. O cenário mais comum acontece em operações de rede ou banco de dados, como quando o sistema precisa carregar o feed de uma rede social pela internet, salvar informações no disco ou carregar imagens de alta resolução. Em vez de fazer o usuário esperar com a interface totalmente travada e congelada enquanto o processamento acontece, as corrotinas realizam essa espera nos bastidores de maneira cooperativa. Dessa forma, a interface do aplicativo permanece totalmente fluida e responsiva, permitindo que o usuário continue navegando normalmente ou veja uma animação de carregamento sem engasgos até que os dados fiquem prontos e surjam na tela.

### Exemplo em Código

```
import kotlinx.coroutines.*

fun main() = runBlocking { 

    println("Iniciando o aplicativo...")

    launch { 
        fazerDownloadPesado() 
    }
    
    println("A tela continua livre para o usuário mexer!")
}

suspend fun fazerDownloadPesado() {

    delay(2000)
    println("Download concluído com sucesso!")
}
```
O código acima   exemplifica o funcionamento das corrotinas simulando um download em segundo plano. Inicialmente, o comando runBlocking abre um escopo que permite gerenciar tarefas assíncronas, garantindo que o programa principal não seja encerrado antes que o processo termine.Assim que o aplicativo inicia, a primeira mensagem é exibida. Logo em seguida, o comando launch dispara a função fazerDownloadPesado() em segundo plano. Como o sistema adota uma postura de disparar e esquecer, ele não espera a tarefa acabar para avançar, imprimindo imediatamente o aviso de que a tela continua livre para o usuário.Por fim, a função de download usa a palavra-chave suspend e o comando delay(2000) para pausar a si mesma por dois segundos, assim que o tempo se esgota, conclui a tarefa exibindo a última mensagem.




