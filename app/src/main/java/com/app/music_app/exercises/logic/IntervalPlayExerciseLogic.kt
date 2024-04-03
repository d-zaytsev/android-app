package com.app.music_app.exercises.logic

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.string_names.MusicIntervalResources
import com.app.music_app.string_names.NoteResources
import com.app.music_app.exercises.pages.CountTaskPage
import com.app.music_app.exercises.pages.ResultsPage
import com.app.music_app.components.custom_progress_bar.TaskProgressBar
import com.app.music_app.exercises.interfaces.AbstractExercise.ScreenName.RESULTS_SCREEN
import com.app.music_app.exercises.interfaces.AbstractExercise.ScreenName.TASK_SCREEN
import com.example.android_app.R
import com.musiclib.intervals.Interval
import com.musiclib.notes.Note
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
    val screens = remember { Array(taskCount) { "${TASK_SCREEN}:$it" } }

    val notesList = remember { range.toList() }

    // Эта штука нужна чтобы не спамить ошибками
    var lastNote = remember { range.start.previousWhole() }

    // Интерфейс
    Column(modifier = Modifier.fillMaxSize()) {
        TaskProgressBar(points = succeedPoints, progress = points, maxProgress = maxProgress)

        NavHost( // Содержит все экраны
            navController = navController,
            startDestination = screens[0]
        ) {
            composable(RESULTS_SCREEN) {
                ResultsPage(succeedPoints.toInt(), maxProgress.toInt())
            }

            for (i in 0 until taskCount) {
                composable(screens[i]) {
                    val interval = remember { possibleIntervals.random() }
                    val pair = remember { getPair(notesList, interval) }
                    val moveFrom =
                        remember { Random.nextBoolean() } // true - откладываем от первой ноты
                    val text = remember { getText(context, pair, interval, moveFrom) }

                    Log.d("AMOGUS", pair.toString())

                    var first = remember { false }
                    var second = remember { false }

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
                            lastNote = note

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
                                        navController.navigate(RESULTS_SCREEN) {
                                            popUpTo(RESULTS_SCREEN)
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

private fun getText(
    context: Context,
    pair: Pair<Note, Note>,
    interval: Interval,
    from: Boolean
): AnnotatedString {
    return buildAnnotatedString {
        // Вообще весь текст
        withStyle(SpanStyle(fontSize = 30.sp)) {
            val intervalName = MusicIntervalResources.nameOf(context, interval)

            val noteName = if (from)
                "${NoteResources.nameOf(context, pair.first.name)}${
                    NoteResources.nameOf(
                        context,
                        pair.first.sign
                    )
                }"
            else
                "${NoteResources.nameOf(context, pair.second.name)}${
                    NoteResources.nameOf(
                        context,
                        pair.second.sign
                    )
                }"

            val textParts =
                (if (from) context.getString(R.string.note_delaying_from) else context.getString(R.string.note_delaying_to))
                    .split("%")

            // Выделяем некоторые части жирным
            textParts.forEachIndexed { i, s ->
                append(s)
                if (i == 0) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append(intervalName)
                    }
                } else if (i == 1) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append(noteName)
                    }
                }
            }

        }
    }
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