package com.musiclib.notes.interfaces

/**
* Представляет собой последовательность нот (или пауз)
*/
interface Melody {

    /** Скорость исполнения музыки (сколько мс длится целая нота)*/
    val temp: Int

    /** Последовательность нот или музыкальных пауз */
    val notes: List<MusicPause>
}