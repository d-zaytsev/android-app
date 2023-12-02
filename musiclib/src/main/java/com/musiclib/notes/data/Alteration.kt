package com.musiclib.notes.data

/**
 * Представляет собой перечисление возможных знаков альтерации
 * @param value изменение тона ноты относительно целой
 */
enum class Alteration(val value: Float) {
    NaturalSign(0f),    // Бекар
    FlatSign(-0.5f),    // Бемоль
    SharpSign(0.5f),    // Диез
    None(0f)
}