package service.entity

enum class AccountErrorEntity {
    CREAT_NAME_LEN("Account name must be at least 3 characters long."),
    CREAT_EXIST("Account name is already used."),
    CREAT_PASSWD_ERR("Try a different password"),
    NONE("");

    private val message: String

    constructor(message: String) {
        this.message = message
    }

    fun getMessage(): String {
        return this.message
    }
}
