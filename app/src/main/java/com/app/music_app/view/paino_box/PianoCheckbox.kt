package com.app.music_app.view.paino_box

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.view.colors.AppColor
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.example.android_app.R

/**
 * Рисует checkbox с клавиатурами
 * @param modifier Позволяет настраивать размеры контейнера
 * @param keyboards Элементы для отрисовки
 * */
@Composable
fun PianoCheckbox(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.piano_box_text),
    backColor: Color = Color.White,
    pianoBoxDefaultColor: Color = AppColor.NonPhotoBlue,
    pianoBoxPressedColor: Color = AppColor.PacificCyan,
    textColor: Color = Color.Black,
    onPianoClick: (keyboard: PianoKeyboard, isLast: Boolean) -> Unit,
    vararg keyboards: PianoKeyboard,
) {

    if (keyboards.isEmpty())
        throw IllegalArgumentException("Can't draw zero keyboards")

    Column(
        modifier = modifier
            .shadow(1.dp, RoundedCornerShape(10.dp))
            .background(backColor, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text,
            textAlign = TextAlign.Center,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(10.dp)
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

                var clicksCount by remember { mutableStateOf(0) }
                // Чтобы элемент переставал работать после всех выборов
                var canClick by remember { mutableStateOf(true) }

                // Box с клавиатурой
                repeat(keyboards.size) {
                    // Цвет каждого квадрата (меняется при нажатии)
                    var color by remember { mutableStateOf(pianoBoxDefaultColor) }

                    Box(
                        modifier = Modifier
                            .size(keyboards[it].size.times(1.2f))
                            .border(
                                BorderStroke(1.dp, color = color),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(color, shape = RoundedCornerShape(15.dp))
                            .padding(3.dp)
                            .pointerInput(Unit) {
                                if (canClick) {
                                    color = pianoBoxPressedColor
                                    clicksCount++
                                    // Когда остаётся один вариант
                                    if (clicksCount >= keyboards.size - 1)
                                        canClick = false
                                    onPianoClick(keyboards[it], !canClick)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Рисуем клавиатуру внутри каждого квадратика
                        keyboards[it].Draw()
                    }
                }
            }

        }
    }
}