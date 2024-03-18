package com.musiclib.notes.interfaces

import com.musiclib.notes.note_metadata.NoteVolume

/** Представляет собой ноту с дополнительными характеристиками звука */
interface MelodyNote : BasicNote, MusicPause {
    /** Динамика */
    val volume: NoteVolume
}