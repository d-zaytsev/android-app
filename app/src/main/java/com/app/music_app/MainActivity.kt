package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.app.music_app.instruments.VirtualPiano
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.musiclib.notes.Note
import com.musiclib.notes.NoteName
import com.musiclib.notes.NoteRange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        val vp = VirtualPiano()
        fun soundOf(note: Note) = vp.soundOf(this, note)
        setContent {

            PianoKeyboard(
                NoteRange(Note(NoteName.Do), Note(NoteName.Si)),
                soundOf = ::soundOf
            ).Draw()
        }
    }
}


@Preview
@Composable
fun PianoKeyboardPreview() {
    PianoKeyboard(
        NoteRange(Note(NoteName.Do), Note(NoteName.Si)),
    ).Draw()
}