package repository.model

class AccountModel {
    val login: String
    val password: String

    constructor(login: String, password: String) {
        this.login = login
        this.password = password
    }
}
