package com.app.music_app.logic

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.logic.exercises.builder.ExerciseInfo
import com.app.music_app.view.screens.DrawMainScreen

/**
 * Выполняет отрисовку главного экрана с возможностью выбора упражнений
 * @param exercises Класс с данными о каждом упражнении (имя, описание, как запускать)
 */
@Composable
fun StartApp(vararg exercises: ExerciseInfo) {
    val navController = rememberNavController()

    NavHost(navController, StartScreen.MAIN) {
        // При выборе упражнения на главном экране совершает переход
        composable(StartScreen.MAIN) {
            DrawMainScreen(exercises) { selectedName ->
                navController.navigate(
                    "${StartScreen.DIFFICULTY}_${selectedName}"
                )
            }
        }

        exercises.forEach { exerciseInfo ->
            composable("${StartScreen.DIFFICULTY}_${exerciseInfo.name}") {
                DifficultyScreen(exerciseInfo.buildDifficulties())
            }
        }
    }
}

object StartScreen {
    const val MAIN = "MAIN_SCREEN"
    const val DIFFICULTY = "DIFFICULTY_SCREEN"
}