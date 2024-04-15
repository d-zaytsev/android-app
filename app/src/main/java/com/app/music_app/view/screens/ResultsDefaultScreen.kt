package com.app.music_app.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.music_app.view.app_theme.AppTheme
import com.example.android_app.R

/**
 * Экран с результатами упражнения
 * @param points Кол-во заработанных очков
 * @param maxPoints Сколько всего очков можно было заработать
 */
@Composable
fun ResultsScreen(points: Int, maxPoints: Int) {
    Column(
        modifier = Modifier
            .background(AppTheme.color.surface)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.35f))
        Text(
            stringResource(R.string.congratulations),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.size.medium),
            textAlign = TextAlign.Center,
            color = AppTheme.color.success,
            style = AppTheme.typography.display
        )
        SuccessBar(progress = points / maxPoints.toFloat())
    }
}

@Composable
private fun SuccessBar(progress: Float) {
    Column(
        modifier = Modifier.padding(horizontal = AppTheme.size.small),
        horizontalAlignment = Alignment.Start
    ) {
        // Шарики в статус баре
        LinearProgressIndicator(
            modifier = Modifier
                .clip(AppTheme.shape.container)
                .height(20.dp)
                .fillMaxWidth(),
            color = getIntermediateColor(progress),
            progress = { progress })


        // Надписи
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text(
                stringResource(R.string.not_bad),
                style = AppTheme.typography.label,
                color = AppTheme.color.onSurface
            )
            Text(
                stringResource(R.string.fine),
                style = AppTheme.typography.label,
                color = AppTheme.color.onSurface
            )
            Text(
                stringResource(R.string.good),
                style = AppTheme.typography.label,
                color = AppTheme.color.onSurface
            )
            Text(
                stringResource(R.string.amazing),
                style = AppTheme.typography.label,
                color = AppTheme.color.onSurface
            )
        }
    }
}

@Composable
private fun Ball(color: Color) {
    Box(
        modifier = Modifier
            .padding(vertical = 1.dp)
            .size(AppTheme.size.normal)
            .clip(CircleShape)
            .background(color = color)
    )
}

/**
 * Находит промежуточное значение между двумя числами на основе заданного коэффициент
 */
private fun interpol(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

/**
 * Находит промежуточный цвет
 */
@Composable
private fun getIntermediateColor(
    progress: Float,
    minColor: Color = AppTheme.color.success,
    maxColor: Color = AppTheme.color.onSuccess
): Color {
    // Нахождение средних значений для каждой составляющей
    val red = interpol(minColor.red, maxColor.red, progress)
    val green = interpol(minColor.green, maxColor.green, progress)
    val blue = interpol(minColor.blue, maxColor.blue, progress)

    // Сбор нового цвета из средних значений
    return Color(red = red, green = green, blue = blue)
}