package com.app.music_app.exercises.logic

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.music_player.instruments.VirtualPiano
import com.app.music_app.exercises.pages.ChooseTaskPage
import com.app.music_app.exercises.pages.ResultsPage
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.app.music_app.view.progress_bar.TaskProgressBar
import com.musiclib.notes.range.NoteRange
import com.musiclib.intervals.Interval
import com.musiclib.notes.Melody
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.Pause
import com.musiclib.notes.note_metadata.NoteDuration
import com.musiclib.notes.interfaces.MusicPause
import kotlin.math.abs

/**
 * Имена скринов, используемых в задании
 */
object ScreenNames {
    const val TASK_SCREEN = "task_screen"
    const val RESULTS_SCREEN = "results"
}

/**
 * Выполняет настройку задания и проводит его
 * @param range Диапазон нот, в котором разрешено выбирать интервалы
 * @param possibleIntervals Какие интервалы будут в упражнении
 * @param chooseVariants Кол-во вариантов выбора
 * @param taskCount Кол-во заданий
 * @param fixFirstNote Если true, то все интервалы будут содержать общую одной ноты
 * @param fixDirection Если true, то интервал будут звучать только по возрастанию нот
 * */
@Composable
fun CompareTaskManager(
    context: Context,
    range: NoteRange,
    chooseVariants: IntRange,
    taskCount: Int,
    fixFirstNote: Boolean = true,
    fixDirection: Boolean = true,
    vararg possibleIntervals: Interval
) {
    // TODO сделать что-то с количеством аргументов и убрать require из этой функции
    require(possibleIntervals.size >= 2) { "Can't use less than 2 intervals" }
    require(range.wholeNotesCount >= 3) { "Can't such small note range" }
    require(taskCount > 0) { "Can't be less than 1 task" }
    require(possibleIntervals.size >= chooseVariants.last - chooseVariants.first) { "Need more intervals to choose" }
    require(possibleIntervals.maxOf { it.distance } >= range.start.pitch - range.endInclusive.pitch) { "You choose too small range for such intervals" }

    val noteList = remember { range.toList() }

    Column {
        var succeedPoints by remember { mutableIntStateOf(0) } // Кол-во верно сделанных заданий
        var points by remember { mutableIntStateOf(0) }        // Кол-во пройденных заданий

        TaskProgressBar(
            succeedPoints.toFloat(),
            points.toFloat(),
            taskCount.toFloat()
        )   // Прогресс пользователя

        val navController =
            rememberNavController()         // С помощью него переключаемся между экранами

        // Список с названиями всех экранов с заданиями
        val screens = remember { Array(taskCount) { "${ScreenNames.TASK_SCREEN}:$it" } }

        NavHost( // Содержит все экраны
            navController = navController,
            startDestination = screens[0]
        ) {

            // Экран с результатами
            composable(ScreenNames.RESULTS_SCREEN) {
                ResultsPage(succeedPoints, taskCount)
            }

            for (i in 0 until taskCount) {
                // Если экран ещё не был проинициализирован
                composable(screens[i]) {
                    // Получаем данные для отрисовки страницы
                    val pairs = remember {
                        pairsByIntervals(
                            chooseVariants,
                            fixFirstNote,
                            noteList,
                            possibleIntervals
                        )
                    }
                    val melodies = remember { getMelodies(pairs, fixDirection) }
                    val pianos = remember { getKeyboards(context, pairs) }

                    ChooseTaskPage(
                        context = context,
                        melodyToPlay = melodies,
                        playInstrument = VirtualPiano(),
                        onEnd = { success ->
                            if (success)
                                succeedPoints++
                            points++
                            if (i < taskCount - 1) {
                                navController.navigate(screens[i + 1]) {
                                    popUpTo(screens[i + 1])
                                }

                            } else
                                navController.navigate(ScreenNames.RESULTS_SCREEN) {
                                    popUpTo(ScreenNames.RESULTS_SCREEN)
                                }
                        },
                        keyboards = pianos
                    )
                }
            }

        }

    }
}


/** Откладывает интервал от переданной ноты */
private fun getPair(first: Note, interval: Interval, notes: List<Note>): Pair<Note, Note> {
    // Все подходящие ноты (по расстоянию друг от друга),
    // я считаю тупым перебором потому что откладывать расстояние честно слишком сложно
    val suitable = notes.filter { abs(first.pitch - it.pitch) == interval.distance }
    if (suitable.isEmpty())
        throw Exception("Can't find suitable note! Interval and random note don't match")

    return Pair(first, suitable.random())
}

/**
 * @return Список пар, состоящий из нот, находящихся друг от друга на опр-х интервалах
 * */
private fun pairsByIntervals(
    chooseVariants: IntRange,
    fixFirstNote: Boolean,
    noteList: List<Note>,
    possibleIntervals: Array<out Interval>
): Array<Pair<Note, Note>> {
    val variantsCount = chooseVariants.random() // кол-во вариантов выбора
    val shuffledIntervals =
        possibleIntervals.copyOf(); shuffledIntervals.shuffle() // интервалы в случайном порядке
    val pairList = mutableListOf<Pair<Note, Note>>() // список с парами нот

    val fixedNote = noteList.random()

    repeat(variantsCount) {
        // добавляем ноты с границ интервалов
        pairList.add(
            // Каждый интервал содержит общую ноту
            if (fixFirstNote) getPair(
                fixedNote,
                shuffledIntervals[it],
                noteList
            )
            // Либо общих нот может не быть
            else getPair(
                noteList.random(),
                shuffledIntervals[it],
                noteList
            )
        )
    }

    return pairList.toTypedArray()
}

/**
 * Создаёт мелодию из переданных пар нот
 * @param pause Пауза после нот
 */
private fun getMelodies(
    notesFromIntervals: Array<Pair<Note, Note>>,
    fixDirection: Boolean = false,
    pause: NoteDuration = NoteDuration.Half
): Melody {
    val melodyList = mutableListOf<MusicPause>().apply {
        for (notes in notesFromIntervals) {
            // Уточняем порядок нот, если от нас этого требуют
            val first =
                if (fixDirection) (if (notes.first < notes.second) notes.first else notes.second) else notes.first
            val second =
                if (fixDirection) (if (notes.first < notes.second) notes.second else notes.first) else notes.second

            add(MelodyNote(first, duration = NoteDuration.Half))
            add(MelodyNote(second, duration = NoteDuration.Half))
            add(Pause(pause))
        }
        removeLast()
    }
    return Melody(melodyList)
}

/**
 * Создаёт клавиатуры для отрисовки из переданных пар нот
 * */
private fun getKeyboards(
    context: Context,
    notesFromIntervals: Array<Pair<Note, Note>>
): Array<PianoKeyboard> {
    // Округляет вправо
    fun Note.toWholeRight(): Note {
        if (isWhole()) (return this) else (if (isExt()) (return nextSemitone()) else (return previousSemitone()))
    }

    // Округляет влево
    fun Note.toWholeLeft(): Note {
        if (isWhole()) (return this) else (if (isExt()) (return previousSemitone()) else (return nextSemitone()))
    }

    val keyboardsToDraw = mutableListOf<PianoKeyboard>().apply {
        for (notes in notesFromIntervals) {
            // Меняем местами если порядок нарушен (чтобы NoteRange не сломался)
            val first =
                if (notes.first > notes.second) notes.second.toWholeLeft() else notes.first.toWholeLeft()
            val second =
                if (notes.first > notes.second) notes.first.toWholeRight() else notes.second.toWholeRight()
            // Создаём noteRange для фортепиано (по бокам только белые клавиши)
            val range = NoteRange(first, second)
            // Стандартные размеры для фортепианы
            // TODO возможно нужны будут исправления тут
            val size = DpSize((range.wholeNotesCount * 33).dp, 100.dp)
            val keyboard =
                PianoKeyboard(
                    context,
                    size,
                    range
                ).apply { mark(notes.first); mark(notes.second) }

            add(keyboard)
        }
    }

    return keyboardsToDraw.toTypedArray()
}