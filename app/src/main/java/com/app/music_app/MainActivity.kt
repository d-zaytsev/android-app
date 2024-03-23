package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.app.music_app.tasks.logic.CompareTaskManager
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange
import com.musiclib.intervals.Interval
import com.musiclib.notes.note_metadata.Alteration


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            CompareTaskManager(
                context = LocalContext.current,
                range = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
                chooseVariants = 2..2,
                taskCount = 5,
                fixFirstNote = true,
                fixDirection = true,
                Interval(Note(NoteName.Do), Note(NoteName.Re)),
                Interval(Note(NoteName.Do), Note(NoteName.Do, sign = Alteration.SharpSign))
            )
        }
    }
}
