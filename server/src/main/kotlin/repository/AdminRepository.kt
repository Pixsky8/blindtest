package repository

import database.Tables
import org.jooq.DSLContext

class AdminRepository {
    private val dataBase: DSLContext

    constructor(dataBase: DSLContext) {
        this.dataBase = dataBase
    }

    fun isAdmin(username: String): Boolean {
        return dataBase.select(Tables.ADMIN.LOGIN)
            .from(Tables.ADMIN)
            .where(Tables.ADMIN.LOGIN.eq(username))
            .count() > 0
    }
}
