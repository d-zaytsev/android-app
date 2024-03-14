package com.musiclib.notes.note_metadata

/**
 * Перечисление возможных длительностей ноты
 * @param value Коэффициент
 * */
enum class NoteDuration(val value: Float) {
    Whole(1f),         // целая
    Half(0.5f),        // половинная
    Quarter(0.25f),    // четвёртая
    Eight(0.125f),     // восьмая
    Sixteenth(0.0625f) // шестнадцатая
}