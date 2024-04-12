package com.app.music_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.app.music_app.exercises.difficulty.TaskDifficulty
import com.app.music_app.view.app_theme.AppTheme
import com.app.music_app.view.components.custom_progress_bar.TaskProgressBar
import com.app.music_app.exercises.difficulty.TaskDifficultyPage
import com.app.music_app.exercises.logic.CompareExercise
import com.app.music_app.exercises.logic.PlayIntervalExercise
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName
import com.musiclib.intervals.IntervalType
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            AppTheme {
                val task = PlayIntervalExercise(
                    LocalContext.current,
                    this@MainActivity,
                    NoteRange(Note(NoteName.Do), Note(NoteName.Si)),
                    2,
                    10,
                    arrayOf(
                        Interval(IntervalName.Secunda, IntervalType.Small),
                        Interval(IntervalName.Secunda, IntervalType.Large)
                    )
                )

                task.run()
            }
        }
    }

    @Preview
    @Composable
    private fun TaskScreen() {
        val testTitle = "Заголовок"
        val testDescription = "Зеленчук жоский пидр"
        AppTheme {
            TaskDifficultyPage(
                listOf(
                    TaskDifficulty(testTitle, testDescription) {},
                    TaskDifficulty(testTitle, testDescription) {},
                    TaskDifficulty(testTitle, testDescription) {},
                    TaskDifficulty(testTitle, testDescription) {},
                    TaskDifficulty(testTitle, testDescription) {},
                    TaskDifficulty(testTitle, testDescription) {},
                )
            ) {}
        }
    }

//    @Preview
//    @Composable
//    private fun CompareExercisePreview() {
//        val task = CompareExercise(
//            LocalContext.current,
//            NoteRange(Note(NoteName.Do), Note(NoteName.Si, octave = 1)),
//            5,
//            possibleIntervals = arrayOf(
//                Interval(IntervalName.Tertia, IntervalType.Small),
//                Interval(IntervalName.Secunda, IntervalType.Small)
//            )
//        )
//
//        task.run()
//    }

//    @Preview
//    @Composable
//    private fun ProgressBarPreview() {
//        Column(modifier = Modifier.fillMaxSize()) {
//
//            var points by remember { mutableFloatStateOf(0f) }
//            var progress by remember { mutableFloatStateOf(0f) }
//            val maxPoints = 100f
//
//            TaskProgressBar(points = points, progress = progress, maxProgress = maxPoints)
//            Button(onClick = {
//                progress += 10f
//            }) {
//                Text("Add progress")
//            }
//            Button(onClick = {
//                points += 10f
//            }) {
//                Text("Add points")
//            }
//        }
//    }

//    CountTask(
//                LocalContext.current,
//                this@MainActivity,
//                NoteRange(Note(NoteName.Do), Note(NoteName.Si)),
//                2,
//                10,
//                Interval(IntervalName.Secunda, IntervalType.Small)
//            )

//    CompareTaskManager(
//    context = LocalContext.current,
//    range = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
//    chooseVariants = 2..2,
//    taskCount = 5,
//    fixFirstNote = true,
//    fixDirection = true,
//    Interval(Note(NoteName.Do), Note(NoteName.Re)),
//    Interval(Note(NoteName.Do), Note(NoteName.Do, sign = Alteration.SharpSign))
//    )
}
