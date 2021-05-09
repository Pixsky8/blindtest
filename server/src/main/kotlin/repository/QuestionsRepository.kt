package repository

import com.fasterxml.jackson.databind.ObjectMapper
import repository.model.QuestionListModel
import repository.model.QuestionModel
import java.io.File
import java.lang.IllegalArgumentException

class QuestionsRepository {
    var questionsList: QuestionListModel

    constructor(pathToQuestions: String) {
        val objectMapper = ObjectMapper()
        val questionsFile = File(pathToQuestions)
        questionsList = objectMapper.readValue(questionsFile, QuestionListModel::class.java)
        for (question in questionsList.questions) {
            if (question.id == null)
                throw IllegalArgumentException("Json file is not valid, id is missing for a question.")
            if (question.audio == null || question.theme == null)
                throw IllegalArgumentException(
                    "Json file is not valid, audio and/or theme filed are missing from question "
                            + question.id
                )
        }
    }

    fun setQuestionList(pathToQuestions: String) {
        val objectMapper = ObjectMapper()
        questionsList = objectMapper.readValue(pathToQuestions, QuestionListModel::class.java)
    }

    fun getQuestion(id: Int): QuestionModel? {
        var res = ArrayList<QuestionModel>()
        questionsList.questions
            .filter { question -> question.id == id }
            .forEach { question -> res.add(question) }
        if (res.size == 0)
            return null
        return res[0]
    }
}
