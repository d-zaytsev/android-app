package com.app.music_app.tasks.pages

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
import com.app.music_app.view.colors.AppColor

/**
 * Экран с результатами упражнения
 * @param points Кол-во заработанных очков
 * @param maxPoints Сколько всего очков можно было заработать
 */
@Composable
fun ResultsPage(points: Int, maxPoints: Int) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(AppColor.WhiteSmoke)
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
                fontSize = 30.sp,
                color = AppColor.PacificCyan
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.5f))

    }
}