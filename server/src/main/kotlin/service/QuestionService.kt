package service

import repository.QuestionsRepository
import service.entity.ChangeQuestionEntity
import service.entity.QuestionEntity

class QuestionService {
    val questionRepository: QuestionsRepository
    val accountService: AccountService
    var currentQuestion: Int = -1

    constructor(questionRepository: QuestionsRepository,
                accountService: AccountService) {
        this.questionRepository = questionRepository
        this.accountService = accountService
    }

    fun resetCurrentQuestion(username: String, questionId: Int): ChangeQuestionEntity {
        if (!accountService.isAdminAccount(username))
            return ChangeQuestionEntity.NO_PERM
        if (questionRepository.getQuestion(questionId) == null)
            return ChangeQuestionEntity.NO_QUEST
        currentQuestion = questionId
        return ChangeQuestionEntity.OK
    }

    fun nextQuestion(username: String): ChangeQuestionEntity {
        if (!accountService.isAdminAccount(username))
            return ChangeQuestionEntity.NO_PERM
        if (questionRepository.getQuestion(currentQuestion + 1) == null)
            return ChangeQuestionEntity.NO_QUEST
        currentQuestion++
        return ChangeQuestionEntity.OK
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
