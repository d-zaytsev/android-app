package com.app.music_app.app_theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp


data class AppColorScheme(
    // https://m3.material.io/styles/color/roles

    val surface: Color,         // Постоянный цвет фона
    val onSurface: Color,       // Цвет текста на обычном фоне
    val primary: Color,         // Цвет самых главных элементов
    val onPrimary: Color,       // Цвет текста на главных элементах
    val secondary: Color,       // Менее заметные элементы
    val onSecondary: Color,     // Текст и значки на фоне вторичного цвета
    val tertiary: Color,        // Что-то "иное", не самое важное, но на чём тоже есть акцент
    val onTertiary: Color,      // Текст и значки на фоне третичного цвета
    val error: Color,           // Сообщает о состоянии ошибки (например неверно выбранный эл-т)
    val onError: Color,         // Текст на эл-те с цветом ошибки
    val success: Color,         // Сообщает о состоянии успеха
    val onSuccess: Color,       // Текст на эл-те с цветом успеха
    val outline: Color,         // Границы, например контур текстового поля
    val outlineVariant: Color,  // Границы для разделителей, не важные
)

data class AppSize(
    val large: Dp,
    val medium: Dp,
    val normal: Dp,
    val small: Dp
)

data class AppTypography(
    val display: TextStyle,     // Очень крупный текст
    val titleLarge: TextStyle,  // Заголовок
    val body: TextStyle,        // Основной текст
    val label: TextStyle,       // Небольшие надписи или метки
)

data class AppShape(
    val pianoKey: Shape,        // Особая форма кнопок фортепиано
    val button: Shape,          // Для разных кнопок
    val container: Shape        // Для контейнеров (Box, Card)
)