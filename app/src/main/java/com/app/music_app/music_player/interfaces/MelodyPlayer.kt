package com.app.music_app.music_player.interfaces

import android.content.Context
import android.media.MediaPlayer
import com.musiclib.notes.Note
import com.musiclib.notes.interfaces.Melody

/**
 * Обёртка для MediaPlayer, необходима для проигрывания определённых нот
 * */
interface MelodyPlayer {


    /** Инструмент, из которого извлекается звук */
    val instrument: AbstractInstrument

    /** @return звук переданной ноты */
    fun soundOf(context: Context, note: Note): MediaPlayer

    /** Проигрывает переданную композицию */
    suspend fun play(context: Context, melody: Melody)
}