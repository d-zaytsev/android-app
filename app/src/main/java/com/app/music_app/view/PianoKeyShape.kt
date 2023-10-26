package com.app.music_app.view

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class PianoKeyShape(private val radius: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            RoundRect(
                0f,
                size.height,
                size.width,
                0f,
                CornerRadius(0f, 0f),
                CornerRadius(0f, 0f),
                CornerRadius(radius.value, radius.value),
                CornerRadius(radius.value, radius.value)
            )
        )
    }

}