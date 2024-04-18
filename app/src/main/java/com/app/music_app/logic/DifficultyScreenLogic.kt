package com.app.music_app.logic

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.logic.exercises.difficulty.DifficultyInfo
import com.app.music_app.view.screens.DrawDifficultyScreen

/**
 * Выполняет отрисовку экрана выбора сложности и переход к выполнению упражнения
 */
@Composable
fun DifficultyScreen(difficulties: Array<DifficultyInfo>) {
    val navController = rememberNavController()

    NavHost(navController, DifficultyScreen.START) {
        composable(DifficultyScreen.START) {
            DrawDifficultyScreen(
                difficulties = difficulties,
                onDifficultySelect = { difficultyInfo ->
                    navController.navigate("${DifficultyScreen.EXERCISE}_${difficultyInfo.name}")
                }) {
                //TODO добавить реализацию кнопки CUSTOM
            }
        }

        difficulties.forEach { difficultyInfo ->
            composable("${DifficultyScreen.EXERCISE}_${difficultyInfo.name}") {
                difficultyInfo.exercise.run()
            }
        }
    }
}

object DifficultyScreen {
    const val START = "DIFFICULTY_SCREEN_START"
    const val EXERCISE = "EXERCISE_SCREEN"
}