package controller.response

class AnswersResponse: Response {
    val id: Int? // Question's id
    val answers: Map<String, String>? // <username, answer>

    constructor(id: Int, answers: Map<String, String>) : super() {
        this.id = id
        this.answers = answers
    }

    constructor(successStatus: Result, errorCode: ErrorCodes, errorMessage: String)
            : super(successStatus, errorCode, errorMessage) {
        this.id = null
        this.answers = null
    }

    constructor(successStatus: Result, errorCode: String, errorMessage: String)
            : super(successStatus, errorCode, errorMessage) {
        this.id = null
        this.answers = null
    }
}
