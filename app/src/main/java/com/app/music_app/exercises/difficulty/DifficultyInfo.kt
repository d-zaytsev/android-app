package com.app.music_app.exercises.difficulty

/**
 * Информация для заполнения карточки (сложность, упражнение
 */
data class DifficultyInfo(val name: String, val description: String = "", val onClick: () -> Unit)