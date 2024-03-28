package com.app.music_app.view.paino_box

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.app.music_app.view.colors.AppColor
import com.app.music_app.view.piano_keyboard.PianoKeyboard

/**
 * Рисует фортепиано и рамку вокруг него
 */
@Composable
fun PianoBox(
    keyboard: PianoKeyboard,
    backgroundColor: Color = AppColor.NonPhotoBlue,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(keyboard.size.times(1.2f))
            .border(
                BorderStroke(1.dp, color = backgroundColor),
                shape = RoundedCornerShape(15.dp)
            )
            .background(backgroundColor, shape = RoundedCornerShape(15.dp))
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        // Рисуем клавиатуру внутри каждого квадратика
        keyboard.Draw()
    }
}