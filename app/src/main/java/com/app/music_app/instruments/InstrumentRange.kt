package com.app.music_app.instruments

import com.musiclib.notes.Note

data class InstrumentRange(val noteA: Note, val noteB: Note) {
    fun inRange(note: Note): Boolean = note in noteA..noteB
}
