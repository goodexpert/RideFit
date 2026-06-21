package org.goodexpert.ridefit.repository

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayer(private val context: Context) {

    private var player: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun playAsset(fileName: String, onComplete: () -> Unit = {}) {
        release()
        player = MediaPlayer().apply {
            context.assets.openFd(fileName).use { afd ->
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            }
            prepare()
            setOnCompletionListener {
                _isPlaying.value = false
                release()
                player = null
                onComplete()
            }
            setOnErrorListener { _, _, _ ->
                _isPlaying.value = false
                release()
                player = null
                true
            }
            start()
            _isPlaying.value = true
        }
    }

    fun play(@RawRes resId: Int) {
        release()
        player = MediaPlayer.create(context, resId)?.apply {
            setOnCompletionListener {
                _isPlaying.value = false
                release()
                player = null
            }
            setOnErrorListener { _, _, _ ->
                _isPlaying.value = false
                release()
                player = null
                true
            }
            start()
            _isPlaying.value = true
        }
    }

    fun stop() {
        player?.let { if (it.isPlaying) it.stop() }
        _isPlaying.value = false
    }

    fun release() {
        player?.release()
        player = null
        _isPlaying.value = false
    }
}
