package repository

import database.Tables
import org.jooq.DSLContext
import repository.model.AccountModel

class AccountRepository {
    private val dataBase: DSLContext

    constructor(dataBase: DSLContext) {
        this.dataBase = dataBase
    }

    fun createAccount(account: AccountModel) {
        dataBase.insertInto(Tables.ACCOUNT, Tables.ACCOUNT.LOGIN, Tables.ACCOUNT.PASSWORD)
            .values(account.login, account.password.toString())
            .execute()
    }

    fun listAccounts(): ArrayList<AccountModel> {
        val res = ArrayList<AccountModel>()
        dataBase.select(Tables.ACCOUNT.LOGIN, Tables.ACCOUNT.PASSWORD)
            .from(Tables.ACCOUNT)
            .forEach{elem ->
                res.add(
                    AccountModel(
                        elem.get(Tables.ACCOUNT.LOGIN),
                        elem.get(Tables.ACCOUNT.PASSWORD)
                    )
                )
            }
        return res;
    }

    fun getAccount(accountLogin: String): AccountModel? {
        val res = ArrayList<AccountModel>()
        dataBase.select(Tables.ACCOUNT.LOGIN, Tables.ACCOUNT.PASSWORD)
            .from(Tables.ACCOUNT)
            .where(Tables.ACCOUNT.LOGIN.eq(accountLogin))
            .forEach{elem ->
                res.add(
                    AccountModel(
                        elem.get(Tables.ACCOUNT.LOGIN),
                        elem.get(Tables.ACCOUNT.PASSWORD)
                    )
                )
            }
        if (res.size == 0)
            return null
        return res[0]
    }
}
