package com.musiclib.notes.range

import com.musiclib.intervals.Interval
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName

/**
 * Представляет собой определённый диапазон нот
 */
data class NoteRange(
    override val start: Note,
    override val endInclusive: Note
) : Iterable<Note>, ClosedRange<Note> {
    constructor(octaveFrom: Int, endOctave: Int = octaveFrom) : this(
        Note(
            NoteName.Do,
            octave = octaveFrom
        ), Note(NoteName.Si, octave = endOctave)
    )

    init {
        require(start < endInclusive) { "Left border cannot be greater than the right" }
    }

    override fun iterator(): Iterator<Note> = NoteIterator(start, endInclusive)

    /** Количество целых нот в диапазоне */
    val wholeNotesCount: Int
        get() = (endInclusive.octave * 7 + endInclusive.name.ordinal) -
                (start.octave * 7 + start.name.ordinal - 1)

    /** @return Находится ли переданная нота в диапазоне */
    fun inRange(element: Note): Boolean = element in start..endInclusive
    fun inRangeAll(elements: Collection<Note>): Boolean {
        for (item in elements)
            if (!inRange(item))
                return false

        return true
    }
}
