package service

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameService {
    val gameConnections: Set<DefaultWebSocketSession>

    constructor(gameConnections: Set<DefaultWebSocketSession>) {
        this.gameConnections = gameConnections
    }

    fun sendUpdateNotification() {
        GlobalScope.launch(Dispatchers.Main) {
            for (connection in gameConnections)
                connection.outgoing.send(Frame.Text("UPDATE"))
        }
    }
}
