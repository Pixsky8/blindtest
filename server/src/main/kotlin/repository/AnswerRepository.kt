package repository

import repository.model.AnswerErrorModel

class AnswerRepository {
    var answerMap = HashMap<Int, HashMap<String, String>>() // <questionId, <username, answer>>

    fun addQuestion(questionId: Int) {
        if (answerMap[questionId] == null)
            answerMap[questionId] = HashMap()
    }

    fun addAnswer(questionId: Int, username: String, answer: String): AnswerErrorModel {
        var questionAnswers = answerMap[questionId] ?: return AnswerErrorModel.NO_QUESTION

        if (questionAnswers[username] != null)
            return AnswerErrorModel.ALREADY_ANSWERED
        questionAnswers[username] = answer
        return AnswerErrorModel.OK
    }

    fun getAnswer(questionId: Int, username: String): String? {
        return answerMap[questionId]?.get(username)
    }

    fun getQuestionAnswer(questionId: Int): Map<String, String>? {
        return answerMap[questionId]
    }

    fun getAnswerMap(): Map<Int, Map<String, String>> {
        return answerMap
    }
}
