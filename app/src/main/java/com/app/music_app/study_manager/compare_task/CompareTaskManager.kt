package com.app.music_app.study_manager.compare_task

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
import com.app.music_app.note_player.MelodyPlayer
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
import com.musiclib.notes.data.NoteName
import com.musiclib.notes.interfaces.MusicPause
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

@Composable
        /**
         * Выполняет настройку задания и проводит его
         * @param range Диапазон нот, в котором разрешено выбирать интервалы
         * @param possibleIntervals Какие интервалы будут в упражнении
         * @param variants Кол-во вариантов выбора
         * @param taskCount Кол-во заданий
         * */
fun CompareTaskManager(
    context: Context,
    range: NoteRange,
    variants: IntRange,
    taskCount: Int,
    vararg possibleIntervals: Interval
) {
    if (possibleIntervals.size < 2)
        throw IllegalArgumentException("Can't use less than 2 intervals")
    if (range.wholeNotesSize < 3)
        throw IllegalArgumentException("Can't such small note range")
    if (taskCount < 1)
        throw IllegalArgumentException("Can't be less than 1 task")
    if (variants.last - variants.first < 2)
        throw IllegalArgumentException("Can't be less than 2 variants")

    Column {

        var points by remember { mutableStateOf(1) }
        TaskProgressBar(points, taskCount)

        val navController = rememberNavController() // С помощью него переключаемся

        NavHost( // Содержит все экраны
            navController = navController,
            startDestination = "task0"
        ) {
            // Создаём экраны один за одним
            for (i in 0 until taskCount) {
                // генерируем задание

                val randomNote = range.wholeNotes.random()
                val randomInterval = possibleIntervals.random()
                val pairList = mutableListOf<Pair<Note, Note>>()
                val variantsCount = variants.random()

                repeat(variantsCount) {
                    val notePair = Pair(
                        randomNote,
                        SecondNote(randomNote, randomInterval, Random.nextBoolean())
                    )
                    pairList.add(notePair)
                }

                val melody = Melody(pairList.toTypedArray())
                val keyboards = Keyboards(context, pairList.toTypedArray())

                composable("task$i") {
                    CompareTaskPage(
                        context = context,
                        melodyToPlay = melody,
                        playInstrument = VirtualPiano(),
                        onEnd = {
                            points++
                            navController.navigate("task${(i + 1)}") {
                                popUpTo("task$i") // удаляем из стека
                            }
                        },
                        keyboards = keyboards
                    )
                }
            }
        }
    }
}

private fun SecondNote(note: Note, interval: Interval, right: Boolean): Note {
    val value = if (right) interval.distance + note.pitch else note.pitch - interval.distance
    return note.add(value)
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
        val range = NoteRange(notePair.first.previous().toWhole(), notePair.second.next().toWhole())
        val size = DpSize((range.wholeNotesSize * 33).dp, 100.dp)
        keyboadsList.add(PianoKeyboard(context, size, range))
    }

    return keyboadsList.toTypedArray()
}

private fun Note.toWhole() = Note(this.name, this.octave, Alteration.None)