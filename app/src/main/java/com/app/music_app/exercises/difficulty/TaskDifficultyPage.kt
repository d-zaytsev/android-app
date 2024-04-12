package com.app.music_app.exercises.difficulty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.music_app.view.app_theme.AppTheme

@Composable
fun TaskDifficultyPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        repeat(3) {
            item {
                DifficultyCard()
            }
        }
    }

}

@Composable
private fun DifficultyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(AppTheme.size.normal),
        backgroundColor = AppTheme.color.secondary,
        elevation = 10.dp,
        shape = AppTheme.shape.container
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Title text!!!",
                textAlign = TextAlign.Center,
                color = AppTheme.color.onSecondary,
                style = AppTheme.typography.title,
                modifier = Modifier.padding(AppTheme.size.small)
            )
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt consectetur.",
                textAlign = TextAlign.Center,
                color = AppTheme.color.onSecondary,
                style = AppTheme.typography.body,
                modifier = Modifier.padding(AppTheme.size.small)
            )
        }

    }
}