package org.goodexpert.ridefit.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

private const val LOG_TEXT_PREVIEW_LENGTH = 15

class TtsPlayer(context: Context) {

    private val tts: TextToSpeech
    private var isReady = false
    private var pendingSpeech: Triple<String, String, Float>? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.KOREAN)
                isReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
                pendingSpeech?.let { (text, id, rate) ->
                    speakNow(text, id, rate)
                    pendingSpeech = null
                }
            }
        }
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) { _isSpeaking.value = true }
            override fun onDone(utteranceId: String?) { _isSpeaking.value = false }

            // Required abstract override; the two-arg form (added in API 21) is the primary handler
            @Suppress("OVERRIDE_DEPRECATION")
            override fun onError(utteranceId: String?) { _isSpeaking.value = false }
            override fun onError(utteranceId: String?, errorCode: Int) { _isSpeaking.value = false }
        })
    }

    fun speak(
        text: String,
        utteranceId: String = "tts_${System.currentTimeMillis()}",
        speechRate: Float = 1.0f,
    ) {
        Log.d("RideFit", "TTS.speak: isReady=$isReady text=${text.take(LOG_TEXT_PREVIEW_LENGTH)}")
        if (!isReady) {
            pendingSpeech = Triple(text, utteranceId, speechRate)
            return
        }
        speakNow(text, utteranceId, speechRate)
    }

    private fun speakNow(text: String, utteranceId: String, speechRate: Float) {
        tts.setSpeechRate(speechRate)
        val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        Log.d("RideFit", "TTS.speakNow: result=$result (0=SUCCESS, -1=ERROR)")
    }

    fun speakQueued(texts: List<String>, pauseMs: Long = 350L) {
        Log.d("RideFit", "TTS.speakQueued: ${texts.size} parts, pauseMs=$pauseMs")
        if (!isReady) {
            pendingSpeech = Triple(texts.joinToString(" "), "tts_queued", 1.0f)
            return
        }
        val base = "tts_${System.currentTimeMillis()}"
        tts.setSpeechRate(1.0f)
        texts.forEachIndexed { i, text ->
            val mode = if (i == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
            tts.speak(text, mode, null, "${base}_$i")
            if (i < texts.lastIndex) {
                tts.playSilentUtterance(pauseMs, TextToSpeech.QUEUE_ADD, "${base}_s$i")
            }
        }
    }

    fun stop() {
        tts.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts.shutdown()
    }
}
