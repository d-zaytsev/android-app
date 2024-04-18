package com.app.music_app.view.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified

@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = style.color
) {
    // При изменении стиля -> перерисовка
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    // Состояние, обозначающее, нужно ли рисовать текст
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = style.fontSize

    Text(
        text = text,
        color = color,
        // Изменяем отрисовку
        modifier = modifier.drawWithContent {
            // Рисуем только если shouldDraw, иначе не рисуем
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        // Вызывается во время расчёта размеров текста
        onTextLayout = { result ->
            // Если текст не помещается
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.8
                )
            } else {
                shouldDraw = true
            }
        }
    )
}