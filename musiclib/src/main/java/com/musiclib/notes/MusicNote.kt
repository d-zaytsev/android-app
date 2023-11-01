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

    /** @return Следующую ноту относительно этой */
    fun next(): Note;

    /** @return Предыдущую ноту относительно этой */
    fun previous(): Note;

    /** @return Целая ли нота */
    fun isWhole(): Boolean

    /** @return Целая нота */
    fun toWhole(): Note

    /** @return Повышенная на пол тона нота */
    fun toExt(): Note

    /**@return Пониженная на пол тона нота*/
    fun loLow(): Note
}