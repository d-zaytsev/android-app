package com.app.music_app.note_player

import android.content.Context
import android.media.MediaPlayer
import com.app.music_app.note_player.interfaces.AbstractInstrument
import com.app.music_app.note_player.interfaces.MelodyPlayer
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.data.NoteDuration
import com.musiclib.notes.data.NoteVolume
import com.musiclib.notes.Pause
import com.musiclib.notes.interfaces.Melody
import kotlinx.coroutines.delay

class MelodyPlayer(override val instrument: AbstractInstrument) :
    MelodyPlayer {
    override fun soundOf(context: Context, note: Note): MediaPlayer {
        val mp = MediaPlayer.create(context, instrument.resourceIdByNote(context, note))
        mp.setVolume(1f, 1f)
        return mp
    }

    /** Получить MediaPlayer с учётом громкости ноты */
    private fun melodyNote(context: Context, note: MelodyNote): MediaPlayer {
        // MediaPlayer не имеет настроек длительности исполнения файлов => мы можем поменять только громкость
        val mp = soundOf(context, note.toBasicNote())
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
        for (note in melody.notes) {
            when (note) {
                is MelodyNote -> {
                    val mp = melodyNote(context, note)
                    mp.start()
                    // После истечения *длительности ноты* мы прекращаем проигрывание файла
                    delay(delayTime(note.duration, melody.temp).toLong())
                    mp.release()
                }

                // пауза на время музыкальной паузы
                is Pause -> delay(delayTime(note.duration, melody.temp).toLong())

                else -> throw IllegalArgumentException("Can't recognize element $note in melody list")
            }
        }
    }

    private fun delayTime(duration: NoteDuration, temp: Int) = duration.value * temp

}