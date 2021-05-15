package service

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import repository.AnswerRepository

class GameService {
    val gameConnections: Set<DefaultWebSocketSession>
    val logger = LoggerFactory.getLogger(GameService::class.java)

    constructor(gameConnections: Set<DefaultWebSocketSession>) {
        this.gameConnections = gameConnections
    }

    fun sendQuestionUpdateNotification() {
        GlobalScope.async {
            logger.info("Sending update notification.")
            for (connection in gameConnections)
                connection.outgoing.send(Frame.Text("UPDATE"))
        }
    }
}
