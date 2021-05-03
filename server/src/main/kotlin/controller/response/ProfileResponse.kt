package controller.response

class ProfileResponse : Response{
    val login: String?

    constructor(login: String): super(){
        this.login = login
    }

    constructor(result: Result, errorCode: ErrorCodes, errorMessage: String)
            : super(result, errorCode, errorMessage) {
        this.login = null
    }
}
