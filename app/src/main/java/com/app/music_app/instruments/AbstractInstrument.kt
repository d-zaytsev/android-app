package com.app.music_app.instruments

import android.content.Context
import android.media.MediaPlayer
import com.musiclib.notes.Note
import com.musiclib.notes.NoteRange

/**
 * Представляет собой класс, связывающий ноты (класс Note) и их физическое представление на устройстве (файл)
 */
abstract class AbstractInstrument {
    /**
     * @param note Нота, для которой нужно извлечь звук
     * @return Путь к файлу с нужным звуком
     * @throws IllegalArgumentException
     * */
    abstract fun soundPath(note: Note): String
    protected fun rangeCheck(note: Note) = noteRange.inRange(note)

    /**
     *  @return индекс ресурса, связанного с нотой
     *  */
    private fun resourceIdByNote(context: Context, note: Note): Int {
        val res = context.resources.getIdentifier(soundPath(note), "raw", "com.example.android_app")

        if (res == 0)
            throw IllegalArgumentException("Can't find resource for note \"$note\"")
        return res
    }

    /**
     * @return Звук переданной ноты
     * */
    fun soundOf(context: Context, note: Note): MediaPlayer =
        MediaPlayer.create(context, resourceIdByNote(context, note))

    abstract val noteRange: NoteRange;

}