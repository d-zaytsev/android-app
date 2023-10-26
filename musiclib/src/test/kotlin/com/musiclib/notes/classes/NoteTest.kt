package com.musiclib.notes.classes

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
                "Si -1 Piano None Whole",
                Note(NoteName.Do).previous().toString()
            )
        }

        @Test
        fun `Next octave check`() {
            Assertions.assertEquals(
                "Do 1 Piano None Whole",
                Note(NoteName.Si).next().toString()
            )
        }

        @Test
        fun `Note equal check`() {
            Assertions.assertEquals(
                "Re 0 Piano None Whole",
                Note(NoteName.Do).next().previous().next().toString()
            )
        }
    }

    @Nested
    inner class `Note compare test` {
        @Test
        fun `Equal notes test`() {
            val res = Note(NoteName.Do).compareTo(Note(NoteName.Do));

            Assertions.assertEquals(0, res);
        }

        @Test
        fun `Dif octave notes test1`() {
            val res = Note(NoteName.Do, 1).compareTo(Note(NoteName.Do, 2));

            Assertions.assertEquals(-1, res);
        }

        @Test
        fun `Dif octave notes test2`() {
            val res = Note(NoteName.Do, 1).compareTo(Note(NoteName.Do, -1));

            Assertions.assertEquals(1, res);
        }

        @Test
        fun `Equal note names test1`() {
            val res = Note(NoteName.Do, sign = Alteration.FlatSign).compareTo(Note(NoteName.Do));

            Assertions.assertEquals(-1, res);
        }

        @Test
        fun `Equal note names test2`() {
            val res = Note(NoteName.Do).compareTo(Note(NoteName.Do, sign = Alteration.SharpSign));

            Assertions.assertEquals(-1, res);
        }
    }
}