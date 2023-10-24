package com.musiclib.notes

/** Основные ступени (Слоговая система наименования)
 * @param dist Расстояние до ноты До в этой октаве */
enum class NoteName(val dist: Float) {
    Do(0f),
    Re(1f),
    Mi(2f),
    Fa(2.5f),
    Sol(3.5f),
    La(4.5f),
    Si(5.5f)
}