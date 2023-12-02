package com.app.music_app.view.play_button

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.view.colors.AppColors
import com.example.android_app.R

class PlayButton(private val size: DpSize) {

    private val iconWidth = size.height
    private val textSize = (size.height.value / 2).sp

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Draw() {
        val text = stringResource(R.string.repeat)

        var selected by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(if (selected) 0.8f else 1f, label = "")

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.lightBlue
            ),
            modifier = Modifier
                .size(size)
                .scale(scale)
                .pointerInteropFilter {
                    // рассматриваются разные случаи action
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            selected = true
                        }

                        MotionEvent.ACTION_UP -> {
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
}