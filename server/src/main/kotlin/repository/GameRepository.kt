package repository

class GameRepository {
    private val scoreboard: HashMap<String, Int> = HashMap() // <username, score>

    fun resetScoreBoard() {
        scoreboard.clear()
    }

    fun givePoints(username: String, nbPoint: Int) {
        val currScore = scoreboard[username]
        if (currScore == null)
            scoreboard[username] = nbPoint
        else
            scoreboard[username] = nbPoint + currScore
    }

    fun getScoreBoard(): Map<String, Int> {
        return scoreboard
    }
}
