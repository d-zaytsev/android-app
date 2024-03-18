package com.app.music_app.note_player.interfaces

import android.content.Context
import com.musiclib.notes.Note
import com.musiclib.notes.range.NoteRange

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

    /**
     *  @return индекс ресурса, связанного с нотой
     *  @throws IllegalArgumentException
     *  */
    fun resourceIdByNote(context: Context, note: Note): Int {
        val res = context.resources.getIdentifier(soundPath(note), "raw", "com.example.android_app")

        if (res == 0)
            throw IllegalArgumentException("Can't find resource for note \"$note\"")
        return res
    }

    abstract val instrumentRange: NoteRange;

}