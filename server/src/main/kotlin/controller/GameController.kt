package controller

import controller.request.SetQuestionIdRequest
import controller.response.QuestionResponse
import controller.response.Response
import service.QuestionService
import service.entity.ChangeQuestionEntity
import tools.cookie.LoginSession

class GameController {
    val questionService: QuestionService

    constructor(questionService: QuestionService) {
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
}
