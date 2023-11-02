package com.musiclib.notes

interface MelodyNote : MusicNote, MusicPause {
    /** Динамика */
    val volume: NoteVolume;
}