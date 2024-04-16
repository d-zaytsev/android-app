package com.app.music_app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.exercises.builder.ExerciseInfo
import com.app.music_app.view.screens.DrawMainScreen
import com.app.music_app.view.screens.TaskDifficultyScreen

/**
 * Выполняет отрисовку главного экрана с возможностью выбора упражнений
 * @param exercises Класс с данными о каждом упражнении (имя, описание, как запускать)
 */
@Composable
fun StartApp(vararg exercises: ExerciseInfo) {
    val navController = rememberNavController()

    NavHost(navController, Screen.MAIN) {
        // При выборе упражнения на главном экране совершает переход
        composable(Screen.MAIN) {
            DrawMainScreen(exercises) { selectedName ->
                navController.navigate(
                    "${Screen.DIFFICULTY}_${selectedName}"
                )
            }
        }

        exercises.forEach {exerciseInfo ->
            composable("${Screen.DIFFICULTY}_${exerciseInfo.name}") {
                TaskDifficultyScreen(exerciseInfo.buildDifficulties()) {
                    //TODO добавить кнопку custom
                }
            }
        }
    }
}

object Screen {
    const val MAIN = "MAIN_SCREEN"
    const val DIFFICULTY = "DIFFICULTY_SCREEN"
}