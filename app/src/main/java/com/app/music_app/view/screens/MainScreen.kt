package com.app.music_app.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.logic.exercises.builder.ExerciseInfo
import com.app.music_app.view.app_theme.AppColor
import com.app.music_app.view.app_theme.AppTheme
import com.example.android_app.R

/**
 * Отрисовывает главный экран
 * @param onClick Передаёт наружу параметр упражнения, которое было выбрано
 */
@Composable
fun DrawMainScreen(exercises: Array<out ExerciseInfo>, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        horizontalAlignment = AbsoluteAlignment.Left,
        verticalArrangement = Arrangement.Center
    ) {
        DisplayText()

        // Список упражнений с возможностью выбора
        LazyRow {
            exercises.forEach {
                item {
                    ExerciseCard(it.name, it.description) { onClick(it.name) }
                }
            }
        }
    }
}

/**
 * Рисует карточку упражнения
 */
@Composable
private fun ExerciseCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .size(DpSize(250.dp, 250.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.color.secondary,
            contentColor = AppTheme.color.onSecondary
        )
    ) {
        Text(title, modifier = Modifier.padding(10.dp), style = AppTheme.typography.title)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            color = AppTheme.color.outlineVariant,
            thickness = 1.dp
        )
        Text(description, modifier = Modifier.padding(10.dp), style = AppTheme.typography.body)

    }
}

@Composable
private fun DisplayText() {
    val lineColor = AppTheme.color.primary

    Text(
        stringResource(R.string.exercises),
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .drawBehind {
                val strokeWidthPx = 2.dp.toPx()
                val verticalOffset = size.height - 2.sp.toPx()
                drawLine(
                    color = lineColor,
                    strokeWidth = strokeWidthPx,
                    start = Offset(-5f, verticalOffset),
                    end = Offset(size.width + 5, verticalOffset)
                )
            },
        style = AppTheme.typography.display,
        color = AppTheme.color.primary
    )
}