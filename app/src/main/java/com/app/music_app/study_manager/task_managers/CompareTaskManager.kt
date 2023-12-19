package com.app.music_app.study_manager.task_managers

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.music_app.note_player.instruments.VirtualPiano
import com.app.music_app.study_manager.pages.CompareTaskPage
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.app.music_app.view.task_progress_indicator.TaskProgressBar
import com.musiclib.NoteRange
import com.musiclib.intervals.Interval
import com.musiclib.notes.Melody
import com.musiclib.notes.MelodyNote
import com.musiclib.notes.Note
import com.musiclib.notes.Pause
import com.musiclib.notes.data.Alteration
import com.musiclib.notes.data.NoteDuration
import com.musiclib.notes.interfaces.MusicPause
import kotlin.math.abs

/**
 * Выполняет настройку задания и проводит его
 * @param range Диапазон нот, в котором разрешено выбирать интервалы
 * @param possibleIntervals Какие интервалы будут в упражнении
 * @param chooseVariants Кол-во вариантов выбора
 * @param taskCount Кол-во заданий
 * @param fixFirstNote Нужно ли фиксировать ноту при поиске интервала
 * */
@Composable
fun CompareTaskManager(
    context: Context,
    range: NoteRange,
    chooseVariants: IntRange,
    taskCount: Int,
    fixFirstNote: Boolean = true,
    vararg possibleIntervals: Interval
) {
    // Ограничения на аргументы
    if (possibleIntervals.size < 2)
        throw IllegalArgumentException("Can't use less than 2 intervals")
    if (range.notes.size < 3)
        throw IllegalArgumentException("Can't such small note range")
    if (taskCount < 1)
        throw IllegalArgumentException("Can't be less than 1 task")
    if (possibleIntervals.size < chooseVariants.last - chooseVariants.first)
        throw IllegalArgumentException("Need more intervals to choose")
    if (possibleIntervals.maxOf { it.distance } < range.fromNote.pitch - range.endNote.pitch)
        throw IllegalArgumentException("You choose too small range for such intervals")

    Column {

        var points by remember { mutableStateOf(0) } // Кол-во верно сделанных заданий
        TaskProgressBar(points, taskCount)                 // Прогресс сверху

        val navController = rememberNavController()         // С помощью него переключаемся

        val resPair = generateTasks(                        // Генерируем все мелодии и клавиатуры
            context = context,
            variants = chooseVariants,
            intervals = possibleIntervals,
            fixFirstNote = fixFirstNote,
            range = range,
            taskCount = taskCount
        )

        val melodies = resPair.first
        val pianos = resPair.second

        NavHost( // Содержит все экраны
            navController = navController,
            startDestination = "task0"
        ) {

            for (i in 0 until taskCount) {
                composable("task$i") {
                    CompareTaskPage(
                        context = context,
                        melodyToPlay = melodies[i],
                        playInstrument = VirtualPiano(),
                        onEnd = {
                            points++
                            navController.navigate("task${(i + 1)}")
                        },
                        keyboards = pianos[i]
                    )
                }
            }

        }
    }
}

private fun generateTasks(
    context: Context,
    variants: IntRange,
    fixFirstNote: Boolean,
    range: NoteRange,
    taskCount: Int,
    vararg intervals: Interval
): Pair<Array<Melody>, Array<Array<PianoKeyboard>>> {
    val melodyList = mutableListOf<Melody>()
    val pianoList = mutableListOf<Array<PianoKeyboard>>()
    repeat(taskCount) {
        // генерируем задание
        val variantsCount = variants.random()
        val pairList = mutableListOf<Pair<Note, Note>>()
        val pastIntervals = mutableListOf<Interval>()

        repeat(variantsCount) {
            val randomInterval =
                intervals.filterNot { pastIntervals.contains(it) }.random()
            pastIntervals.add(randomInterval)

            val notePair =
                if (fixFirstNote) getPair(range.notes.random(), randomInterval, range) else getPair(
                    randomInterval,
                    range
                )

            pairList.add(notePair)
        }

        melodyList.add(Melody(pairList.toTypedArray()))
        pianoList.add(Keyboards(context, pairList.toTypedArray()))
    }

    return Pair(melodyList.toTypedArray(), pianoList.toTypedArray())
}

private fun getPair(first: Note, interval: Interval, range: NoteRange): Pair<Note, Note> {
    val second = range.notes.filter { abs(first.pitch - it.pitch) == interval.distance }.random()

    return Pair(first, second)
}

private fun getPair(interval: Interval, range: NoteRange): Pair<Note, Note> {
    var first = range.fromNote.previous()
    var second = range.endNote.next()
    while (!range.inRange(first) || !range.inRange(second)) {
        first = range.notes.random()
        // Находим все возможные подходящие по интервалу ноты и берём любую
        second = range.notes.filter { abs(first.pitch - it.pitch) == interval.distance }.random()
    }
    return Pair(first, second)
}

/**
 * Создаёт мелодию из переданных пар нот
 */
private fun Melody(
    notesFromIntervals: Array<Pair<Note, Note>>
): Melody {
    val melodyList = mutableListOf<MusicPause>()
    for (notes in notesFromIntervals) {
        // Добавляем интерва
        melodyList.add(MelodyNote(notes.first, duration = NoteDuration.Half))
        melodyList.add(MelodyNote(notes.second, duration = NoteDuration.Half))
        melodyList.add(Pause(NoteDuration.Half))
    }
    // Последняя пауза лишняя
    melodyList.removeLast()

    return Melody(melodyList)
}

/**
 * Создаёт клавиатуры для отрисовки из переданных пар нот
 * */
private fun Keyboards(
    context: Context,
    notesFromIntervals: Array<Pair<Note, Note>>
): Array<PianoKeyboard> {
    val keyboadsList = mutableListOf<PianoKeyboard>()
    for (notePair in notesFromIntervals) {
        // Генерируются не всегда в нужном порядке
        val first = if (notePair.first < notePair.second) notePair.first else notePair.second
        val second = if (notePair.first < notePair.second) notePair.second else notePair.first

        // Создаём noteRange для фортепиано (по бокам только белые клавиши)
        val range = NoteRange(
            if (!first.isWhole())
                first.previous().toWhole()
            else first,
            if (!second.isWhole())
                second.next().toWhole()
            else second
        )
        val size = DpSize((range.notes.filter { it.isWhole() }.size * 33).dp, 100.dp)

        val keyboard = PianoKeyboard(context, size, range)
        keyboard.mark(notePair.first); keyboard.mark(notePair.second)

        keyboadsList.add(keyboard)
    }

    return keyboadsList.toTypedArray()
}

private fun Note.toWhole() = Note(this.name, this.octave, Alteration.None)