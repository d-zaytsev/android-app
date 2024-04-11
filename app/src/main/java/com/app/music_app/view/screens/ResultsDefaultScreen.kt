package com.app.music_app.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.app.music_app.view.app_theme.AppColor
import com.app.music_app.view.app_theme.AppTheme

/**
 * Экран с результатами упражнения
 * @param points Кол-во заработанных очков
 * @param maxPoints Сколько всего очков можно было заработать
 */
@Composable
fun ResultsScreen(points: Int, maxPoints: Int) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(AppTheme.color.surface)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.3f))
        Box(
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$points / $maxPoints",
                style = AppTheme.typography.display,
                color = AppTheme.color.primary
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.5f))

    }
}