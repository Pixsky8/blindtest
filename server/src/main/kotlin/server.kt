import controller.AccountController
import controller.GameController
import controller.request.AccountCreationRequest
import controller.request.LoginRequest
import controller.request.ProfileRequest
import controller.request.SetQuestionIdRequest
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
import org.jooq.Log
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import repository.AccountRepository
import repository.QuestionsRepository
import service.AccountService
import service.QuestionService
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

fun igniteServer(): NettyApplicationEngine? {
    val connection = setupDatabaseConnection();
    if (connection == null) {
        println("Could not connect to database")
        return null
    }

    val dataBase: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    val accountRepository = AccountRepository(dataBase)
    val accountService = AccountService(accountRepository)
    val accountController = AccountController(accountService)
    val questionRepository = QuestionsRepository("config/questions.json")
    val questionService = QuestionService(questionRepository)
    val gameController = GameController(questionService)

    return embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
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

            // Account Routes
            /* DEBUG
            get("/account/list") {
                call.respond(accountController.listAccounts())
            }*/

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
                val session = call.sessions.get<LoginSession>()
                if (session != null) {
                    call.sessions.clear<LoginSession>()
                    call.respond(accountController.logout(session.username))
                }
                else
                    call.respond(accountController.logout(null))
            }

            get("/profile") {
                val session = call.sessions.get<LoginSession>()
                if (session == null)
                    call.respond(accountController.getProfile(ProfileRequest(null)))
                else
                    call.respond(accountController.getProfile(ProfileRequest(session.username)))
            }

            // Game Routes
            get("/question") {
                call.respond(gameController.getCurentQuestion())
            }

            put("/question") {
                val session = call.sessions.get<LoginSession>()
                call.respond(gameController.nextQuestion(session))
            }

            patch("/question") {
                val session = call.sessions.get<LoginSession>()
                val request = call.receive<SetQuestionIdRequest>()
                call.respond(gameController.setQuestion(session, request))
            }
        }
    }.start(wait = false)
}
