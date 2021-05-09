package service.discord

import audio.LavaPlayerAudioProvider
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer
import discord4j.voice.AudioProvider
import java.util.concurrent.atomic.AtomicBoolean

class MusicService {
    val playerManager: AudioPlayerManager
    val audioPlayer: AudioPlayer
    val provider: AudioProvider

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

        audioPlayer = playerManager.createPlayer()
        provider = LavaPlayerAudioProvider(audioPlayer)
    }
}
