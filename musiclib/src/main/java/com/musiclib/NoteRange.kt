package com.musiclib

import com.musiclib.intervals.Interval
import com.musiclib.notes.Note
import com.musiclib.notes.data.Alteration
import com.musiclib.notes.data.NoteName

/**
 * Представляет собой определённый диапазон нот
 */
data class NoteRange(val fromNote: Note, val endNote: Note) {
    constructor(octaveFrom: Int, endOctave: Int = octaveFrom) : this(
        Note(
            NoteName.Do,
            octave = octaveFrom
        ), Note(NoteName.Si, octave = endOctave)
    )

    init {
        if (fromNote > endNote)
            throw IllegalArgumentException("Left border cannot be greater than the right")
    }

    /** Список целых нот в этом диапазоне */
    val wholeNotes: List<Note>
        get() {
            val list = mutableListOf<Note>()

            var curNote = if (fromNote.sign == Alteration.SharpSign)
                fromNote.next()
            else
                fromNote

            val prevEnd = if (endNote.sign == Alteration.FlatSign)
                endNote.previous()
            else
                endNote

            while (curNote != prevEnd) {
                list.add(curNote)
                curNote = curNote.next()
            }

            return list
        }

    /** Количество целых нот в диапазоне */
    val wholeNotesSize: Int
        get() = (endNote.octave * 7 + endNote.name.ordinal) - (fromNote.octave * 7 + fromNote.name.ordinal - 1)

    fun inRangeAll(elements: Collection<Note>): Boolean {
        for (item in elements)
            if (!inRange(item))
                return false

        return true
    }

    /** @return Находится ли переданная нота в диапазоне */
    fun inRange(element: Note): Boolean = element in fromNote..endNote
}
