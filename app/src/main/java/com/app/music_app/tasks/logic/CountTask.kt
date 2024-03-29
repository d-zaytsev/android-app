package com.app.music_app.tasks.logic

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import com.app.music_app.tasks.pages.CountTaskPage
import com.app.music_app.view.progress_bar.TaskProgressBar
import com.musiclib.intervals.Interval
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange

/**
 * Выполняет настройку задания и проводит его
 * @param range Диапазон нот, в котором разрешено выбирать интервалы и распознавать звуки
 * @param possibleIntervals Какие интервалы будут в упражнении
 * @param taskCount Кол-во заданий
 * @param maxErrorCount Кол-во ошибок, которое максимум можно совершить за каждое задание
 * */
@Composable
fun CountTask(
    context: Context,
    activity: Activity,
    range: NoteRange,
    taskCount: Int,
    maxErrorCount: Int,
    vararg possibleIntervals: Interval
) {
    if (!checkAudioPermission(context, activity))
        throw IllegalStateException("Can't get voice recording permission")

    var succeedPoints by remember { mutableFloatStateOf(0f) }
    var points by remember { mutableFloatStateOf(0f) }
    val maxProgress = remember { taskCount * maxErrorCount.toFloat() }

    var errorAttempts by remember { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.fillMaxSize()) {
        TaskProgressBar(points = succeedPoints, progress = points, maxProgress = maxProgress)

        CountTaskPage(
            context,
            range,
            "Play note La"
        )
        { note ->
            // Определение правильности нажатия
            if (note == Note(NoteName.La)) {
                points += errorAttempts
                succeedPoints += maxErrorCount - errorAttempts
                errorAttempts = 0f

                return@CountTaskPage true
            } else {
                if (errorAttempts < maxErrorCount) {
                    errorAttempts++
                }
                return@CountTaskPage false
            }
        }
    }
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