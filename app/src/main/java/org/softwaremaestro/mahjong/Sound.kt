package org.softwaremaestro.mahjong

import android.content.Context
import android.media.SoundPool

class Sound {
    private var soundPool: SoundPool? = null
    private var cardSound: Int? = null

    fun init() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
    }

    fun load(context: Context) {
        cardSound = soundPool?.load(context, R.raw.card_sound, 1)
    }

    fun play() {
        soundPool?.play(cardSound!!, 1f, 1f, 1, 0, 1f);
    }

    fun release() {
        soundPool?.release()
    }
}