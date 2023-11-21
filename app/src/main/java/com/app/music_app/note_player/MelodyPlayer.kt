package com.app.music_app.note_player

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import com.app.music_app.note_player.interfaces.AbstractInstrument
import com.app.music_app.note_player.interfaces.MelodyPlayer
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.NoteDuration
import com.musiclib.notes.NoteVolume
import com.musiclib.notes.Pause
import com.musiclib.notes.interfaces.Melody
import com.musiclib.notes.interfaces.MusicPause
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MelodyPlayer(override val instrument: AbstractInstrument) :
    MelodyPlayer {
    override fun soundOf(context: Context, note: Note): MediaPlayer {
        val mp = MediaPlayer.create(context, instrument.resourceIdByNote(context, note))
        mp.setVolume(1f, 1f)
        return mp
    }

    private fun melodyNote(context: Context, note: MelodyNote): MediaPlayer {
        val mp =
            MediaPlayer.create(context, instrument.resourceIdByNote(context, note.toSimpleNote()))
        // Громкость звучания
        val volume = mapOf(
            NoteVolume.Fortissimo to 1f,
            NoteVolume.Forte to 0.8f,
            NoteVolume.Piano to 0.5f,
            NoteVolume.Pianissimo to 0.4f
        )[note.volume]
            ?: throw NullPointerException("MelodyPlayer can't find settings for such NoteVolume")
        mp.setVolume(volume, volume)

        return mp
    }

    override suspend fun play(context: Context, melody: Melody) {
        //TODO поддержка разного темпа
        for (note in melody.notes) {
            when (note) {
                is MelodyNote -> {
                    val mp = melodyNote(context, note)
                    mp.start()
                    delay(delayTime(note.duration, melody.temp).toLong())
                    mp.release()
                }

                // приостанавливаем на время паузы
                is Pause -> delay(delayTime(note.duration, melody.temp).toLong())

                else -> throw IllegalArgumentException("Can't recognize element $note in melody list")
            }
        }
    }

    private fun delayTime(duration: NoteDuration, temp: Int) = duration.value * temp

}