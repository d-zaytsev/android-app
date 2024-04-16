package com.app.music_app.exercises.logic

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
    companion object TaskScreenNames {
        const val START_SCREEN = "task_start"
        const val TASK_SCREEN = "task_screen"
        const val RESULTS_SCREEN = "task_results"
    }

    protected fun screenNameOf(i: Int) = if (i == 0) START_SCREEN else "$TASK_SCREEN:$i"

}