package com.app.music_app.note_player

import android.content.Context
import android.media.MediaPlayer
import com.app.music_app.note_player.instruments.AbstractInstrument
import com.musiclib.notes.Note
import com.musiclib.notes.SoundNote
import com.musiclib.notes.interfaces.MelodyNote

/**
 * Обёртка для MediaPlayer, необходима для проигрывания определённых нот
 * */
interface NotePlayer {

    val instrument: AbstractInstrument
    fun soundOf(context: Context, note: Note): MediaPlayer
    fun soundOf(context: Context, note: SoundNote): MediaPlayer
}