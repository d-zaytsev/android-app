package com.app.music_app.note_player.instruments

import com.app.music_app.note_player.interfaces.AbstractInstrument
import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange


// Piano notes: https://github.com/fuhton/piano-mp3
class VirtualPiano() : AbstractInstrument() {

    // Диапазон современного фортепиано: от ноты Ля субконтроктавы(-4) до ноты До пятой октавы(4)
    override val instrumentRange =
        NoteRange(Note(NoteName.La, octave = -4), Note(NoteName.Do, octave = 4))

    private companion object {
        val map = mapOf(
            NoteName.Do to "c",
            NoteName.Re to "d",
            NoteName.Mi to "e",
            NoteName.Fa to "f",
            NoteName.Sol to "g",
            NoteName.La to "a",
            NoteName.Si to "b"
        )
    }

    override fun soundPath(note: Note): String {
        require(instrumentRange.inRange(note)) { "Note is outside the permissible musical range of the ${this.javaClass}" }

        val oct = note.octave + 4 // Главная октава - 4я по счёту в файлах

        // бемоль -> диез, в файлах только бемоли
        return when (note.sign) {
            Alteration.SharpSign -> map[note.nextWhole().name] + "b" + oct.toString()
            Alteration.FlatSign -> map[note.name] + "b" + oct.toString()
            else -> map[note.name] + oct.toString()
        }
    }
}