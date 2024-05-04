package com.app.music_app.logic.exercises.builder

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.app.music_app.MainActivity
import com.app.music_app.logic.exercises.difficulty.DifficultyInfo
import com.app.music_app.logic.exercises.logic.CompareExercise
import com.app.music_app.logic.exercises.logic.PlayIntervalExercise
import com.example.android_app.R
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName
import com.musiclib.intervals.IntervalType
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange

class PlayExerciseInfo(private val context: Context, private val activity: Activity) : ExerciseInfo {
    override val name: String
        get() = context.getString(R.string.play_task_name)
    override val description: String
        get() = context.getString(R.string.play_task_description)

    @Composable
    override fun buildDifficulties(): Array<DifficultyInfo> {
        return arrayOf(
            DifficultyInfo(
                stringResource(R.string.task_difficulty_basic),
                stringResource(R.string.task_difficulty_basic_description),
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Secunda, IntervalType.Small),
                        Interval(IntervalName.Secunda, IntervalType.Large)
                    )
                )
            ),
            DifficultyInfo(
                stringResource(R.string.task_difficulty_intermidiate),
                stringResource(R.string.task_difficulty_description_intermidiate_2),
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Tertia, IntervalType.Small),
                        Interval(IntervalName.Tertia, IntervalType.Large)
                    )
                )
            ),
            DifficultyInfo(
                stringResource(R.string.task_difficulty_advanced),
                stringResource(R.string.task_difficulty_advanced_description),
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Quarta, IntervalType.Extended),
                        Interval(IntervalName.Quinta, IntervalType.Pure),
                        Interval(IntervalName.Sexta, IntervalType.Small),
                        Interval(IntervalName.Sexta, IntervalType.Large)
                    )
                )
            ),
            DifficultyInfo(
                stringResource(R.string.task_difficulty_expert),
                stringResource(R.string.task_difficulty_description_expert_2),
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Octava, IntervalType.Pure),
                        Interval(IntervalName.Septima, IntervalType.Small),
                        Interval(IntervalName.Septima, IntervalType.Large)
                    )
                )
            )
        )
    }

    companion object {
        private val range = NoteRange(Note(NoteName.Si, octave = -1), Note(NoteName.Do, octave = 1))
        private const val TASK_COUNT = 10
    }

    @Composable
    private fun defaultExerciseOf(intervals: Array<Interval>): PlayIntervalExercise {
        return PlayIntervalExercise(
            context,
            activity,
            range,
            TASK_COUNT,
            5,
            possibleIntervals = intervals
        )
    }
}