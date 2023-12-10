package com.app.music_app.view.paino_box

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.music_app.view.colors.AppColors
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
    backColor: Color = AppColors.WhiteSmoke,
    pianoBoxDefaultColor: Color = AppColors.LightCyan,
    pianoBoxPressedColor: Color = AppColors.PacificCyan,
    textColor: Color = AppColors.PacificCyan,
    onPianoClick: (PianoKeyboard) -> Unit,
    vararg keyboards: PianoKeyboard,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.piano_box_text),
            textAlign = TextAlign.Center,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
        // Общий контейнер
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(3.dp, color = backColor),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(backColor, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            RowWithWrap(
                horizontalSpacer = 20.dp,
                verticalSpacer = 20.dp
            ) {
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
                            .pointerInput(PointerEventType.Press) {
                                    color = pianoBoxPressedColor
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