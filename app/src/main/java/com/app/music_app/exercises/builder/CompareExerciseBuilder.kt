package com.app.music_app.exercises.builder

import com.app.music_app.exercises.difficulty.DifficultyInfo

class CompareExerciseBuilder : ExerciseBuilder {
    override val name: String
        get() = "Interval comparison"
    override val description: String
        get() = "You need to compare different intervals"

    override fun buildDifficulties(): Array<DifficultyInfo> {
        return arrayOf(
            DifficultyInfo(
                "Basic",
                "Basic intervals: minor and major seconds",
                onClick = {}),
            DifficultyInfo(
                "Mozart's favorite interval",
                "Learning to recognize thirds",
                onClick = {}),
            DifficultyInfo(
                "Ceremonial intervals",
                "Playing fourths and fifths",
                onClick = {})
        )
    }
}