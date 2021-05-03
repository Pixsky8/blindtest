package controller

import controller.request.AccountCreationRequest
import controller.request.LoginRequest
import controller.request.ProfileRequest
import controller.response.AccountCreationResponse
import controller.response.AccountListResponse
import controller.response.ProfileResponse
import controller.response.Response
import io.ktor.http.content.*
import service.AccountService
import service.entity.AccountEntity

class AccountController {
    val accountService: AccountService

    constructor(accountService: AccountService) {
        this.accountService = accountService
    }

    fun createAccount(request: AccountCreationRequest): AccountCreationResponse {
        val result: Boolean = accountService.createAccount(AccountEntity(request.login, request.password))
        return AccountCreationResponse()
    }

    // DEBUG ONLY
    fun listAccounts(): AccountListResponse {
        val tmp = accountService.listAccounts()
        val res = ArrayList<AccountListResponse.Account>()
        for (account in tmp) {
            res.add(AccountListResponse.Account(account.login, account.password))
        }

        return AccountListResponse(res)
    }

    fun login(request: LoginRequest): Response /*PLACE HOLDER*/ {
        if (accountService.samePassword(AccountEntity(request.login, request.password)))
            return Response();

        return Response(Response.Result.FAILURE, "LOGIN_FAILED", "Login failed")
    }

    fun getProfile(request: ProfileRequest): ProfileResponse {
        if (request.user == null)
            return ProfileResponse(Response.Result.FAILURE, "NOT_LOGGED", "Not logged in")
        return ProfileResponse(request.user)
    }
}
