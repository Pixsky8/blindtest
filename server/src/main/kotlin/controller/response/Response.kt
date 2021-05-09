package controller.response

open class Response {
    enum class Result {
        SUCCESS,
        FAILURE,
    }

    enum class ErrorCodes {
        LOGIN_FAIL,
        NOT_LOGGED,
        PERM_DENIED,
    }

    val status: Result;
    val errorCode: String?;
    val errorMessage: String?;

    constructor() {
        status = Result.SUCCESS;
        errorCode = null;
        errorMessage = null;
    }

    constructor(successStatus: Result, errorCode: ErrorCodes, errorMessage: String) {
        this.status = successStatus
        this.errorCode = errorCode.toString()
        this.errorMessage = errorMessage
    }

    constructor(successStatus: Result, errorCode: String, errorMessage: String) {
        this.status = successStatus
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }
}
