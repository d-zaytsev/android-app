package com.app.music_app.tasks.logic

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.tasks.pages.CountTaskPage
import com.app.music_app.tasks.pages.ResultsPage
import com.app.music_app.view.progress_bar.TaskProgressBar
import com.musiclib.intervals.Interval
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.random.Random

/**
 * Выполняет настройку задания и проводит его
 * @param range Диапазон нот, в котором разрешено выбирать интервалы и распознавать звуки
 * @param possibleIntervals Какие интервалы будут в упражнении
 * @param taskCount Кол-во заданий
 * @param maxAttemptsCount Кол-во ошибок, которое максимум можно совершить за каждое задание
 * */
@Composable
fun CountTask(
    context: Context,
    activity: Activity,
    range: NoteRange,
    taskCount: Int,
    maxAttemptsCount: Int,
    vararg possibleIntervals: Interval
) {
    // TODO сделать что-то с количеством аргументов и убрать require из этой функции
    require(checkAudioPermission(context, activity)) { "Can't get voice recording permission" }
    require(possibleIntervals.isNotEmpty()) { "Intervals count = 0" }
    require(maxAttemptsCount > 0) { "Attempts count can't be less than 1" }
    require(taskCount > 0) { "Task count can't be less than 1" }
    require(possibleIntervals.none { it.distance > range.endInclusive.pitch - range.start.pitch }) { " Can't use intervals bigger than range " }

    // Подсчёт правильных ответов/ошибок
    var succeedPoints by remember { mutableFloatStateOf(0f) } // Правильные ответы
    var points by remember { mutableFloatStateOf(0f) } // Общий прогресс
    val maxProgress = remember { taskCount * maxAttemptsCount.toFloat() }
    var errorAttempts by remember { mutableFloatStateOf(0f) } // Кол-во ошибок, совершённое в текущем упражнении

    val navController = rememberNavController()
    val screens = remember { Array(taskCount) { "${ScreenNames.TASK_SCREEN}:$it" } }

    val notesList = remember { range.toList() }

    // Интерфейс
    Column(modifier = Modifier.fillMaxSize()) {
        TaskProgressBar(points = succeedPoints, progress = points, maxProgress = maxProgress)

        NavHost( // Содержит все экраны
            navController = navController,
            startDestination = screens[0]
        ) {
            composable(ScreenNames.RESULTS_SCREEN) {
                ResultsPage(succeedPoints.toInt(), maxProgress.toInt())
            }

            for (i in 0 until taskCount) {
                composable(screens[i]) {
                    val interval = remember { possibleIntervals.random() }
                    val pair = remember { getPair(notesList, interval) }
                    val moveFrom =
                        remember { Random.nextBoolean() } // true - откладываем от первой ноты
                    val text = remember { getText(pair, interval, moveFrom) }

                    var first = remember { false }
                    var second = remember { false }

                    var lastNote = remember { range.start.previousWhole() }

                    CountTaskPage(
                        context,
                        range,
                        text
                    )
                    { note ->

                        // Определение правильности нажатия
                        if (note == pair.first) {
                            first = true
                            return@CountTaskPage true
                        } else if (note == pair.second && first && !second) {
                            second = true
                            points += maxAttemptsCount
                            succeedPoints += maxAttemptsCount - errorAttempts
                            errorAttempts = 0f

                            // Переход на следующее задание
                            if (i < taskCount - 1) {
                                runBlocking {
                                    launch(Dispatchers.Main) {
                                        navController.navigate(screens[i + 1]) {
                                            popUpTo(screens[i + 1])
                                        }
                                    }
                                }
                            } else
                                runBlocking {
                                    launch(Dispatchers.Main) {
                                        navController.navigate(ScreenNames.RESULTS_SCREEN) {
                                            popUpTo(ScreenNames.RESULTS_SCREEN)
                                        }
                                    }
                                }

                        }
                        // Если ошиблись

                        if (note != lastNote) {
                            if (errorAttempts < maxAttemptsCount)
                                errorAttempts++
                            lastNote = note
                        }

                        return@CountTaskPage false

                    }
                }
            }

        }
    }
}

private fun getText(pair: Pair<Note, Note>, interval: Interval, from: Boolean): String {
    return if (from)
        "Отложи $interval от ноты ${pair.first}"
    else
        "Отложи $interval, заканчивая нотой ${pair.second}"
}

private fun getPair(notes: List<Note>, interval: Interval): Pair<Note, Note> {
    val first = notes.random()
    val second =
        notes.filter { note -> abs(first.pitch - note.pitch) == interval.distance }.random()
    return if (first < second) Pair(first, second) else Pair(second, first)
}

/**
 * @return True - удалось получить разрешение
 */
private fun checkAudioPermission(context: Context, activity: Activity): Boolean {

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            1234
        )

        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED)
    }

    return true
}