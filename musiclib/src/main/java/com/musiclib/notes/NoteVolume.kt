package com.musiclib.notes

enum class NoteVolume(val num: Int) {
    Fortissimo(5), // ff - очень громко
    Forte(4),      // f - громко
    MezzoForte(3), // mf - умеренно громко
    Piano(2),      // p - тихо
    MezzoPiano(1), // mp - умеренно тихо
    Pianissimo(0)  // pp - очень тихо
}