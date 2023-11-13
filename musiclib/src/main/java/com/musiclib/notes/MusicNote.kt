package com.musiclib.notes

import com.musiclib.Alteration

/** Представляет собой ноту (абсолютную ступень) */
interface MusicNote {

    /** Ступень (слоговая нотация) */
    val name: NoteName;

    /** Знак альтерации */
    val sign: Alteration;

    /** Номер октавы, 0 - главная */
    val octave: Int;
}