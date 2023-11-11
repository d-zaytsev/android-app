package com.musiclib.notes

import com.musiclib.Alteration

class Note(
    override val name: NoteName,
    override val octave: Int = 0,
    override val sign: Alteration = Alteration.None,
) : MusicNote, Comparable<Note> {
    /** Сравнивает ноты по высоте */
    override fun compareTo(other: Note): Int =
        (octave * 7 + sign.value + name.value).compareTo(other.octave * 7 + other.sign.value + other.name.value)

    override fun equals(other: Any?): Boolean {
        return if (other is Note)
            compareTo(other) == 0
        else
            false
    }

    override fun hashCode(): Int {
        return this.toString().hashCode()
    }


    override fun next(): Note {
        return if (name == NoteName.Si)
            Note(NoteName.Do, octave + 1, sign)
        else
            Note(NoteName.values()[name.ordinal + 1], octave, sign)
    }

    override fun previous(): Note {
        return if (name == NoteName.Do)
            Note(NoteName.Si, octave - 1, sign)
        else
            Note(NoteName.values()[name.ordinal - 1], octave, sign)
    }

    override fun isWhole(): Boolean = sign == Alteration.NaturalSign || sign == Alteration.None

    override fun toWhole() = Note(name, octave, Alteration.None)
    override fun toExt() = Note(name, octave, Alteration.SharpSign)

    override fun loLow() = Note(name, octave, Alteration.FlatSign)

    override fun toString(): String = "$name $octave $sign"
}