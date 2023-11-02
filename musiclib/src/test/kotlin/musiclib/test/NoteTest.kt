package musiclib.test

import com.musiclib.Alteration
import com.musiclib.notes.Note
import com.musiclib.notes.NoteName
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
                Note(NoteName.Do).previous().toString()
            )
        }

        @Test
        fun `Next octave check`() {
            Assertions.assertEquals(
                "Do 1 None",
                Note(NoteName.Si).next().toString()
            )
        }

        @Test
        fun `Note equal check`() {
            Assertions.assertEquals(
                "Re 0 None",
                Note(NoteName.Do).next().previous().next().toString()
            )
        }
    }

    @Nested
    inner class `Note compare test` {
        @Test
        fun `Equal notes test`() {
            Assertions.assertTrue(Note(NoteName.Do) == Note(NoteName.Do))
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

        @Test
        fun `Equal note names test1`() {
            Assertions.assertTrue(Note(NoteName.Do, sign = Alteration.FlatSign) < Note(NoteName.Do))
        }

        @Test
        fun `Equal note names test2`() {
            Assertions.assertTrue(
                Note(NoteName.Do) < Note(
                    NoteName.Do,
                    sign = Alteration.SharpSign
                )
            );
        }
    }
}