package com.app.music_app.view.components

/**
 * Сложность выбранного упражнения
 */
data class CardInfo(val name: String, val description: String = "", val onClick: () -> Unit)