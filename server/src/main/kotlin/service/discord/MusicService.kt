package service.discord

import audio.LavaPlayerAudioProvider
import audio.TrackScheduler
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer
import discord4j.voice.AudioProvider
import java.util.concurrent.atomic.AtomicBoolean

class MusicService {
    val playerManager: AudioPlayerManager
    val provider: AudioProvider
    val scheduler: TrackScheduler

    constructor() {
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

        val audioPlayer = playerManager.createPlayer()
        provider = LavaPlayerAudioProvider(audioPlayer)
        scheduler = TrackScheduler(audioPlayer)
    }
}
