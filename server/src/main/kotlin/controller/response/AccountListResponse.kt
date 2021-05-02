package controller.response

class AccountListResponse {
    class Account {
        val login: String
        val password: String

        constructor(login: String, password: String) {
            this.login = login
            this.password = password
        }
    }

    val accountList: ArrayList<Account>

    constructor(accountList: ArrayList<Account>) {
        this.accountList = accountList
    }
}
