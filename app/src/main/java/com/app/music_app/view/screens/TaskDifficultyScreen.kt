package com.app.music_app.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.music_app.logic.exercises.difficulty.DifficultyInfo
import com.app.music_app.view.app_theme.AppTheme
import com.example.android_app.R

@Composable
fun DrawDifficultyScreen(
    difficulties: Array<DifficultyInfo>,
    onDifficultySelect: (DifficultyInfo) -> Unit,
    onCustomButtonClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            stringResource(R.string.select_difficulty_level),
            color = AppTheme.color.secondary,
            style = AppTheme.typography.title
        )

        CustomDivider()

        DrawDifficultyCards(difficulties) { onDifficultySelect(it) }

        if (onCustomButtonClick != null) DrawCustomButtonArea(onCustomButtonClick)
    }
}

/**
 * Рисует список из сложностей
 * @param onClick Возвращает какую сложность выбрал пользователь
 */
@Composable
private fun DrawDifficultyCards(
    difficulties: Array<DifficultyInfo>,
    onClick: (DifficultyInfo) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(0.85f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        difficulties.forEach { data ->
            item {
                DifficultyCard(data) { onClick(data) }
            }
        }
    }
}

@Composable
private fun DifficultyCard(difficulty: DifficultyInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(AppTheme.size.normal)
            .clickable(onClick = onClick),
        backgroundColor = AppTheme.color.secondary,
        elevation = 5.dp,
        shape = AppTheme.shape.container
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                difficulty.name,
                modifier = Modifier.padding(horizontal = 5.dp),
                textAlign = TextAlign.Center,
                color = AppTheme.color.onSecondary,
                style = AppTheme.typography.title
            )
            if (difficulty.description.isNotEmpty())
                Text(
                    difficulty.description,
                    textAlign = TextAlign.Center,
                    color = AppTheme.color.onSecondary,
                    style = AppTheme.typography.body,
                    modifier = Modifier.padding(5.dp)
                )
        }

    }
}

/**
 * Разделитель, вписанный в тему экрана
 *
 */
@Composable
private fun CustomDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = AppTheme.size.normal),
        color = AppTheme.color.outline
    )
}

/**
 * Рисует кнопку и разделитель
 */
@Composable
private fun DrawCustomButtonArea(onCustomButtonClick: (() -> Unit)) {

    CustomDivider()

    Button(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.color.tertiary),
        elevation = ButtonDefaults.elevation(2.dp),
        shape = AppTheme.shape.container,
        onClick = onCustomButtonClick
    ) {
        Text(
            stringResource(R.string.custom),
            textAlign = TextAlign.Center,
            color = AppTheme.color.onTertiary,
            style = AppTheme.typography.title
        )
    }
}