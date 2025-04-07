package org.example

import io.javalin.Javalin
import io.javalin.websocket.WsContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
val playerQueue: Channel<WsContext> = Channel(Channel.UNLIMITED)

fun main() {
    Javalin.create {
        it.router.mount {
            it.ws("/connect") { ws ->
                ws.onConnect { ctx ->
                    playerQueue.trySend(ctx)
                }
            }
        }
    }.start(8080)

    CoroutineScope(Dispatchers.Default).launch {
        while(true) {
            val firstPlayer = playerQueue.receive()
            val secondPlayer = playerQueue.receive()
            createGameSession(firstPlayer, secondPlayer)
        }
    }

}

suspend fun createGameSession(player1: WsContext, player2: WsContext) {

}