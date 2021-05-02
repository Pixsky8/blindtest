package controller.response

class AccountCreationResponse : Response {
    constructor() : super() {}

    constructor(errorCode: String, errorMessage: String) :
            super(Result.FAILURE, errorCode, errorMessage) {}
}
