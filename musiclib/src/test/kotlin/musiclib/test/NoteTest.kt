package musiclib.test

import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.note_metadata.NoteName
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NoteTest {

    @Nested
    inner class `Previous & Next note test` {
        @Test
        fun `Prev octave check`() {
            Assertions.assertEquals(
                "Si -1 None",
                Note(NoteName.Do).previousWhole().toString()
            )
        }

        @Test
        fun `Next octave check`() {
            Assertions.assertEquals(
                "Do 1 None",
                Note(NoteName.Si).nextWhole().toString()
            )
        }

        @Test
        fun `Note equal check`() {
            Assertions.assertEquals(
                "Re 0 None",
                Note(NoteName.Do).nextWhole().previousWhole().nextWhole().toString()
            )
        }

        @Test
        fun `Next semitone check #1`() {
            Assertions.assertEquals(
                "Re 0 None",
                Note(NoteName.Do, sign = Alteration.SharpSign).nextSemitone().toString()
            )
        }
        @Test
        fun `Next semitone check #2`() {
            Assertions.assertEquals(
                "Do 0 SharpSign",
                Note(NoteName.Do).nextSemitone().toString()
            )
        }
        @Test
        fun `Next semitone check #3`() {
            Assertions.assertEquals(
                "Do 1 None",
                Note(NoteName.Si).nextSemitone().toString()
            )
        }
        @Test
        fun `Next semitone check #4`() {
            Assertions.assertEquals(
                "Fa 0 None",
                Note(NoteName.Mi).nextSemitone().toString()
            )
        }

        @Test
        fun `Previous semitone check #1`() {
            Assertions.assertEquals(
                "Do 0 None",
                Note(NoteName.Do, sign = Alteration.SharpSign).previousSemitone().toString()
            )
        }
        @Test
        fun `Previous semitone check #2`() {
            Assertions.assertEquals(
                "Si -1 None",
                Note(NoteName.Do).previousSemitone().toString()
            )
        }
        @Test
        fun `Previous semitone check #3`() {
            Assertions.assertEquals(
                "Mi 0 None",
                Note(NoteName.Fa).previousSemitone().toString()
            )
        }
        @Test
        fun `Previous semitone check #4`() {
            Assertions.assertEquals(
                "Re 0 FlatSign",
                Note(NoteName.Re).previousSemitone().toString()
            )
        }
    }

    @Nested
    inner class `Note compare test` {
        @Test
        fun `Equal notes test`() {
            Assertions.assertTrue(Note(NoteName.Do, octave = 1, sign = Alteration.SharpSign) == Note(
                NoteName.Do, octave = 1, sign = Alteration.SharpSign))
        }

        @Test
        fun `Dif octave notes test1`() {
            Assertions.assertTrue(Note(NoteName.Do, 1) < Note(NoteName.Do, 2))
        }

        @Test
        fun `Dif octave notes test2`() {
            Assertions.assertTrue(Note(NoteName.Do, 1) > Note(NoteName.Do, -1));
        }

        @Test
        fun `Dif octave notes test3`() {
            Assertions.assertTrue(Note(NoteName.Si, 1) < Note(NoteName.Do, 2));
        }

    }
}