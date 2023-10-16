package com.musiclib.notes

import com.musiclib.Alteration

class Note(
    override val name: NoteName,
    override val octave: Int = 0,
    override val volume: NoteVolume = NoteVolume.Piano,
    override val sign: Alteration = Alteration.None,
    override val duration: NoteDuration = NoteDuration.Whole
) : MusicNote {
    override fun toString(): String = "${name} ${octave} ${volume} ${sign} ${duration}"
}