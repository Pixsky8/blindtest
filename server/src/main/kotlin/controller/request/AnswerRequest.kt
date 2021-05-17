package controller.request

class AnswerRequest {
    val questionId: Int
    val answer: String

    constructor(questionId: Int, answer: String) {
        this.questionId = questionId
        this.answer = answer
    }
}
