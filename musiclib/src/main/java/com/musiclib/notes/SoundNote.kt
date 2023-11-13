package com.musiclib.notes

import com.musiclib.Alteration

/**
 * Представляет собой ноту, содержащую дополнительную информацию о своём звучании
 */
class SoundNote(
    override val volume: NoteVolume,
    override val name: NoteName,
    override val sign: Alteration,
    override val octave: Int,
    override val duration: NoteDuration
) : MelodyNote {

    constructor(note: Note) : this(NoteVolume.Forte, note.name, note.sign, note.octave, NoteDuration.Whole)

    override fun toString(): String = "$name $octave $sign $volume $duration"
    override fun hashCode(): Int = this.toString().hashCode()
}