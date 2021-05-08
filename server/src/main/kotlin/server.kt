import controller.AccountController
import controller.request.AccountCreationRequest
import controller.request.LoginRequest
import controller.request.ProfileRequest
import controller.response.Response
import tools.cookie.LoginSession
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import kotlinx.html.*
import org.h2.jdbcx.JdbcDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import repository.AccountRepository
import service.AccountService
import java.sql.Connection
import kotlin.random.Random


fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
    }
}

fun setupDatabaseConnection(): Connection? {
    val dataSource = JdbcDataSource()
    dataSource.setURL("jdbc:h2:./accounts")
    dataSource.user = "h2"
    dataSource.password = ""
    return dataSource.connection
}

fun main() {
    val connection = setupDatabaseConnection();
    if (connection == null) {
        println("Could not connect to database")
        return
    }

    val dataBase: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    val accountRepository = AccountRepository(dataBase)
    val accountService = AccountService(accountRepository)
    val accountController = AccountController(accountService)

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            jackson()
        }
        install(Sessions) {
            cookie<LoginSession>("LOGIN_SESSION", storage = SessionStorageMemory()) {
                val secretSignKey = ByteArray(64);
                Random.Default.nextBytes(secretSignKey, 0, secretSignKey.size)
                transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
            }
        }
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            // DEBUG
            get("/account/list") {
                call.respond(accountController.listAccounts())
            }

            post("/account") {
                val request = call.receive<AccountCreationRequest>()
                call.respond(accountController.createAccount(request))
            }

            post("/login") {
                val request = call.receive<LoginRequest>()
                val response = accountController.login(request)
                if (response.status == Response.Result.SUCCESS) {
                    val loginSession = call.sessions.get<LoginSession>() ?: LoginSession(username = request.login)
                    call.sessions.set(loginSession)
                }
                call.respond(response)
            }
            delete("/login") {
                call.sessions.clear<LoginSession>()
                call.respond(accountController.logout())
            }

            get("/profile") {
                val session = call.sessions.get<LoginSession>()
                if (session == null)
                    call.respond(accountController.getProfile(ProfileRequest(null)))
                else
                    call.respond(accountController.getProfile(ProfileRequest(session.username)))
            }
        }
    }.start(wait = true)
}
