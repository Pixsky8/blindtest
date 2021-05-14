package service.entity

enum class ChangeQuestionEntity {
    OK(""),
    NO_PERM("You must have administrator rights to do that."),
    NO_QUEST("There are no question left.");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}
