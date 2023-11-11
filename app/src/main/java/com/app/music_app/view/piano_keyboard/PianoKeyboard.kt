package com.app.music_app.view.piano_keyboard

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.view.text.AutoResizedText
import com.musiclib.notes.Note
import com.musiclib.notes.NoteName
import com.musiclib.notes.NoteRange

class PianoKeyboard(
    private val noteRange: NoteRange,
    private val size: DpSize = DpSize((noteRange.noteCount * 30).dp, 100.dp),
    private val soundOf: ((Note) -> MediaPlayer)? = null
) {

    // Piano key size
    private val whiteKeySize = DpSize(size.width / noteRange.noteCount, size.height)
    private val darkKeySize = DpSize((whiteKeySize.width.value / 3).dp, whiteKeySize.height / 2)

    // Additional key size info
    private val darkKeySide = darkKeySize.width.value / 2
    private val whiteKeyWidth = whiteKeySize.width.value

    // Other keys information
    private val pressedButtonColor = Color(164, 222, 235)

    // Text
    private val textVertPadding = (whiteKeySize.height.value / 10).dp
    private val keyNameFont = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)

    init {
        if (!noteRange.fromNote.isWhole() || !noteRange.endNote.isWhole())
            throw IllegalArgumentException("Can't draw piano keyboard with non whole notes")

    }

    @Composable
    fun Draw() {
        Box(
            modifier = Modifier
                .size(size)
                .background(color = Color.LightGray)
        ) {
            // --- Белые клавиши
            Row(modifier = Modifier.fillMaxSize()) {

                var curNote = noteRange.fromNote
                repeat(noteRange.noteCount) {
                    WhitePianoKey(curNote)
                    curNote = curNote.next()
                }
            }
            // --- Чёрные клавиши
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                var curNote = noteRange.fromNote;
                repeat(noteRange.noteCount) {
                    // Расстояние от правого края прошлой чёрной ноты до левого края этой
                    val spaceSize =
                        if (noteRange.fromNote == curNote && !hasDarkKey(curNote)) whiteKeyWidth
                        else if (noteRange.fromNote != curNote && !hasDarkKey(curNote)) whiteKeyWidth - darkKeySide
                        else if (!hasDarkKey(curNote.previous())) whiteKeyWidth - darkKeySide
                        else if (noteRange.fromNote == curNote) whiteKeyWidth - darkKeySide
                        else whiteKeyWidth - darkKeySide * 2

                    Spacer(modifier = Modifier.width(spaceSize.dp))

                    if (hasDarkKey(curNote) && curNote != noteRange.endNote)
                        DarkPianoKey(curNote)

                    curNote = curNote.next()
                }
            }
            // --- Подписи
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                var curNote = noteRange.fromNote
                repeat(noteRange.noteCount) {
                    Column(
                        modifier = Modifier.size(whiteKeySize),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        PianoKeyName(curNote)
                        curNote = curNote.next()
                    }
                }
            }
        }
    }

    private fun hasDarkKey(note: Note) = note.name != NoteName.Mi && note.name != NoteName.Si

    private fun playSound(note: Note) {
        val sound = soundOf?.invoke(note);

        if (sound != null && sound.isPlaying)
            sound.stop()
        sound?.start()
    }

    @Composable
    fun DarkPianoKey(note: Note) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState().value
        val color = if (isPressed) pressedButtonColor else Color.Black

        Button(
            onClick = { playSound(note.toExt()) },
            interactionSource = interactionSource,
            modifier = Modifier
                .size(darkKeySize),
            shape = PianoKeyShape(5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = color
            )
        ) {}
    }

    @Composable
    fun WhitePianoKey(note: Note) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState().value
        val color = if (isPressed) pressedButtonColor else Color.White

        Button(
            onClick = { playSound(note) },
            interactionSource = interactionSource,
            modifier = Modifier
                .size(whiteKeySize)
                .padding(0.5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
            ),
            shape = PianoKeyShape(15f)
        ) {
        }
    }

    @Composable
    fun PianoKeyName(note: Note) {
        val text = note.name.toString()

        AutoResizedText(
            text = text,
            modifier = Modifier
                .alpha(0.5f)
                .padding((whiteKeyWidth / 4 - text.length).dp, textVertPadding),
            style = keyNameFont,
        )

    }
}