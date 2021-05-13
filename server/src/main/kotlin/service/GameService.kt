package service

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep

class GameService {
    val gameConnections: Set<DefaultWebSocketSession>
    val logger = LoggerFactory.getLogger(GameService::class.java)

    constructor(gameConnections: Set<DefaultWebSocketSession>) {
        this.gameConnections = gameConnections
    }

    fun sendUpdateNotification() {
        GlobalScope.async {
            logger.info("Sending update notification.")
            for (connection in gameConnections)
                connection.outgoing.send(Frame.Text("UPDATE"))
        }
    }
}
