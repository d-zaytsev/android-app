package com.app.music_app.view.progress_bar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.music_app.view.colors.AppColor

@Composable
fun TaskProgressBar(
    points: Int,
    maxPoints: Int,
    color: Color = AppColor.HonoluluBlue,
    trackColor: Color = AppColor.WhiteSmoke
) {
    val animatedProgress = animateFloatAsState(
        targetValue = points.toFloat() / maxPoints,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        LinearProgressIndicator(
            progress = { animatedProgress },
            color = AppColor.HonoluluBlue,
            trackColor = AppColor.WhiteSmoke,
            modifier = Modifier
                .height(15.dp)
                .fillMaxWidth()
        )
    }

}