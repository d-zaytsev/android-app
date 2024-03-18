package com.musiclib.intervals

/** Задаёт дополнительное свойство для интервалов. Не определяет увеличенные и уменьшенные
 * @param tone Позволяет посчитать изменение тона
 */
enum class IntervalType(val tone: Float) {
    Large(0.5f),      // Большой
    Small(0f),      // Малый
    Pure(0f),       // Чистый музыкальный интервал
    Extended(0.5f),   // Увеличенный интервал
    Reduced(-0.5f)     // Уменьшенный
}