package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.app.music_app.study_manager.compare_task.CompareTaskManager
import com.musiclib.notes.Note
import com.musiclib.notes.data.NoteName
import com.musiclib.NoteRange
import com.musiclib.intervals.Interval


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            CompareTaskManager(
                context = LocalContext.current,
                range = NoteRange(Note(NoteName.Do), Note(NoteName.Si)),
                chooseVariants = 2..2,
                taskCount = 1,
                fixFirstNote = false,
                Interval(Note(NoteName.Do), Note(NoteName.Mi)),
                Interval(Note(NoteName.Do), Note(NoteName.Re))
            )
        }
    }
}
