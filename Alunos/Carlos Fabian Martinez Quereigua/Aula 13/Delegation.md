# Pesquisa sobre Delegation

## Nome
Carlos Fabian Martinez Quereigua

## Conceito escolhido
Delegation

## Timestamp do vídeo que menciona o conceito
Aproximadamente 2:05

> "Multi-inheritance and delegation, and the flamboyant data class"

## O que é?

Delegation é um recurso do Kotlin que permite que uma classe repasse parte de suas responsabilidades para outro objeto. Em vez de implementar diretamente todos os métodos de uma interface, uma classe pode delegar essa implementação para outra classe, reduzindo a quantidade de código repetitivo e facilitando a manutenção do sistema.

## Pra que serve?

A delegação é utilizada para promover o reaproveitamento de código e diminuir o acoplamento entre classes. Ela permite que uma classe utilize funcionalidades de outra sem precisar herdar seu comportamento, sendo considerada uma alternativa mais flexível à herança em diversas situações.

## Como é normalmente utilizado?

No Kotlin, a delegação é frequentemente utilizada para implementar interfaces de forma simples e elegante. Ao delegar uma interface para outro objeto, o compilador gera automaticamente os métodos necessários, evitando que o desenvolvedor precise escrever código repetitivo.

## Exemplo de código

```kotlin
interface Mensagem {
    fun enviar()
}

class MensagemEmail : Mensagem {
    override fun enviar() {
        println("Enviando mensagem por e-mail")
    }
}

class Notificacao(private val mensagem: Mensagem) : Mensagem by mensagem

fun main() {
    val email = MensagemEmail()
    val notificacao = Notificacao(email)

    notificacao.enviar()
}
```

### Explicação do exemplo

Nesse exemplo, a classe `Notificacao` não implementa o método `enviar()` diretamente. Utilizando a palavra-chave `by`, ela delega essa responsabilidade ao objeto `MensagemEmail`. Assim, quando o método `enviar()` é chamado, a execução é encaminhada automaticamente para a classe delegada, evitando a duplicação de código.