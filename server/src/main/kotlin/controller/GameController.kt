package controller

import controller.request.SetQuestionIdRequest
import controller.response.QuestionResponse
import controller.response.Response
import service.QuestionService
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
                Response.ErrorCodes.PERM_DENIED,
                "Cannot change question without admin permissions."
            )

        questionService.resetCurrentQuestion(session.username, request.id)
        return Response()
    }

    fun nextQuestion(session: LoginSession?): Response {
        if (session == null)
            return Response(
                Response.Result.FAILURE,
                Response.ErrorCodes.PERM_DENIED,
                "Cannot change question without admin permissions."
            )

        questionService.nextQuestion(session.username)
        return Response()
    }
}
