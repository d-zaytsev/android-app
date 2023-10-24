package com.app.music_app

import com.musiclib.Alteration
import com.musiclib.notes.Note
import com.musiclib.notes.NoteName
import instruments.VirtualPiano
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VirtualPianoTest {

    val vp = VirtualPiano();

    @Test
    fun `Main octave Do check`() {
        Assertions.assertEquals("c4", vp.soundPath(Note(NoteName.Do)))
    }

    @Test
    fun `a0 check`() {
        Assertions.assertEquals("a0", vp.soundPath(Note(NoteName.La, -4)))
    }

    @Test
    fun `D#1 Check1`() {
        Assertions.assertEquals(
            "eb1",
            vp.soundPath(Note(NoteName.Re, octave = -3, sign = Alteration.SharpSign))
        )
    }

    @Test
    fun `D#1 Check2`() {
        Assertions.assertEquals(
            "eb1",
            vp.soundPath(Note(NoteName.Mi, octave = -3, sign = Alteration.FlatSign))
        )
    }
}