package com.app.music_app.tasks.pages

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.music_app.music_player.MelodyPlayer
import com.app.music_app.music_player.interfaces.AbstractInstrument
import com.app.music_app.pitch_analyzer.NoteDetector
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.musiclib.notes.Note
import com.musiclib.notes.range.NoteRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Страница с упражнением (откладывание интервала)
 * @param onTouch Вызывается при нажатии на кнопку фортепиано
 */
@Composable
fun CountTaskPage(
    context: Context,
    pianoRange: NoteRange,
    playInstrument: AbstractInstrument,
    onTouch: (note: Note) -> Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val composableScope = rememberCoroutineScope()

        val pianoSize = remember { DpSize((pianoRange.wholeNotesCount * 33).dp, 100.dp) }
        val player = remember { MelodyPlayer(playInstrument) }
        val piano = remember {
            PianoKeyboard(
                context,
                pianoSize,
                pianoRange,
                player
            )
        }

        // Фортепиано
        piano.Draw()

        LaunchedEffect(Unit) {
            composableScope.launch(Dispatchers.IO) {
                NoteDetector().run { note ->
                    if (note != null && pianoRange.inRange(note)) {
                        // Если пользователь нажал на нужную клавишу
                        if (onTouch(note))
                            piano.mark(note)
                        else
                            piano.mark(note, Color.DarkGray)
                    }
                }
            }
        }

    }

}