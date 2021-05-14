package service

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import repository.QuestionsRepository
import service.discord.DiscordService
import service.entity.ChangeQuestionEntity
import service.entity.QuestionEntity

class QuestionService {
    val questionRepository: QuestionsRepository
    val accountService: AccountService
    val discordService: DiscordService
    val gameService: GameService
    var currentQuestion: Int = -1

    constructor(questionRepository: QuestionsRepository,
                accountService: AccountService,
                discordService: DiscordService,
                gameService: GameService) {
        this.questionRepository = questionRepository
        this.accountService = accountService
        this.discordService = discordService
        this.gameService = gameService
    }

    fun resetCurrentQuestion(username: String, questionId: Int): ChangeQuestionEntity {
        if (!accountService.isAdminAccount(username))
            return ChangeQuestionEntity.NO_PERM

        val newQuestion = questionRepository.getQuestion(questionId)
        if (newQuestion == null)
            return ChangeQuestionEntity.NO_QUEST

        currentQuestion = questionId

        gameService.sendUpdateNotification()
        if (newQuestion.audio == true && newQuestion.audioFile != null) {
            discordService.playAudio(newQuestion.audioFile!!)
        }

        return ChangeQuestionEntity.OK
    }

    fun nextQuestion(username: String): ChangeQuestionEntity {
        if (!accountService.isAdminAccount(username))
            return ChangeQuestionEntity.NO_PERM
        if (questionRepository.getQuestion(currentQuestion + 1) == null)
            return ChangeQuestionEntity.NO_QUEST
        currentQuestion++

        gameService.sendUpdateNotification()

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
