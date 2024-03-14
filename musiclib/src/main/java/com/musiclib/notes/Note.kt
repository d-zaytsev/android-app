package com.musiclib.notes

import com.musiclib.notes.interfaces.BasicNote
import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.note_metadata.NoteName

/**
 * Представляет собой абстрактную ноту определённой высоты
 * */
class Note(
    override val name: NoteName,
    override val octave: Int = 0,
    override val sign: Alteration = Alteration.None,
) : BasicNote, Comparable<Note> {

    init {
        require(name != NoteName.Mi || sign != Alteration.SharpSign) { "Can't create note (Mi-SharpSign)" }
        require(name != NoteName.Fa || sign != Alteration.FlatSign) { "Can't create note (Mi-FlatSign)" }
        require(name != NoteName.Si || sign != Alteration.SharpSign) { "Can't create note (Si-SharpSign)" }
        require(name != NoteName.Do || sign != Alteration.FlatSign) { "Can't create note (Do-FlatSign)" }
    }

    /** @return Следующую целую ноту относительно этой */
    fun nextWhole(): Note {
        return if (name == NoteName.Si)
            Note(NoteName.Do, octave + 1, sign)
        else
            Note(NoteName.entries[name.ordinal + 1], octave, sign)
    }

    /** @return Предыдущую целую ноту относительно этой */

    fun previousWhole(): Note {
        return if (name == NoteName.Do)
            Note(NoteName.Si, octave - 1, sign)
        else
            Note(NoteName.entries[name.ordinal - 1], octave, sign)
    }

    /** @return Нота, повышенная на полтона */
    fun nextSemitone(): Note {
        return if (sign == Alteration.FlatSign)
            Note(name, octave, Alteration.None)
        else if (sign == Alteration.SharpSign)
            Note(nextWhole().name, octave, Alteration.None)
        else if (name == NoteName.Si)
            Note(NoteName.Do, octave + 1, sign)
        else if (name == NoteName.Mi)
            Note(NoteName.Fa, octave, Alteration.None)
        else
            Note(name, octave, Alteration.SharpSign)

    }

    /** @return Нота, пониженная на полтона */
    fun previousSemitone(): Note {
        return if (sign == Alteration.SharpSign)
            Note(name, octave, Alteration.None)
        else if (sign == Alteration.FlatSign)
            Note(previousWhole().name, octave, Alteration.None)
        else if (name == NoteName.Do)
            Note(NoteName.Si, octave - 1, sign)
        else if (name == NoteName.Fa)
            Note(NoteName.Mi, octave, Alteration.None)
        else
            Note(name, octave, Alteration.FlatSign)
    }

    fun isWhole(): Boolean = sign == Alteration.None
    fun isExt(): Boolean = sign == Alteration.FlatSign
    fun isLow(): Boolean = sign == Alteration.FlatSign

    val pitch = octave * 6 + sign.value + name.value

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