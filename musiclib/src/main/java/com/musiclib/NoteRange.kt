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

    private val notesList: List<Note>
    init {
        if (fromNote > endNote)
            throw IllegalArgumentException("Left border cannot be greater than the right")

        this.notesList = mutableListOf()

        var curNote = fromNote
        while (curNote != endNote) {
            notesList.add(curNote)
            curNote = curNote.add(0.5f)
        }
        notesList.add(endNote)
    }

    /** Список всех нот в этом диапазоне */
    val notes: List<Note>
        get() = notesList


    /** @return Находится ли переданная нота в диапазоне */
    fun inRange(element: Note): Boolean = element in fromNote..endNote
    fun inRangeAll(elements: Collection<Note>): Boolean {
        for (item in elements)
            if (!inRange(item))
                return false

        return true
    }
}
