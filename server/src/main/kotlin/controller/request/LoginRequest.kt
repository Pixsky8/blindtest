package controller.request

class LoginRequest {
    val login: String
    val password: String

    constructor(login: String, password: String) {
        this.login = login
        this.password = password
    }
}
