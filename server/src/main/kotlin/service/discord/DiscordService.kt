package service.discord

import discord4j.core.DiscordClientBuilder
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.entity.channel.Channel
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono


class DiscordService {
    val client: GatewayDiscordClient?
    val cmdPrefix: String = "++"
    var adminChannel: Channel? = null

    var logger: Logger = LoggerFactory.getLogger(DiscordService::class.java)

    constructor(token: String) {
        client = DiscordClientBuilder.create(token)
            .build()
            .login()
            .block()

        client.getEventDispatcher().on(ReadyEvent::class.java)
            .subscribe { event: ReadyEvent ->
                val self: User = event.self
                logger.info(
                    java.lang.String.format(
                        "Logged in as %s#%s", self.getUsername(), self.getDiscriminator()
                    )
                )
            }

        client.getEventDispatcher().on(MessageCreateEvent::class.java)
            .map { obj: MessageCreateEvent -> obj.message }
            .filter { message: Message ->
                message.author.map { user: User -> !user.isBot }
                    .orElse(false)
            }
            .filter { message: Message ->
                message.content.startsWith(cmdPrefix)
            }
            .flatMap { message ->
                commandDispatch(message)
            }
            .subscribe()
    }

    fun commandDispatch(msg: Message): Publisher<out Any>? {
        val msgString = msg.content

        logger.info(msg.author.get().username + ": " + msgString)

        return when (msgString.substring(cmdPrefix.length)) {
            "ping" -> pingCommand(msg)
            else -> unknownCommand(msg)
        }
    }

    fun pingCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        return channel.createMessage("Pong!")
    }

    fun setAdmChanCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        adminChannel = channel
        return channel.createMessage("Admin Channel Set.")
    }

    fun unknownCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        return channel.createMessage("Unknown Command: " + msg.content)
    }
}
