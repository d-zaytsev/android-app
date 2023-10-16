package com.musiclib.melody

import com.musiclib.notes.Note

/**
 * Представляет мелодию как набор звуков, идущих с определённой скоростью
 * */
interface SoundMelody {

    /** Список нот (и пауз), идущих друг за другом */
    val notes: List<Note>;

    /** BPM: Количество четвёртых нот в минуту. */
    // 120 BPM = 2 четвёртых ноты в секунду
    val temp: Int;

}