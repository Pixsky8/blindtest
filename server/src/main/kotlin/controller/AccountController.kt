package controller

import controller.request.AccountCreationRequest
import controller.request.LoginRequest
import controller.request.ProfileRequest
import controller.response.AccountListResponse
import controller.response.ProfileResponse
import controller.response.Response
import org.slf4j.LoggerFactory
import service.AccountService
import service.entity.AccountEntity
import service.entity.AccountErrorEntity

class AccountController {
    val accountService: AccountService
    val logger = LoggerFactory.getLogger(AccountController::class.java)

    constructor(accountService: AccountService) {
        this.accountService = accountService
    }

    fun createAccount(request: AccountCreationRequest): Response {
        val result: AccountErrorEntity = accountService.createAccount(AccountEntity(request.login, request.password))
        if (result == AccountErrorEntity.NONE) {
            logger.info("New account created: " + request.login)
            return Response()
        }

        logger.info("Account creation failed: " + request.login + " err: " + result.toString())
        return Response(Response.Result.FAILURE, result.toString(), result.getMessage())
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
        if (accountService.samePassword(AccountEntity(request.login, request.password))) {
            logger.info(request.login + " logged in")
            return Response()
        }

        logger.info(request.login + ": log in failed")
        return Response(Response.Result.FAILURE, Response.ErrorCodes.LOGIN_FAIL, "Login failed")
    }

    fun logout(username: String?): Response {
        if (username == null)
            return Response(Response.Result.FAILURE, Response.ErrorCodes.NOT_LOGGED, "Not logged in")
        logger.info(username + " logged out")
        return Response()
    }

    fun getProfile(request: ProfileRequest): ProfileResponse {
        if (request.user_login == null)
            return ProfileResponse(Response.Result.FAILURE, Response.ErrorCodes.NOT_LOGGED, "Not logged in")
        val isAdmin: Boolean = accountService.isAdminAccount(request.user_login)
        return ProfileResponse(request.user_login, isAdmin)
    }
}
