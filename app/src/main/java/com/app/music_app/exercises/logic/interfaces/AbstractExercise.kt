package com.app.music_app.exercises.logic.interfaces

import androidx.compose.runtime.Composable

abstract class AbstractExercise {

    /**
     * Запуск упражнения
     */
    @Composable
    abstract fun run()

    /**
     * Имена экранов, используемых в задании
     */
    companion object ScreenName {
        const val TASK_SCREEN = "task_screen"
        const val RESULTS_SCREEN = "results"
    }

}