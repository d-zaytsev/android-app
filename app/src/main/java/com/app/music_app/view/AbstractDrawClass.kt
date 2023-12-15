package com.app.music_app.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize

/**
 * Объединяет все классы, рисующие элементы
 */
abstract class AbstractDrawClass {
    abstract val size: DpSize
    protected abstract val context: Context
    @Composable
    abstract fun Draw()
}