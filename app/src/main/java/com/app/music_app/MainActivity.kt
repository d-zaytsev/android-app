package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.app.music_app.instruments.VirtualPiano
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.musiclib.Alteration
import com.musiclib.notes.Note
import com.musiclib.notes.NoteName
import com.musiclib.notes.NoteRange
import kotlinx.coroutines.GlobalScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        val vp = VirtualPiano()
        fun soundOf(note: Note) = vp.soundOf(this, note)
        setContent {

            Column {
                val piano = PianoKeyboard(
                    NoteRange(Note(NoteName.Si), Note(NoteName.Si, octave = 1)),
                    soundOf = ::soundOf,
                    context = LocalContext.current
                )
                piano.Draw()

                Button(onClick = { piano.mark(Note(NoteName.Si)) }) {

                }
            }
        }
    }
}