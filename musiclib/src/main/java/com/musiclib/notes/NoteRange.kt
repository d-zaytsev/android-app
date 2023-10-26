package com.musiclib.notes

data class NoteRange(val noteA: Note, val noteB: Note) {
    fun inRange(note: Note): Boolean = note in noteA..noteB
}
