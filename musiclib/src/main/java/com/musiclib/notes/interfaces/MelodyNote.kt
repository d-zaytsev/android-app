package com.musiclib.notes.interfaces

import com.musiclib.notes.data.NoteVolume

/** Представляет собой ноту с дополнительными характеристиками звука */
interface MelodyNote : BasicNote, MusicPause {
    /** Динамика */
    val volume: NoteVolume
}