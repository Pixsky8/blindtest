package service

import repository.AnswerRepository
import repository.QuestionsRepository
import repository.model.AnswerErrorModel
import service.discord.DiscordService
import service.entity.AnswerErrorEntity
import service.entity.ChangeQuestionEntity
import service.entity.QuestionEntity

class QuestionService {
    val answerRepository: AnswerRepository
    val questionRepository: QuestionsRepository
    val accountService: AccountService
    val discordService: DiscordService
    val gameService: GameService
    var currentQuestion: Int = -1

    constructor(answerRepository: AnswerRepository,
                questionRepository: QuestionsRepository,
                accountService: AccountService,
                discordService: DiscordService,
                gameService: GameService) {
        this.answerRepository = answerRepository
        this.questionRepository = questionRepository
        this.accountService = accountService
        this.discordService = discordService
        this.gameService = gameService
    }

    fun setCurrentQuestion(username: String, questionId: Int): ChangeQuestionEntity {
        if (!accountService.isAdminAccount(username))
            return ChangeQuestionEntity.NO_PERM

        val newQuestion = questionRepository.getQuestion(questionId)
        if (newQuestion == null)
            return ChangeQuestionEntity.NO_QUEST

        currentQuestion = questionId

        answerRepository.addQuestion(currentQuestion)
        gameService.sendQuestionUpdateNotification()

        if (newQuestion.audio == true && newQuestion.audioFile != null) {
            discordService.playAudio(newQuestion.audioFile!!)
        }

        return ChangeQuestionEntity.OK
    }

    fun nextQuestion(username: String): ChangeQuestionEntity {
        return setCurrentQuestion(username, currentQuestion + 1)
    }

    fun answerQuestion(questionId: Int, username: String, answer: String): AnswerErrorEntity {
        if (currentQuestion == -1)
            return AnswerErrorEntity.NO_QUEST
        if (questionId != currentQuestion)
            return AnswerErrorEntity.WRONG_QUEST

        val error = answerRepository.addAnswer(questionId, username, answer)
        return when (error) {
            AnswerErrorModel.OK -> AnswerErrorEntity.OK
            AnswerErrorModel.NO_QUESTION -> AnswerErrorEntity.NO_QUEST
            AnswerErrorModel.ALREADY_ANSWERED -> AnswerErrorEntity.ALREADY_ANS
        }
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

    fun getQuestionAnswers(questionId: Int): Map<String, String>? {
        if (questionRepository.getQuestion(questionId) == null)
            return null
        return answerRepository.getQuestionAnswer(questionId)
    }
}
