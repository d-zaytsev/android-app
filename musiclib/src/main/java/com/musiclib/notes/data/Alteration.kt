package com.musiclib.notes.data

/**
 * Представляет собой перечисление возможных знаков альтерации
 * @param value изменение тона ноты относительно целой
 */
enum class Alteration(val value: Float) {
    None(0f),
    FlatSign(-0.5f),    // Бемоль
    SharpSign(0.5f),    // Диез
    NaturalSign(0f)
}