package com.app.music_app.exercises.logic

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.music_players.instruments.VirtualPiano
import com.app.music_app.view.screens.ChooseTaskScreen
import com.app.music_app.view.screens.ResultsScreen
import com.app.music_app.view.components.piano_keyboard.PianoKeyboard
import com.app.music_app.view.components.progress_bars.TaskProgressBar
import com.musiclib.notes.range.NoteRange
import com.musiclib.intervals.Interval
import com.musiclib.notes.Melody
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.Pause
import com.musiclib.notes.note_metadata.NoteDuration
import com.musiclib.notes.interfaces.MusicPause
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Упражнение на сопоставление звука и интервала
 * @param range Диапазон нот, в котором разрешено выбирать интервалы
 * @param possibleIntervals Какие интервалы будут в упражнении
 * @param taskCount Кол-во заданий
 * @param fixFirstNote Если true, то все интервалы будут содержать общую одной ноты
 * @param fixDirection Если true, то интервал будут звучать только по возрастанию нот
 * */
class CompareExercise(
    private val context: Context,
    private val range: NoteRange,
    private val taskCount: Int = 5,
    private val fixFirstNote: Boolean = true,
    private val fixDirection: Boolean = true,
    private val possibleIntervals: Array<Interval>
) : AbstractExercise() {

    init {
        require(possibleIntervals.size >= 2) { "Can't use less than 2 intervals" }
        require(range.wholeNotesCount >= 3) { "Can't such small note range" }
        require(taskCount > 0) { "Can't be less than 1 task" }
        require(possibleIntervals.maxOf { it.distance } >= range.start.pitch - range.endInclusive.pitch) { "You choose too small range for such intervals" }
    }

    private val noteList = range.toList()

    @Composable
    override fun run() {
        var succeedPoints by remember { mutableFloatStateOf(0f) }   // Кол-во верно сделанных заданий
        var points by remember { mutableFloatStateOf(0f) }          // Общее кол-во пройденных заданий

        Column {
            TaskProgressBar(succeedPoints, points, taskCount.toFloat())

            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()

            NavHost(navController, START_SCREEN) {
                // Экран с результатами
                composable(RESULTS_SCREEN) { ResultsScreen(succeedPoints.toInt(), taskCount) }

                repeat(taskCount) { screenId ->
                    composable(screenNameOf(screenId)) {
                        // Получаем данные для отрисовки страницы
                        val pairs = remember {
                            pairsByIntervals(
                                fixFirstNote,
                                noteList,
                                possibleIntervals
                            )
                        }
                        val melodies = remember { getMelodies(pairs, fixDirection) }
                        val pianos = remember { getKeyboards(context, pairs) }
                        val shuffledPianos = remember { pianos.clone().also { it.shuffle() } }
                        var curPiano by remember { mutableIntStateOf(0) }

                        ChooseTaskScreen(
                            context = context,
                            melodyToPlay = melodies,
                            playInstrument = VirtualPiano(),
                            shuffledKeyboards = shuffledPianos,
                            onPianoClick = { clickedPiano, _ ->
                                points++

                                val correctVariant = clickedPiano == pianos[curPiano]
                                if (correctVariant)
                                    succeedPoints++

                                curPiano++

                                coroutineScope.launch {
                                    delay(1500)
                                    if (screenId == taskCount - 1) {
                                        navController.navigate(RESULTS_SCREEN) {
                                            popUpTo(RESULTS_SCREEN)
                                        }
                                    } else {
                                        navController.navigate(screenNameOf(screenId.inc())) {
                                            popUpTo(screenNameOf(screenId.inc()))
                                        }
                                    }
                                }


                                // Красит в нужный цвет
                                return@ChooseTaskScreen correctVariant
                            }
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
        fixFirstNote: Boolean,
        noteList: List<Note>,
        possibleIntervals: Array<out Interval>
    ): Array<Pair<Note, Note>> {
        val shuffledIntervals = possibleIntervals.copyOf().also { it.shuffle() }
        val pairList = mutableListOf<Pair<Note, Note>>() // список с парами нот

        val fixedNote = noteList.random()

        repeat(2) {
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

}