package com.app.music_app.view.task_progress_indicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.view.colors.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TaskProgressBar(points: Int, maxPoints: Int) {
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
            color = AppColor.HonoluluBlue,
            trackColor = AppColor.WhiteSmoke,
            progress = animatedProgress,
            modifier = Modifier
                .height(15.dp)
                .fillMaxWidth()
        )
    }

}