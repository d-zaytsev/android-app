package com.app.music_app.exercises.difficulty

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.music_app.view.app_theme.AppTheme
import com.example.android_app.R

@Composable
fun TaskDifficultyPage(difficulties: List<TaskDifficulty>, onCustomClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            repeat(difficulties.size) {
                item {
                    DifficultyCard(difficulties[it])
                }
            }
        }

        if (onCustomClick != null) {
            Divider(modifier = Modifier.padding(horizontal = AppTheme.size.normal),color = AppTheme.color.outline)

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.color.tertiary),
                elevation = ButtonDefaults.elevation(2.dp),
                shape = AppTheme.shape.container,
                onClick = onCustomClick
            ) {
                Text(
                    stringResource(R.string.custom),
                    textAlign = TextAlign.Center,
                    color = AppTheme.color.onTertiary,
                    style = AppTheme.typography.title
                )
            }
        }
    }
}

@Composable
private fun DifficultyCard(difficulty: TaskDifficulty) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(AppTheme.size.normal)
            .clickable(onClick = difficulty.onClick),
        backgroundColor = AppTheme.color.secondary,
        elevation = 5.dp,
        shape = AppTheme.shape.container
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                difficulty.name,
                textAlign = TextAlign.Center,
                color = AppTheme.color.onSecondary,
                style = AppTheme.typography.title,
                modifier = Modifier.padding(AppTheme.size.small)
            )
            if (difficulty.description.isNotEmpty())
                Text(
                    difficulty.description,
                    textAlign = TextAlign.Center,
                    color = AppTheme.color.onSecondary,
                    style = AppTheme.typography.body,
                    modifier = Modifier.padding(AppTheme.size.small)
                )
        }

    }
}