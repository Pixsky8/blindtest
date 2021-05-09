package controller.request

class AnwserRequest {
    val questionId: Int
    val anwser: String

    constructor(questionId: Int, anwser: String) {
        this.questionId = questionId
        this.anwser = anwser
    }
}
