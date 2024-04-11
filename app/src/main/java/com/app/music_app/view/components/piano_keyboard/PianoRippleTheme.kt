package com.app.music_app.view.components.piano_keyboard

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class PianoRippleTheme (private val pressedButtonColor: Color) : RippleTheme {
    @Composable
    override fun defaultColor(): Color = pressedButtonColor

    private val alpha = 1f;
    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(alpha, alpha, alpha, alpha)
}