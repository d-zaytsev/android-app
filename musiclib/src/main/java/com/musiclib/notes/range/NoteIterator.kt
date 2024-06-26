package com.musiclib.notes.range

import com.musiclib.notes.Note

class NoteIterator(start: Note, private val end: Note) : Iterator<Note> {

    private var currentNote = start
    override fun hasNext(): Boolean = currentNote <= end
    override fun next(): Note {
        val temp = currentNote
        currentNote = currentNote.nextSemitone()
        return temp
    }
}