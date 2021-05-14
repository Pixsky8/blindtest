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
import io.ktor.http.cio.websocket.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.html.*
import org.h2.jdbcx.JdbcDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.slf4j.LoggerFactory
import repository.AccountRepository
import repository.AdminRepository
import repository.QuestionsRepository
import service.AccountService
import service.GameService
import service.QuestionService
import service.discord.DiscordService
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

fun igniteServer(discordToken: String): NettyApplicationEngine? {
    val logger = LoggerFactory.getLogger("Ktor Server")

    val connection = setupDatabaseConnection();
    if (connection == null) {
        println("Could not connect to database")
        return null
    }

    val gameConnections = HashSet<DefaultWebSocketSession>()

    val dataBase: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    val accountRepository = AccountRepository(dataBase)
    val adminRepository = AdminRepository(dataBase)
    val questionRepository = QuestionsRepository("config/questions.json")

    val accountService = AccountService(accountRepository, adminRepository)
    val gameService = GameService(gameConnections)
    val discordService = DiscordService(discordToken)
    val questionService = QuestionService(questionRepository, accountService, discordService, gameService)

    val accountController = AccountController(accountService)
    val gameController = GameController(questionService)

    return embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
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
        install(WebSockets)
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }

            // Account Routes
            /*
            // DEBUG
            get("/account/list") {
                call.respond(accountController.listAccounts())
            }
            */

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

            // Game Setup Routes
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

            webSocket("/ws/question") {
                outgoing.send(Frame.Text("CONNECTED"))
                logger.info("New websocket")
                gameConnections.add(this)
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val text = frame.readText()
                                outgoing.send(Frame.Text("YOU SAID: $text"))
                            }
                        }
                    }
                }
                finally {
                    logger.info(this.toString() + " left.")
                    gameConnections.remove(this)
                }
            }
        }
    }.start(wait = false)
}
