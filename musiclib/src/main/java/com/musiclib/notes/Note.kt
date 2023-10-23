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
    override fun compareTo(other: Note): Int {
        if (other.octave > octave)
            return -1
        else if (other.octave < octave)
            return 1
        else {
            if (other.name > name)
                return -1
            else if (other.name < name)
                return 1
            else {
                if (other.sign.value > sign.value)
                    return -1
                else if (other.sign.value < sign.value)
                    return 1
                return 0
            }
        }

    }

    override fun nextNote(): Note {
        return if (name == NoteName.Si)
            Note(NoteName.Do, octave + 1, volume, sign, duration)
        else
            Note(NoteName.values()[name.ordinal + 1], octave, volume, sign, duration)
    }

    override fun prevNote(): Note {
        return if (name == NoteName.Do)
            Note(NoteName.Si, octave - 1, volume, sign, duration)
        else
            Note(NoteName.values()[name.ordinal - 1], octave, volume, sign, duration)
    }

    override fun toString(): String = "$name $octave $volume $sign $duration"
}