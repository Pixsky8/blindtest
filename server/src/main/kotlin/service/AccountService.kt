package service

import repository.AccountRepository
import repository.model.AccountModel
import service.entity.AccountEntity
import service.entity.AccountErrorEntity
import tools.HashPassword
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class AccountService {
    private val accountRepository: AccountRepository

    constructor(accountRepository: AccountRepository) {
        this.accountRepository = accountRepository
    }

    fun createAccount(account: AccountEntity): AccountErrorEntity {
        if (account.login.length < 3)
            return AccountErrorEntity.CREAT_NAME_LEN

        if (getAccount(account.login) != null)
            return AccountErrorEntity.CREAT_EXIST

        val passwd_sha3 = HashPassword(account.password)

        if (passwd_sha3 == null) {
            return AccountErrorEntity.CREAT_PASSWD_ERR
        }

        accountRepository.createAccount(AccountModel(account.login, passwd_sha3))
        return AccountErrorEntity.NONE
    }

    fun listAccounts(): ArrayList<AccountEntity> {
        val accountModelList: ArrayList<AccountModel> = accountRepository.listAccounts()
        val res = ArrayList<AccountEntity>()

        for (account: AccountModel in accountModelList) {
            res.add(AccountEntity(account.login, account.password))
        }

        return res;
    }

    fun getAccount(accountLogin: String): AccountEntity? {
        val res = accountRepository.getAccount(accountLogin)

        if (res == null)
            return null
        return AccountEntity(res.login, res.password)
    }

    fun samePassword(account: AccountEntity): Boolean {
        val accountEntity = getAccount(account.login)
        if (accountEntity == null)
            return false

        val passwd_sha3 = HashPassword(account.password)
        if (passwd_sha3 == null) {
            return false
        }

        return account.login.equals(accountEntity.login)
                && passwd_sha3.equals(accountEntity.password)
    }
}
