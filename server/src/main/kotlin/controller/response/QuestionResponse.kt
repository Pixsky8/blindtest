package controller.response

class QuestionResponse : Response {
    var id: Int?
    var theme: String?
    var audio: Boolean?
    var question: String?

    constructor(id: Int, theme: String, audio: Boolean, question: String?) : super() {
        this.id = id
        this.theme = theme
        this.audio = audio
        this.question = question
    }

    constructor(successStatus: Result, errorCode: String, errorMessage: String) : super(
        successStatus,
        errorCode,
        errorMessage
    ) {
        id = null
        theme = null
        audio = null
        question = null
    }
}
