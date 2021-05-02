package controller.response

open class Response {
    enum class Result {
        SUCCESS,
        FAILURE,
    }

    val successStatus: Result;
    val errorCode: String?;
    val errorMessage: String?;

    constructor() {
        successStatus = Result.SUCCESS;
        errorCode = null;
        errorMessage = null;
    }

    constructor(successStatus: Result, errorCode: String, errorMessage: String) {
        this.successStatus = successStatus
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }


}
