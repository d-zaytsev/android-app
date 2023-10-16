package com.musiclib.notes

import com.musiclib.Alteration

/** Представляет собой ноту (абсолютную ступень) */
interface MusicNote : MusicPause {

    /** Ступень (слоговая нотация) */
    val name: NoteName;

    /** Знак альтерации */
    val sign: Alteration;

    /** Динамика */
    val volume: NoteVolume;

    /** Номер октавы, 0 - главная */
    val octave: Int;

}