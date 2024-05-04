package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.app.music_app.logic.DifficultyScreen
import com.app.music_app.logic.exercises.builder.CompareExerciseInfo
import com.app.music_app.view.app_theme.AppTheme
import com.app.music_app.logic.exercises.logic.PlayIntervalExercise
import com.app.music_app.logic.StartApp
import com.app.music_app.logic.exercises.builder.PlayExerciseInfo
import com.app.music_app.music_players.instruments.VirtualPiano
import com.app.music_app.view.components.paino_box.PianoBox
import com.app.music_app.view.components.paino_box.PianoCheckbox
import com.app.music_app.view.components.piano_keyboard.PianoKeyboard
import com.app.music_app.view.screens.CompareExerciseScreen
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName
import com.musiclib.intervals.IntervalType
import com.musiclib.notes.Melody
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange
import kotlin.math.min


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            AppTheme {
                val context = LocalContext.current
                val activity = this@MainActivity

                StartApp(CompareExerciseInfo(context), PlayExerciseInfo(context, activity))
            }
        }
    }

    @Preview(locale = "ru")
    @Composable
    private fun PianoBoxPreview() {
        AppTheme {
            val context = LocalContext.current
            val activity = this@MainActivity

            StartApp(CompareExerciseInfo(context), PlayExerciseInfo(context, activity))
        }
    }

}
