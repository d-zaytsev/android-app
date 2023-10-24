package instruments

import com.musiclib.Alteration
import com.musiclib.notes.Note
import com.musiclib.notes.NoteName


// Piano notes: https://github.com/fuhton/piano-mp3
class VirtualPiano() : AbstractInstrument() {

    // Диапазон современного фортепиано: от ноты Ля субконтроктавы(-4) до ноты До пятой октавы(4)
    override val instrumentRange =
        InstrumentRange(Note(NoteName.La, octave = -4), Note(NoteName.Do, octave = 4))

    override fun soundPath(note: Note): String {
        if (!rangeCheck(note))
            throw IllegalArgumentException("Note is outside the permissible musical range of the ${this.javaClass}")

        val map = mapOf(
            NoteName.Do to "c",
            NoteName.Re to "d",
            NoteName.Mi to "e",
            NoteName.Fa to "f",
            NoteName.Sol to "g",
            NoteName.La to "a",
            NoteName.Si to "b"
        )

        val oct = note.octave + 4 // Главная октава - 4я по счёту в файлах

        // бемоль = диез, в файлах только бемоли
        if (note.sign == Alteration.SharpSign)
            return map[note.nextNote().name] + "b" + oct.toString()
        else if (note.sign == Alteration.FlatSign)
            return map[note.name] + "b" + oct.toString()
        else
            return map[note.name] + oct.toString()
    }
}