package service.discord

import audio.LavaPlayerAudioProvider
import audio.TrackScheduler
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer
import discord4j.voice.AudioProvider
import java.util.concurrent.atomic.AtomicBoolean


class MusicService {
    val player: AudioPlayer
    val playerManager: AudioPlayerManager
    val provider: AudioProvider
    val scheduler: TrackScheduler

    constructor() : super() {
        playerManager = DefaultAudioPlayerManager()
        playerManager.getConfiguration()
            .setFrameBufferFactory { bufferDuration: Int, format: AudioDataFormat?, stopping: AtomicBoolean? ->
                NonAllocatingAudioFrameBuffer(
                    bufferDuration,
                    format,
                    stopping
                )
            }

        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)

        player = playerManager.createPlayer()
        provider = LavaPlayerAudioProvider(player)
        scheduler = TrackScheduler(player)
    }

    fun play(music: String, force: Boolean) {
        if (force)
            stop()
        playerManager.loadItem(music, scheduler)
    }

    fun stop() {
        player.stopTrack()
    }
}

