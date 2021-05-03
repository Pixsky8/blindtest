package controller.request

class ProfileRequest {
    val user_login: String?

    constructor(user_login: String?) {
        this.user_login = user_login
    }
}
