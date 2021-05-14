package service.discord

import discord4j.core.DiscordClientBuilder
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.entity.channel.MessageChannel
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.voice.VoiceConnection
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.io.File
import java.lang.IllegalStateException


class DiscordService {
    private val client: GatewayDiscordClient?
    private val musicService: MusicService
    private var cmdPrefix: String = "++"
    private var adminChannel: MessageChannel? = null

    private val logger: Logger = LoggerFactory.getLogger(DiscordService::class.java)

    constructor(token: String) {
        client = DiscordClientBuilder.create(token)
            .build()
            .login()
            .block()

        if (client == null)
            throw IllegalStateException("Could not initialize discord client.")

        musicService = MusicService()

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

    private fun commandDispatch(msg: Message): Publisher<out Any>? {
        val msgString = msg.content

        logger.info(msg.author.get().username + ": " + msgString)

        return when (msgString.split(" ")[0].substring(cmdPrefix.length)) {
            "ping" -> pingCommand(msg)
            "set_adm_chan" -> setAdmChanCommand(msg)
            "join" -> joinAudioCommand(msg)
            "play" -> playAudioCommand(msg)
            "stop" -> stopAudioCommand(msg)
            else -> unknownCommand(msg)
        }
    }

    private fun pingCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        return channel.createMessage("Pong!")
    }

    private fun setAdmChanCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        adminChannel = channel
        return channel.createMessage("Admin Channel Set.")
    }

    private fun joinAudioCommand(msg: Message): Mono<VoiceConnection>? {
        val member = msg.authorAsMember.block() ?: return null
        val voiceState = member.voiceState.block() ?: return null
        val audioChannel = voiceState.channel.block() ?: return null
        return audioChannel.join { spec ->
            spec.setProvider(musicService.provider)
        }
    }

    private fun playAudioCommand(msg: Message): Mono<Message>? {
        val arg = msg.content.split(" ")
        musicService.play(arg[1], false)
        val channel = msg.channel.block() ?: return null
        return channel.createMessage("Now playing: " + arg[1])
    }

    private fun stopAudioCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        musicService.stop()
        return channel.createMessage("Stopped playing.")
    }

    private fun unknownCommand(msg: Message): Mono<Message>? {
        val channel = msg.channel.block() ?: return null
        return channel.createMessage("Unknown Command: " + msg.content)
    }

    fun playAudio(filePath: String): Boolean {
        val file = File(filePath)
        if (!file.isFile) {
            logger.error("Could not find file `$filePath'.")
            return false
        }

        musicService.play(filePath, true)
        return true
    }
}
