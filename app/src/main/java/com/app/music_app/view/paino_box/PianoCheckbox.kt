package com.app.music_app.view.paino_box

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    mainColor: Color = Color(72, 202, 228),
    backgroundColor: Color = Color(202, 240, 248),
    vararg keyboards: PianoKeyboard,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.piano_box_text),
            textAlign = TextAlign.Center,
            color = mainColor,
            fontWeight = FontWeight.Bold
        )
        // Общий контейнер
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(3.dp, color = mainColor),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(backgroundColor, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            RowWithWrap (horizontalSpacer = 20.dp,
                verticalSpacer = 20.dp) {
                // Box с клавиатурой
                repeat(keyboards.size) {
                    Box(
                        modifier = Modifier
                            .size(keyboards[it].size.times(1.2f))
                            .border(
                                BorderStroke(1.dp, color = mainColor),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(3.dp),
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