package com.app.music_app.components.paino_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.app_theme.AppTheme
import com.app.music_app.components.piano_keyboard.PianoKeyboard
import com.example.android_app.R

/**
 * Рисует checkbox с клавиатурами
 * @param keyboards Элементы для отрисовки
 * @param onPianoClick (нажатая клавиатура, является ли она последней) -> правильный лм выбор
 * */
@Composable
fun PianoCheckbox(
    text: String = stringResource(R.string.piano_box_text),
    onPianoClick: (keyboard: PianoKeyboard, isLast: Boolean) -> Boolean,
    vararg keyboards: PianoKeyboard,
) {
    val pianoBoxDefaultColor: Color = AppTheme.color.tertiary
    val successColor: Color = AppTheme.color.success
    val errorColor: Color = AppTheme.color.error

    Column(
        modifier = Modifier
            .shadow(6.dp, AppTheme.shape.container)
            .background(AppTheme.color.surface, AppTheme.shape.container),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text,
            textAlign = TextAlign.Center,
            color = AppTheme.color.onSurface,
            style = AppTheme.typography.title,
            modifier = Modifier.padding(AppTheme.size.small)
        )
        // Общий контейнер
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            RowWithWrap(
                horizontalSpacer = keyboards[0].size.height / 5,
                verticalSpacer = keyboards[0].size.width / 5
            ) {
                var clicksCount by remember { mutableIntStateOf(0) }
                // Чтобы элемент переставал работать после всех выборов
                var canClick by remember { mutableStateOf(true) }

                // Box с клавиатурой
                repeat(keyboards.size) {
                    // Цвет каждого квадрата (меняется при нажатии)
                    var color by remember { mutableStateOf(pianoBoxDefaultColor) }
                    val keyboardLength = remember { keyboards[it].noteRange.wholeNotesCount }

                    PianoBox(
                        keyboard = keyboards[it],
                        widthMultiplier = if (keyboardLength == 2) 1.3f else if (keyboardLength == 3) 1.2f else 1.1f,
                        backgroundColor = color,
                        modifier = Modifier.pointerInput(Unit) {
                            if (canClick) {
                                clicksCount++
                                // Когда остаётся один вариант
                                if (clicksCount == keyboards.size - 1)
                                    canClick = false

                                // Окрашиваем в нужный цвет (в зависимости от правильности выбора)
                                color = if (onPianoClick(keyboards[it], !canClick))
                                    successColor
                                else
                                    errorColor
                            }
                        })
                }
            }

        }
    }
}