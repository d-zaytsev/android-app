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
import com.app.music_app.view.names.MusicIntervalResources
import com.app.music_app.view.names.NoteResources
import com.app.music_app.view.screens.IntervalPlayScreen
import com.app.music_app.view.screens.ResultsScreen
import com.app.music_app.view.components.custom_progress_bar.TaskProgressBar
import com.app.music_app.exercises.logic.interfaces.AbstractExercise
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
class PlayIntervalExercise(
    private val context: Context,
    activity: Activity,
    private val range: NoteRange,
    private val taskCount: Int,
    private val maxAttemptsCount: Int,
    private val possibleIntervals: Array<Interval>
) : AbstractExercise() {

    init {
        require(checkAudioPermission(context, activity)) { "Can't get voice recording permission" }
        require(possibleIntervals.isNotEmpty()) { "Intervals count = 0" }
        require(maxAttemptsCount > 0) { "Attempts count can't be less than 1" }
        require(taskCount > 0) { "Task count can't be less than 1" }
        require(possibleIntervals.none { it.distance > range.endInclusive.pitch - range.start.pitch }) { " Can't use intervals bigger than range " }
    }

    private var lastNote = range.start.previousWhole() // последняя нота, сыгранная в упр-ии
    private val notesList = range.toList()

    @Composable
    override fun run() {

        // --- Оценка прогресса
        var succeedPoints by remember { mutableFloatStateOf(0f) } // Правильные ответы
        var points by remember { mutableFloatStateOf(0f) } // Общий прогресс
        val maxProgress = remember { taskCount * maxAttemptsCount.toFloat() }
        var curTaskErrors = remember { 0f } // Кол-во ошибок, совершённое в текущем упражнении

        val navController = rememberNavController()

        // Интерфейс
        Column(modifier = Modifier.fillMaxSize()) {
            TaskProgressBar(
                points = succeedPoints,
                progress = points,
                maxProgress = maxProgress
            )

            NavHost( // Содержит все экраны
                navController = navController,
                startDestination = START_SCREEN
            ) {
                composable(RESULTS_SCREEN) {
                    ResultsScreen(succeedPoints.toInt(), maxProgress.toInt())
                }

                for (i in 0 until taskCount) {
                    composable(screenNameOf(i)) {
                        val interval = remember { possibleIntervals.random() }
                        val pair = remember { getPair(notesList, interval) }
                        val moveFrom =
                            remember { Random.nextBoolean() } // true - откладываем от первой ноты
                        val text = remember { getText(context, pair, interval, moveFrom) }

                        var firstNoteFlag = remember { false } // была ли угадана первая нота

                        IntervalPlayScreen(
                            context,
                            range,
                            text
                        )
                        { note ->

                            // Определение правильности нажатия
                            if (note == pair.first) {
                                firstNoteFlag = true
                                return@IntervalPlayScreen true
                            } else if (note == pair.second && firstNoteFlag) {
                                firstNoteFlag = false
                                points += maxAttemptsCount
                                succeedPoints += maxAttemptsCount - curTaskErrors
                                curTaskErrors = 0f

                                lastNote = note // иначе засчитаем ошибку в следующем этапе

                                // Переход на следующее задание
                                if (i < taskCount - 1) {
                                    runBlocking {
                                        launch(Dispatchers.Main) {
                                            navController.navigate(screenNameOf(i + 1)) {
                                                popUpTo(screenNameOf(i + 1))
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

                                return@IntervalPlayScreen true
                            }

                            if (curTaskErrors < maxAttemptsCount && note != lastNote)
                                curTaskErrors++

                            lastNote = note // иначе будем повторно засчитывать ошибки
                            firstNoteFlag = false
                            return@IntervalPlayScreen false

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

                // Текст в ресурсах разделён % в местах где должны быть подстановки

                val textParts =
                    (if (from) context.getString(R.string.note_delaying_from) else context.getString(
                        R.string.note_delaying_to
                    ))
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

    /**
     * @param excludedNote Нота, которая не должна быть в паре на первом месте
     */
    private fun getPair(
        notes: List<Note>,
        interval: Interval,
    ): Pair<Note, Note> {
        val first = notes.random().shuffleSign()
        val second =
            notes.filter { note -> abs(first.pitch - note.pitch) == interval.distance }.random()
                .shuffleSign()
        Log.d("AMOGUS", "$first $second")
        return if (first < second) Pair(first, second) else Pair(second, first)
    }

    /**
     * Может поменять ноту на аналогичную, но с другим знаком
     */
    private fun Note.shuffleSign(): Note {
        return if (this.isWhole() || !Random.nextBoolean())
            this
        else if (this.isExt())
            this.nextSemitone().previousSemitone()
        else
            this.previousSemitone().nextSemitone()
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

}