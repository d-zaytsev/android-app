package com.musiclib.notes

/**
 * Представляет собой определённый диапазон нот
 */
data class NoteRange(val from: Note, val to: Note) {

    init {
        if (from > to)
            throw IllegalArgumentException("Left border cannot be greater than the right")
    }

    /** Количество нот в диапазоне */
    val noteCount: Int
        get() = (to.octave * 7 + to.name.ordinal) - (from.octave * 7 + from.name.ordinal - 1)

    fun inRangeAll(elements: Collection<Note>): Boolean {
        for (item in elements)
            if (!inRange(item))
                return false

        return true
    }

    /** @return Находится ли переданная нота в диапазоне */
    fun inRange(element: Note): Boolean = element in from..to
}
