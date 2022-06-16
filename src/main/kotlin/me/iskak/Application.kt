package me.iskak

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import me.iskak.argorithm.Algorithm
import me.iskak.argorithm.Bubble
import kotlin.random.Random

fun main() {
    embeddedServer(Netty, port = 8084, host = "0.0.0.0") {
        install(WebSockets) {
            contentConverter = JacksonWebsocketContentConverter()
        }

        routing {
            webSocket("/") {
                try {
                    while (true) {
                        val message = receiveDeserialized<Message>()
                        when (message.action) {
                            "random" -> sendSerialized(
                                Message(
                                    message.action, randomArray(message.data as Map<*, *>)
                                )
                            )
                            "algorithms" -> sendSerialized(
                                Message(
                                    message.action, getAlgorithms()
                                )
                            )
                            "sort" -> sortArray(this, message.action, message.data as Map<*, *>)
                        }
                    }
                } catch (_: ClosedReceiveChannelException) {
                }
            }

        }
    }.start(wait = true)
}

suspend fun sortArray(session: DefaultWebSocketServerSession, action: String, data: Map<*, *>) {
    val array: IntArray = (data["array"] as ArrayList<*>)
        .map { it.toString().toInt() }
        .toIntArray()
    val delay: Long = (data["delay"] as String).toLong()
    val algorithm: Algorithm

    when (Algorithms.find(data["algorithm"] as String)) {
        Algorithms.BUBBLE_SORT -> algorithm = Bubble(array)
    }

    while (!algorithm.sorted) {
        session.sendSerialized(Message(action, algorithm.next()))
        delay(delay)
    }

    session.sendSerialized(Message(action, ""))
}

fun getAlgorithms(): List<String> {
    return Algorithms.values().map { algorithm ->
        algorithm.toString()
    }
}

fun randomArray(data: Map<*, *>): IntArray {
    return IntArray(data["size"] as Int) {
        Random.nextInt(data["min"] as Int, data["max"] as Int)
    }
}
