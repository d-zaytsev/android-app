package com.musiclib.notes.interfaces

import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.note_metadata.NoteName

/** Представляет собой ноту (абсолютную ступень) */
interface BasicNote {

    /** Ступень (слоговая нотация) */
    val name: NoteName

    /** Знак альтерации */
    val sign: Alteration

    /** Номер октавы, 0 - главная */
    val octave: Int

}