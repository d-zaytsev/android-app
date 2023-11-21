package com.musiclib.notes.data

/** Основные ступени (Слоговая система наименования)
 * @param value Расстояние до ноты До в этой октаве */
enum class NoteName(val value: Float) {
    Do(0f),
    Re(1f),
    Mi(2f),
    Fa(2.5f),
    Sol(3.5f),
    La(4.5f),
    Si(5.5f)
}