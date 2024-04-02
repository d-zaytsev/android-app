package com.app.music_app.exercises.interfaces

interface AbstractExercise {

    /**
     * Запуск упражнения
     */
    abstract fun run(): ExerciseResults

    fun Float.toResultType(): ExerciseResults {
        return when (this) {
            in 0f..0.2f -> ExerciseResults.NOT_BAD
            in 0.2f..0.5f -> ExerciseResults.FINE
            in 0.5f..0.85f -> ExerciseResults.GREAT
            in 0.85f..1f -> ExerciseResults.AMAZING
            else -> throw IllegalArgumentException("Can't conver float to ExerciseResult")
        }
    }

}