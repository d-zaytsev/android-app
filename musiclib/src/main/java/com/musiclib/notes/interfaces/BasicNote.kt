package com.musiclib.notes.interfaces

import com.musiclib.Alteration
import com.musiclib.notes.NoteName

/** Представляет собой ноту (абсолютную ступень) */
interface BasicNote {

    /** Ступень (слоговая нотация) */
    val name: NoteName

    /** Знак альтерации */
    val sign: Alteration

    /** Номер октавы, 0 - главная */
    val octave: Int

}