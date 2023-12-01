package com.app.music_app.view.play_button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    @Composable
    fun Draw() {
        val text = stringResource(R.string.repeat)

        Button(onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.lightBlue
            ),
            border = BorderStroke(0.5.dp, Color.White),
            modifier = Modifier.size(size)) {
            Icon(
                modifier = Modifier
                    .size(iconWidth),
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Refresh button",
                tint = Color.White)
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = textSize,
                modifier = Modifier
                    .weight(1f)
                    .offset(x = -iconWidth/2), //иконка сдвигает текст слишком вправо
            )
        }
        }
}