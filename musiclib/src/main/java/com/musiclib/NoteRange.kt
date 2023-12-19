package com.musiclib

import com.musiclib.notes.Note
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

    val notes: List<Note>
        get() {
            val list = mutableListOf<Note>()

            var curNote = fromNote
            while (curNote != endNote) {
                list.add(curNote)
                curNote = curNote.add(0.5f)
            }
            list.add(endNote)

            return list
        }
    val wholeNotes: List<Note>
        get() {
            val list = mutableListOf<Note>()

            var curNote = fromNote
            while (curNote != endNote) {
                list.add(curNote)
                curNote = curNote.next()
            }
            list.add(endNote)

            return list
        }

    /** Количество целых нот в диапазоне */
    val wholeNotesCount: Int
        get() = (endNote.octave * 7 + endNote.name.ordinal) - (fromNote.octave * 7 + fromNote.name.ordinal - 1)

    /** @return Находится ли переданная нота в диапазоне */
    fun inRange(element: Note): Boolean = element in fromNote..endNote
    fun inRangeAll(elements: Collection<Note>): Boolean {
        for (item in elements)
            if (!inRange(item))
                return false

        return true
    }
}
