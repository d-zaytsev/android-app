package com.musiclib.notes

import com.musiclib.Alteration

class Note(
    override val name: NoteName,
    override val octave: Int = 0,
    override val volume: NoteVolume = NoteVolume.Piano,
    override val sign: Alteration = Alteration.None,
    override val duration: NoteDuration = NoteDuration.Whole
) : MusicNote, Comparable<Note> {
    /** Сравнивает ноты по высоте */
    override fun compareTo(other: Note): Int =
        (octave + sign.value + name.value).compareTo(other.octave + other.sign.value + other.name.value)

    override fun next(): Note {
        return if (name == NoteName.Si)
            Note(NoteName.Do, octave + 1, volume, sign, duration)
        else
            Note(NoteName.values()[name.ordinal + 1], octave, volume, sign, duration)
    }

    override fun previous(): Note {
        return if (name == NoteName.Do)
            Note(NoteName.Si, octave - 1, volume, sign, duration)
        else
            Note(NoteName.values()[name.ordinal - 1], octave, volume, sign, duration)
    }

    override fun toString(): String = "$name $octave $volume $sign $duration"
}