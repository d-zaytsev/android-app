package com.app.music_app.view.components.paino_box

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.app.music_app.view.app_theme.AppTheme
import com.app.music_app.view.components.piano_keyboard.PianoKeyboard
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
    modifier: Modifier = Modifier,
    vararg keyboards: PianoKeyboard,
) {
    val pianoBoxDefaultColor: Color = AppTheme.color.tertiary
    val successColor: Color = AppTheme.color.success
    val errorColor: Color = AppTheme.color.error

    // Контейнер для текста и бокса
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
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
        // Контейнер для пианин
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Контейнер который под пианины растягивается
            RowWithWrap(
                horizontalSpacer = AppTheme.size.small,
                verticalSpacer = AppTheme.size.small
            ) {
                // Наполнение

                var clicksCount by remember { mutableIntStateOf(0) }
                // Чтобы элемент переставал работать после всех выборов
                var canClick by remember { mutableStateOf(true) }

                // Каждая клавиатура
                repeat(keyboards.size) {
                    // Цвет каждого квадрата (меняется при нажатии)
                    var color by remember { mutableStateOf(pianoBoxDefaultColor) }
                    val keyboardLength = remember { keyboards[it].noteRange.wholeNotesCount }

                    // Пианина и обводка вокруг неё
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