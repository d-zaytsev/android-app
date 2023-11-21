package com.musiclib.notes

import com.musiclib.notes.data.Alteration
import com.musiclib.notes.data.NoteDuration
import com.musiclib.notes.data.NoteName
import com.musiclib.notes.data.NoteVolume
import com.musiclib.notes.interfaces.MelodyNote

/**
 * Представляет собой ноту, содержащую дополнительную информацию о своём звучании
 */
class MelodyNote(
    override val name: NoteName,
    override val volume: NoteVolume = NoteVolume.Forte,
    override val sign: Alteration = Alteration.None,
    override val octave: Int = 0,
    override val duration: NoteDuration = NoteDuration.Whole
) : MelodyNote {

    constructor(note: Note) : this(note.name, NoteVolume.Forte , note.sign, note.octave, NoteDuration.Whole)

    fun toBasicNote(): Note = Note(name, octave, sign)

    override fun toString(): String = "$name $octave $sign $volume $duration"
    override fun hashCode(): Int = this.toString().hashCode()
}