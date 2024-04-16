package com.app.music_app.exercises.builder

import androidx.compose.runtime.Composable
import com.app.music_app.exercises.difficulty.DifficultyInfo

/**
 * Задаёт возможные варианты конструкций упражнения (какие сложности могут быть)
 */
interface ExerciseInfo {

    // Название упражнения
    val name: String
    // Описание упражнения
    val description: String

    /**
     * @return все сложности, доступные для данного упражнения
     */
    @Composable
    fun buildDifficulties(): Array<DifficultyInfo>

}