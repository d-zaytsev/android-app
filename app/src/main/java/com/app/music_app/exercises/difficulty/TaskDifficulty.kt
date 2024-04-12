package com.app.music_app.exercises.difficulty

/**
 * Сложность выбранного упражнения
 */
data class TaskDifficulty(val name: String, val description: String = "", val onClick: () -> Unit)