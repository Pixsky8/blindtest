package service

import repository.QuestionsRepository
import service.entity.QuestionEntity

class QuestionService {
    val questionRepository: QuestionsRepository
    var currentQuestion: Int = -1

    constructor(questionRepository: QuestionsRepository) {
        this.questionRepository = questionRepository
    }

    fun resetCurrentQuestion(username: String, questionId: Int) {
        // TODO check username is an admin
        currentQuestion = questionId;
    }

    fun nextQuestion(username: String) {
        // TODO check username is an admin
        currentQuestion++
    }

    fun getQuestion(questionId: Int): QuestionEntity? {
        val questionModel = questionRepository.getQuestion(questionId) ?: return null
        return QuestionEntity(
            questionModel.id!!,
            questionModel.theme!!,
            questionModel.audio!!,
            questionModel.audioFile,
            questionModel.question
        )
    }

    fun getCurrentQuestion(): QuestionEntity? {
        if (currentQuestion < 0)
            return null

        return getQuestion(currentQuestion)
    }
}
