package com.app.music_app.view.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.app.music_app.pitch_analyzer.NoteRecognizer
import com.app.music_app.view.app_theme.AppColor
import com.app.music_app.view.app_theme.AppTheme
import com.app.music_app.view.components.paino_box.PianoBox
import com.app.music_app.view.components.piano_keyboard.PianoKeyboard
import com.musiclib.notes.Note
import com.musiclib.notes.range.NoteRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Страница с упражнением (откладывание интервала)
 * @param onTouch Вызывается при нажатии на кнопку фортепиано
 */
@Composable
fun IntervalPlayScreen(
    context: Context,
    pianoRange: NoteRange,
    text: AnnotatedString,
    onTouch: (note: Note) -> Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composableScope = rememberCoroutineScope()

        val pianoSize = remember { DpSize((pianoRange.wholeNotesCount * 33).dp, 100.dp) }
        val piano = remember {
            PianoKeyboard(
                context,
                pianoSize,
                pianoRange
            )
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.3f))
        Text(
            text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(AppTheme.size.normal)
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
        // Фортепиано
        PianoBox(piano)

        // Распознавалка нот & отображение на фортепиано
        LaunchedEffect(Unit) {
            composableScope.launch(Dispatchers.IO) {
                NoteRecognizer().run { note ->
                    if (note != null && pianoRange.inRange(note)) {
                        piano.unmark()

                        if (onTouch(note))
                            piano.mark(note, AppColor.Emerald)
                        else
                            piano.mark(note)
                    }
                }
            }
        }

    }

}