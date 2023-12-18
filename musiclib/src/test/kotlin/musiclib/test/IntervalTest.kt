package musiclib.test

import com.musiclib.notes.data.Alteration
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName
import com.musiclib.intervals.IntervalType
import com.musiclib.notes.Note
import com.musiclib.notes.data.NoteName
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class IntervalTest {

    val noteDo = Note(NoteName.Do);
    val noteRe = Note(NoteName.Re);
    val noteMi = Note(NoteName.Mi);
    val noteFa = Note(NoteName.Fa);
    val noteDoPrevOct = Note(NoteName.Do, -1);

    @Nested
    inner class `Interval Main Constructor Check` {
        @Test
        fun `Ordinary interval creating`() {
            val interval = Interval(IntervalName.Quarta, IntervalType.Extended)

            assertEquals("Extended Quarta", interval.toString())
        }

        @Test
        fun `Creating a non-existent interval`() {
            assertThrows<IllegalArgumentException>
            {
                Interval(IntervalName.Quarta, IntervalType.Small)
            }
        }
    }

    @Nested
    inner class `Interval Notes Constructor Check` {

        @Test
        fun `Prima check`() {
            val interval = Interval(noteDo, noteDo);

            assertEquals("Pure Prima", interval.toString())
        }

        @Test
        fun `Large secunda check`() {
            val interval = Interval(noteDo, noteRe);

            assertEquals("Large Secunda", interval.toString())
        }

        @Test
        fun `Small secunda check`() {
            val interval = Interval(noteDo, Note(NoteName.Do, sign = Alteration.SharpSign))

            assertEquals("Small Secunda", interval.toString())
        }

        @Test
        fun `Small tertia check`() {
            val interval = Interval(noteDo, Note(NoteName.Mi, sign = Alteration.FlatSign))
            assertEquals("Small Tertia", interval.toString())
        }

        @Test
        fun `Large tertia check`() {
            val interval = Interval(noteDo, noteMi);
            assertEquals("Large Tertia", interval.toString())
        }

        @Test
        fun `Pure quarta check`() {
            val interval = Interval(noteDo, noteFa);
            assertEquals("Pure Quarta", interval.toString())
        }

        @Test
        fun `Extended quarta check`() {
            val interval = Interval(noteDo, Note(NoteName.Fa, sign = Alteration.SharpSign))
            assertEquals("Extended Quarta", interval.toString())
        }

        @Test
        fun `Small sexta check`() {
            val interval = Interval(noteDo, Note(NoteName.La, sign = Alteration.FlatSign))
            assertEquals("Small Sexta", interval.toString())
        }

        @Test
        fun `Large sexta check`() {
            val interval = Interval(noteDo, Note(NoteName.La));
            assertEquals("Large Sexta", interval.toString())
        }

        @Test
        fun `Small septima check`() {
            val interval = Interval(noteDo, Note(NoteName.Si, sign = Alteration.FlatSign))
            assertEquals("Small Septima", interval.toString())
        }

        @Test
        fun `Large septima check`() {
            val interval = Interval(noteDo, Note(NoteName.Si));
            assertEquals("Large Septima", interval.toString())
        }

        @Test
        fun `Different octavas check`() {
            val interval = Interval(Note(NoteName.Si), Note(NoteName.Re, 1))

            assertEquals("Small Tertia", interval.toString())
        }

        @Test
        fun `Octava check`() {
            val interval = Interval(noteDo, noteDoPrevOct)

            assertEquals("Pure Octava", interval.toString())
        }
    }
}