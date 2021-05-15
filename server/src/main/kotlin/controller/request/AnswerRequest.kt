package controller.request

class AnswerRequest {
    val questionId: Int
    val anwser: String

    constructor(questionId: Int, anwser: String) {
        this.questionId = questionId
        this.anwser = anwser
    }
}
