package com.app.music_app.view.play_button

import android.content.Context
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.note_player.MelodyPlayer
import com.app.music_app.note_player.interfaces.AbstractInstrument
import com.app.music_app.view.colors.AppColors
import com.example.android_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Элемент интерфейса, представляющий собой кнопку для воспроизведения мелодии. Имеет фиксированный размер
 * @param melody Мелодия, которая будет проигрываться при нажатии
 * @param instrument Инструмент, на котором будет воспроизводиться мелодия
 * @param mainColor Цвет кнопки
 * */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayButton(
    context: Context,
    melody: com.musiclib.notes.Melody,
    instrument: AbstractInstrument,
    size: DpSize = DpSize(300.dp, 50.dp),
    mainColor: Color = AppColors.LightCyan
) {
    val iconWidth = size.height
    val textSize = (size.height.value / 2).sp

    val text = stringResource(R.string.repeat)

    // selected позволяет определить, нажата ли кнопка в данный момент
    var selected by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (selected) 0.8f else 1f, label = "")

    var isPlaying by remember { mutableStateOf(false) }

    Button(
        enabled = !isPlaying,
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            disabledContainerColor = Color.Gray
        ),
        modifier = Modifier
            .size(size)
            .scale(scale)
            .pointerInteropFilter {
                // рассматриваются разные случаи action
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!isPlaying)
                            selected = true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (!isPlaying) {
                            CoroutineScope(Dispatchers.Default).launch {
                                isPlaying = true
                                MelodyPlayer(instrument).play(context, melody)
                                isPlaying = false
                            }
                        }
                        selected = false
                    }
                }
                true
            }
    ) {
        Icon(
            modifier = Modifier
                .size(iconWidth),
            imageVector = Icons.Rounded.QueueMusic,
            contentDescription = "Refresh button",
            tint = Color.White
        )
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = textSize,
            modifier = Modifier
                .weight(1f)
                .offset(x = -iconWidth / 2), //иконка сдвигает текст слишком вправо
        )
    }
}