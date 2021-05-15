package controller.response

class ScoreboardResponse : Response {
    val scoreboard: Map<String, Int>?

    constructor(scoreboard: Map<String, Int>) {
        this.scoreboard = scoreboard
    }

    constructor(successStatus: Result, errorCode: ErrorCodes, errorMessage: String) : super(
        successStatus,
        errorCode,
        errorMessage
    ) {
        scoreboard = null
    }

    constructor(successStatus: Result, errorCode: String, errorMessage: String) : super(
        successStatus,
        errorCode,
        errorMessage
    ) {
        scoreboard = null
    }
}
