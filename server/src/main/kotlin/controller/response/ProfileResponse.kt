package controller.response

class ProfileResponse : Response{
    val login: String?
    val admin: Boolean?

    constructor(login: String, isAdmin: Boolean): super(){
        this.login = login
        this.admin = isAdmin
    }

    constructor(result: Result, errorCode: ErrorCodes, errorMessage: String)
            : super(result, errorCode, errorMessage) {
        this.login = null
        this.admin = null
    }
}
