package com.app.music_app.exercises.difficulty

import com.app.music_app.exercises.logic.AbstractExercise

/**
 * Информация для заполнения карточки (сложность, упражнение
 */
data class DifficultyInfo(val name: String, val description: String = "", val exercise: AbstractExercise)