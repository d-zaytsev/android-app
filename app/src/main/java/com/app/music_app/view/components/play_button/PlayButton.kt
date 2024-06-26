package com.app.music_app.view.components.play_button

import android.content.Context
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.music_players.MelodyPlayer
import com.app.music_app.music_players.interfaces.AbstractInstrument
import com.app.music_app.view.app_theme.AppColor
import com.app.music_app.view.app_theme.AppTheme
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
    modifier: Modifier = Modifier
) {
    val text = stringResource(R.string.repeat)

    // selected позволяет определить, нажата ли кнопка в данный момент
    var selected by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (selected) 0.8f else 1f, label = "")

    var isPlaying by remember { mutableStateOf(false) }

    Button(
        enabled = !isPlaying,
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.color.secondary,
            disabledContainerColor = AppTheme.color.inactive
        ),
        modifier = modifier
            .scale(scale) // анимация при нажатии
            .shadow(AppTheme.size.medium, shape = AppTheme.shape.button)
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

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .scale(1.5f),
                imageVector = Icons.AutoMirrored.Rounded.QueueMusic,
                contentDescription = "Refresh button",
                tint = AppTheme.color.surface
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                color = AppTheme.color.surface,
                textAlign = TextAlign.Center,
                style = AppTheme.typography.body
            )
        }

    }
}