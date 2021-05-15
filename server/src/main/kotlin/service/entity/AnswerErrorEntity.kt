package service.entity

enum class AnswerErrorEntity {
    NO_QUEST("There are currently no question to answer."),
    ALREADY_ANS("You already answered the question."),
    WRONG_QUEST("You are answering the wrong question."),
    OK("");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
