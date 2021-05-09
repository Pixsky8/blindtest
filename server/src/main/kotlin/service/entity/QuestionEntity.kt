package service.entity

class QuestionEntity {
    var id: Int
    var theme: String
    var audio: Boolean
    var audioFile: String?
    var question: String?

    constructor(id: Int, theme: String, audio: Boolean, audioFile: String?, question: String?) {
        this.id = id
        this.theme = theme
        this.audio = audio
        this.audioFile = audioFile
        this.question = question
    }
}
