package musiclib.test

import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NoteRangeTest {

    @Nested
    inner class `Count check` {
        @Test
        fun `Note count check`() {
            assertEquals(7, NoteRange(Note(NoteName.Do), Note(NoteName.Si)).wholeNotesCount)
        }

        @Test
        fun `Note count check (dif octavas)`() {
            assertEquals(
                2,
                NoteRange(
                    Note(NoteName.Si, octave = 1),
                    Note(NoteName.Do, octave = 2)
                ).wholeNotesCount
            )
        }

        @Test
        fun `Check`() {
            val range = NoteRange(0)
            assertEquals(
                2,
                NoteRange(
                    Note(NoteName.Si, octave = 1),
                    Note(NoteName.Do, octave = 2)
                ).wholeNotesCount
            )
        }
    }

    @Nested
    inner class `toList() check` {
        @Test
        fun `toList check #1`() {
            val act = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)).toList()
            val exp = listOf(
                Note(NoteName.Do),
                Note(NoteName.Do, sign = Alteration.SharpSign),
                Note(NoteName.Re),
                Note(NoteName.Re, sign = Alteration.SharpSign),
                Note(NoteName.Mi)
            )

            assertEquals(exp, act)
        }

        @Test
        fun `toList check #2`() {
            val act = NoteRange(
                Note(NoteName.Do, sign = Alteration.SharpSign),
                Note(NoteName.Mi)
            ).toList()
            val exp = listOf(
                Note(NoteName.Do, sign = Alteration.SharpSign),
                Note(NoteName.Re),
                Note(NoteName.Re, sign = Alteration.SharpSign),
                Note(NoteName.Mi)
            )

            assertEquals(exp, act)
        }

        @Test
        fun `toList check #3`() {
            val act =
                NoteRange(Note(NoteName.Do), Note(NoteName.Mi, sign = Alteration.FlatSign)).toList()
            val exp = listOf(
                Note(NoteName.Do, sign = Alteration.SharpSign),
                Note(NoteName.Re),
                Note(NoteName.Re, sign = Alteration.SharpSign),
            )

            assertEquals(exp, act)
        }
        @Test
        fun `toList check #4`() {
            val act =
                NoteRange(Note(NoteName.Si, sign = Alteration.FlatSign), Note(NoteName.Do, octave = 1 , sign = Alteration.SharpSign)).toList()
            val exp = listOf(
                Note(NoteName.Si, sign = Alteration.FlatSign),
                Note(NoteName.Si),
                Note(NoteName.Do),
                Note(NoteName.Do, sign = Alteration.SharpSign),
            )

            assertEquals(exp, act)
        }
    }
}