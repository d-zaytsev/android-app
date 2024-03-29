package com.app.music_app.view.progress_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.music_app.view.colors.AppColor

/**
 * @param points верные ответы
 * @param progress прогресс в целом
 * @param maxProgress какой прогресс максимален
 */
@Composable
fun TaskProgressBar(
    points: Float,
    progress: Float,
    maxProgress: Float,
    color: Color = AppColor.HonoluluBlue,
    trackColor: Color = AppColor.WhiteSmoke
) {
    // Animation for progress bar filling
    val animatedProgress = animateFloatAsState(
        targetValue = progress / maxProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    ).value

    // Animation for color changes
    val animatedColor by animateColorAsState(
        targetValue = color.copy(alpha = calculateAlpha(points, progress)),
        label = ""
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        LinearProgressIndicator(
            progress = { animatedProgress },
            color = animatedColor,
            trackColor = trackColor,
            modifier = Modifier
                .height(15.dp)
                .fillMaxWidth()
        )
    }

}

/**
 * Считаем насыщенность цвета
 * @param points верные ответы
 * @param progress все ответы
 */
private fun calculateAlpha(points: Float, progress: Float): Float {
    if (progress == 0f)
        return 1f
    if (points > progress)
        throw IllegalStateException("True answers > answers")
    val res = points / progress
    // Ограничение чтобы слишком тускло не было
    return if (res < 0.2f) 0.2f else res
}