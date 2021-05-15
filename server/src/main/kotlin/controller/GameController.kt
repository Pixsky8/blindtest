package controller

import controller.request.AnswerRequest
import controller.request.SetQuestionIdRequest
import controller.response.AnswersResponse
import controller.response.QuestionResponse
import controller.response.Response
import service.AccountService
import service.GameService
import service.QuestionService
import service.entity.AnswerErrorEntity
import service.entity.ChangeQuestionEntity
import tools.cookie.LoginSession

class GameController {
    val accountService: AccountService
    val gameService: GameService
    val questionService: QuestionService

    constructor(accountService: AccountService,
                gameService: GameService,
                questionService: QuestionService) {
        this.accountService = accountService
        this.gameService = gameService
        this.questionService = questionService
    }

    fun getCurentQuestion(): QuestionResponse {
        val question = questionService.getCurrentQuestion()

        if (question == null)
            return QuestionResponse(
                Response.Result.FAILURE,
                "NO_QUESTION",
                "There is currently no question."
            )

        return QuestionResponse(
            question.id,
            question.theme,
            question.audio,
            question.question
        )
    }

    fun setQuestion(session: LoginSession?, request: SetQuestionIdRequest): Response {
        if (session == null)
            return Response(
                Response.Result.FAILURE,
                Response.ErrorCodes.NOT_LOGGED,
                "You must login."
            )

        val res: ChangeQuestionEntity = questionService.resetCurrentQuestion(session.username, request.id)

        if (res == ChangeQuestionEntity.OK)
            return Response()
        return Response(
            Response.Result.FAILURE,
            res.toString(),
            res.message
        )
    }

    fun nextQuestion(session: LoginSession?): Response {
        if (session == null)
            return Response(
                Response.Result.FAILURE,
                Response.ErrorCodes.PERM_DENIED,
                "Cannot change question without admin permissions."
            )

        val res: ChangeQuestionEntity = questionService.nextQuestion(session.username)
        if (res == ChangeQuestionEntity.OK)
            return Response()
        return Response(
            Response.Result.FAILURE,
            res.toString(),
            res.message
        )
    }

    fun answerQuestion(session: LoginSession?, request: AnswerRequest): Response {
        if (session == null)
            return Response(
                Response.Result.FAILURE,
                Response.ErrorCodes.NOT_LOGGED,
                "You must be logged in."
            )

        val res = questionService.answerQuestion(request.questionId, session.username, request.anwser)
        if (res == AnswerErrorEntity.OK)
            return Response()
        return Response(Response.Result.FAILURE, res.toString(), res.message)
    }

    fun getAnswers(session: LoginSession?): AnswersResponse {
        if (session == null || !accountService.isAdminAccount(session.username))
            return AnswersResponse(
                Response.Result.FAILURE,
                Response.ErrorCodes.PERM_DENIED,
                "You must be an administrator to do that."
            )

        val answers = questionService.getQuestionAnswers(questionService.currentQuestion)
        if (answers == null)
            return AnswersResponse(
                Response.Result.FAILURE,
                "NO_QUEST",
                "There are no answers for question ${questionService.currentQuestion}."
            )

        return AnswersResponse(questionService.currentQuestion, answers)
    }
}
