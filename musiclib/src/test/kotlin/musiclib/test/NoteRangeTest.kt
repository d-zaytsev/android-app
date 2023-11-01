package musiclib.test

import com.musiclib.notes.Note
import com.musiclib.notes.NoteName
import com.musiclib.notes.NoteRange
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NoteRangeTest {

    @Test
    fun `Note count check`() {
        assertEquals(7, NoteRange(Note(NoteName.Do), Note(NoteName.Si)).noteCount)
    }

    @Test
    fun `Note count check (dif octavas)`() {
        assertEquals(
            2,
            NoteRange(Note(NoteName.Si, octave = 1), Note(NoteName.Do, octave = 2)).noteCount
        )
    }
}