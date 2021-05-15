package service

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import repository.AnswerRepository
import repository.GameRepository

class GameService {
    val gameService: GameRepository
    val gameConnections: Set<DefaultWebSocketSession>
    val logger = LoggerFactory.getLogger(GameService::class.java)

    constructor(gameService: GameRepository,
                gameConnections: Set<DefaultWebSocketSession>) {
        this.gameService = gameService
        this.gameConnections = gameConnections
    }

    fun sendQuestionUpdateNotification() {
        GlobalScope.async {
            logger.info("Sending update notification.")
            for (connection in gameConnections)
                connection.outgoing.send(Frame.Text("UPDATE"))
        }
    }

    fun resetScoreBoard() {
        gameService.resetScoreBoard()
    }

    fun givePoints(username: String, points: Int) {
        gameService.givePoints(username, points)
    }

    fun getScoreBoard(): Map<String, Int> {
        return gameService.getScoreBoard()
    }
}
