package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.note_player.instruments.VirtualPiano
import com.app.music_app.study_manager.pages.CompareTaskPage
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.app.music_app.view.task_progress_indicator.TaskProgressBar
import com.musiclib.notes.Melody
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.Pause
import com.musiclib.notes.data.Alteration
import com.musiclib.notes.data.NoteDuration
import com.musiclib.notes.data.NoteName
import com.musiclib.NoteRange
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            val context = LocalContext.current
            val testMelody1 = Melody(
                listOf(
                    MelodyNote(NoteName.Do),
                    MelodyNote(NoteName.Do, sign = Alteration.SharpSign),
                    Pause(NoteDuration.Half),
                    MelodyNote(NoteName.Do),
                    MelodyNote(NoteName.Re)
                )
            )

            val testMelody2 = Melody(
                listOf(
                    MelodyNote(NoteName.Re),
                    MelodyNote(NoteName.Re, sign = Alteration.FlatSign),
                    Pause(NoteDuration.Half),
                    MelodyNote(NoteName.Re),
                    MelodyNote(NoteName.Do)
                )
            )

            val testPianos1 =
                arrayOf(
                    PianoKeyboard(
                        size = DpSize(100.dp, 100.dp),
                        noteRange = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
                        context = context
                    ),
                    PianoKeyboard(
                        size = DpSize(100.dp, 100.dp),
                        noteRange = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
                        context = context
                    )
                )

            testPianos1[0].mark(note = Note(NoteName.Do))
            testPianos1[0].mark(note = Note(NoteName.Do, sign = Alteration.SharpSign))

            testPianos1[1].mark(note = Note(NoteName.Do))
            testPianos1[1].mark(note = Note(NoteName.Re))

            val testPianos2 =
                arrayOf(
                    PianoKeyboard(
                        size = DpSize(100.dp, 100.dp),
                        noteRange = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
                        context = context
                    ),
                    PianoKeyboard(
                        size = DpSize(100.dp, 100.dp),
                        noteRange = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
                        context = context
                    )
                )

            testPianos2[0].mark(note = Note(NoteName.Re))
            testPianos2[0].mark(note = Note(NoteName.Re, sign = Alteration.FlatSign))

            testPianos2[1].mark(note = Note(NoteName.Re))
            testPianos2[1].mark(note = Note(NoteName.Do))

            // С помощью него переключаемся
            val navController = rememberNavController()
            // Содержит все экраны
            Column {
                var points by remember { mutableStateOf( 3 ) }
                TaskProgressBar(points, 10)
                NavHost(
                    navController = navController,
                    startDestination = "screen1"
                ) {
                    composable("screen1") {
                        // Функция которая будет запускаться при переходе на screen1
                        CompareTaskPage(
                            context = context,
                            melodyToPlay = testMelody1,
                            playInstrument = VirtualPiano(),
                            onEnd = {
                                points++
                                navController.navigate("screen2") {
                                    popUpTo("screen2") // удаляем из стека
                                }
                            },
                            keyboards = testPianos1
                        )
                    }

                    composable("screen2") {
                        // Функция которая будет запускаться при переходе на screen1
                        CompareTaskPage(
                            context = context,
                            melodyToPlay = testMelody1,
                            playInstrument = VirtualPiano(),
                            keyboards = testPianos1,
                            onEnd = {

                            }
                        )
                    }
                }
            }
        }
    }
}
