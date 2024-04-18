package com.app.music_app.logic.exercises.builder

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.app.music_app.logic.exercises.difficulty.DifficultyInfo
import com.app.music_app.logic.exercises.logic.CompareExercise
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName
import com.musiclib.intervals.IntervalType
import com.musiclib.notes.range.NoteRange

class CompareExerciseInfo : ExerciseInfo {
    override val name: String
        get() = "Interval comparison"
    override val description: String
        get() = "You need to compare different intervals"

    @Composable
    override fun buildDifficulties(): Array<DifficultyInfo> {
        return arrayOf(
            DifficultyInfo(
                "Basic",
                "Basic intervals: minor and major seconds",
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Secunda, IntervalType.Small),
                        Interval(IntervalName.Secunda, IntervalType.Large)
                    )
                )
            ),
            DifficultyInfo(
                "Normal",
                "Learning to recognize thirds",
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Tertia, IntervalType.Small),
                        Interval(IntervalName.Tertia, IntervalType.Large)
                    )
                )
            ),
            DifficultyInfo(
                "Hard",
                "Playing fourths and fifths",
                defaultExerciseOf(
                    intervals = arrayOf(
                        Interval(IntervalName.Quarta, IntervalType.Extended),
                        Interval(IntervalName.Quinta, IntervalType.Pure),
                        Interval(IntervalName.Sexta, IntervalType.Small),
                        Interval(IntervalName.Sexta, IntervalType.Large)
                    )
                )
            )
        )
    }
    companion object {
        private val range = NoteRange(-4, 4)
        private const val TASK_COUNT = 5
    }

    @Composable
    private fun defaultExerciseOf(intervals: Array<Interval>): CompareExercise {
        return CompareExercise(
            LocalContext.current,
            range,
            TASK_COUNT,
            false,
            fixDirection = false,
            possibleIntervals = intervals
        )
    }
}