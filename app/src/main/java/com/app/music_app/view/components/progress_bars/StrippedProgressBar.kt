package com.app.music_app.view.components.progress_bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.music_app.view.app_theme.AppTheme
@Composable
fun StripedProgressBar(
    progress: Float,
    firstColor: Color = AppTheme.color.success,
    secondColor: Color = AppTheme.color.onSuccess,
) {
    Box(
        modifier = Modifier
            .clip(AppTheme.shape.container)
            .background(AppTheme.color.tertiary)
            .height(AppTheme.size.normal)
    ) {
        Box(
            modifier = Modifier
                .clip(AppTheme.shape.container)
                .background(StripeBrush(firstColor, secondColor, 5.dp))
                .fillMaxHeight()
                .fillMaxWidth(progress)
        )
    }
}
@Composable
private fun StripeBrush(
    stripeColor: Color,
    stripeBg: Color,
    stripeWidth: Dp
): Brush {
    val stripeWidthPx = with(LocalDensity.current) { stripeWidth.toPx() }
    val brushSizePx = 2 * stripeWidthPx
    val stripeStart = stripeWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to stripeBg,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated
    )
}