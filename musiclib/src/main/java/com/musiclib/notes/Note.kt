package com.musiclib.notes

import com.musiclib.notes.data.Alteration
import com.musiclib.notes.data.NoteName
import com.musiclib.notes.interfaces.BasicNote
import kotlin.math.abs

/**
 * Представляет собой абстрактную ноту определённой высоты
 * */
class Note(
    override val name: NoteName,
    override val octave: Int = 0,
    override val sign: Alteration = Alteration.None,
) : BasicNote, Comparable<Note> {

    /** @return Следующую ноту относительно этой */
    fun next(): Note {
        return if (name == NoteName.Si)
            Note(NoteName.Do, octave + 1, sign)
        else
            Note(NoteName.entries[name.ordinal + 1], octave, sign)
    }

    /** @return Предыдущую ноту относительно этой */

    fun previous(): Note {
        return if (name == NoteName.Do)
            Note(NoteName.Si, octave - 1, sign)
        else
            Note(NoteName.entries[name.ordinal - 1], octave, sign)
    }

    fun isWhole(): Boolean = sign == Alteration.None
    fun isExt(): Boolean = sign == Alteration.FlatSign
    fun isLow(): Boolean = sign == Alteration.FlatSign

    fun add(value: Float) =
        (this.pitch + value).toNote() ?: throw IllegalArgumentException("Can't add $value to note")

    val pitch = octave * 6 + sign.value + name.value
    private fun Float.toNote(): Note? {
        if (this == 0f)
            return Note(NoteName.Do)

        val value = (if (this > 0) this else abs(this) - 1)
        val octave = (value / 6).toInt()

        val names = NoteName.entries
        val signs = Alteration.entries.filterNot { alt -> alt == Alteration.NaturalSign }

        // Перебираем все возможные варианты и пытаемся найти подходящий
        for (noteName in names) {
            for (noteSign in signs) {
                if (noteSign.value + noteName.value + octave * 6f == value) {
                    if (noteName == NoteName.Mi && noteSign == Alteration.SharpSign)
                        continue

                    // В зависимости от знака возвращаем разное
                    return if (this > 0)
                        Note(noteName, octave, noteSign)
                    else
                        Note(
                            NoteName.entries[NoteName.entries.size - noteName.ordinal - 1],
                            -1 * octave - 1,
                            noteSign
                        )
                }
            }
        }

        return null
    }

    /** Сравнивает ноты по высоте */
    override fun compareTo(other: Note): Int =
        pitch.compareTo(other.pitch)

    override fun equals(other: Any?): Boolean {
        return if (other is Note)
            compareTo(other) == 0
        else
            false
    }

    override fun hashCode(): Int {
        return this.pitch.hashCode()
    }

    override fun toString(): String = "$name $octave $sign"
}