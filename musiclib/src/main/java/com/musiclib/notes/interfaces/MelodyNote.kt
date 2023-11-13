package com.musiclib.notes.interfaces

import com.musiclib.notes.NoteVolume

interface MelodyNote : BasicNote, MusicPause {
    /** Динамика */
    val volume: NoteVolume
}