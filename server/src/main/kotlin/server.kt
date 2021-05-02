import controller.AccountController
import controller.request.AccountCreationRequest
import controller.request.LoginRequest
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
import kotlinx.html.*
import org.h2.jdbcx.JdbcDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import repository.AccountRepository
import service.AccountService
import java.sql.Connection


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
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            // DEBUG
            get("/account") {
                call.respond(accountController.listAccounts())
            }

            post("/account") {
                val request = call.receive<AccountCreationRequest>()
                call.respond(accountController.createAccount(request))
            }

            post("/login") {
                val request = call.receive<LoginRequest>()
                call.respond(accountController.login(request))
            }
        }
    }.start(wait = true)
}
