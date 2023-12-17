package com.app.music_app.view.piano_keyboard

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.note_player.MelodyPlayer
import com.app.music_app.view.colors.AppColor
import com.app.music_app.view.text.AutoResizedText
import com.example.android_app.R
import com.musiclib.notes.Note
import com.musiclib.notes.data.NoteName
import com.musiclib.NoteRange

/**
 * Класс для взаимодейсвтия пользователя с виртуальной клавиатурой
 * @param noteRange Диапазон нот для отрисовки
 * @param player То, через что ноты будут исполняться
 */
class PianoKeyboard(
    private val context: Context,
    val size: DpSize,
    private val noteRange: NoteRange,
    private val player: MelodyPlayer? = null
) {

    // Piano key size
    private val whiteKeySize = DpSize((size.width) / noteRange.wholeNotesSize, size.height)
    private val darkKeySize = DpSize((whiteKeySize.width / 3), whiteKeySize.height / 2)

    // Additional key size info
    private val darkKeySide = darkKeySize.width.value / 2
    private val whiteKeyWidth = whiteKeySize.width.value

    // Text
    private val textVertPadding = (whiteKeySize.height.value / 10).dp
    private val keyNameFont = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)

    private val pressedWhiteButtonColor: Color = AppColor.LightCyan
    private val pressedBlackButtonColor: Color = AppColor.HonoluluBlue

    // Maps
    private lateinit var colorMap: MutableMap<Note, Color>
    private val nameMap: Map<NoteName, String> = mapOf(
        NoteName.Do to context.getString(R.string.note_name_do),
        NoteName.Re to context.getString(R.string.note_name_re),
        NoteName.Mi to context.getString(R.string.note_name_mi),
        NoteName.Fa to context.getString(R.string.note_name_fa),
        NoteName.Sol to context.getString(R.string.note_name_sol),
        NoteName.La to context.getString(R.string.note_name_la),
        NoteName.Si to context.getString(R.string.note_name_si)
    )

    // мы не можем mark пока элемент не отрисован, поэтому добавляем всё сюда и ждём вызова Draw()
    private val toMarkMap: MutableMap<Note, Color> = mutableMapOf()

    init {
        if (!noteRange.fromNote.isWhole() || !noteRange.endNote.isWhole())
            throw IllegalArgumentException("Can't draw piano keyboard with non whole notes")
    }

    @Composable
    fun Draw() {

        colorMap = remember {
            mutableStateMapOf()
        }

        Box(
            modifier = Modifier
                .size(size)
        ) {
            // --- Белые клавиши
            Row(modifier = Modifier.fillMaxSize()) {

                var curNote = noteRange.fromNote
                repeat(noteRange.wholeNotesSize) {
                    // Для изменения цвета в mark
                    if (colorMap[curNote] == null)
                        colorMap[curNote] = toMarkMap[curNote] ?: Color.White

                    PianoKey(
                        note = curNote, size = whiteKeySize,
                        padding = 0.5.dp,
                        shapeRadius = 15f,
                        color = colorMap[curNote]
                            ?: throw NullPointerException("Can't color key $curNote"),
                        canPress = player != null
                    )
                    curNote = curNote.next()
                }
            }
            // --- Чёрные клавиши
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                var curWhiteNote = noteRange.fromNote;
                repeat(noteRange.wholeNotesSize) {
                    // Расстояние от правого края прошлой чёрной ноты до левого края этой
                    val spaceSize =
                        if (noteRange.fromNote == curWhiteNote && !hasDarkKey(curWhiteNote)) whiteKeyWidth
                        else if (noteRange.fromNote != curWhiteNote && !hasDarkKey(curWhiteNote)) whiteKeyWidth - darkKeySide
                        else if (!hasDarkKey(curWhiteNote.previous())) whiteKeyWidth - darkKeySide
                        else if (noteRange.fromNote == curWhiteNote) whiteKeyWidth - darkKeySide
                        else whiteKeyWidth - darkKeySide * 2

                    Spacer(modifier = Modifier.width(spaceSize.dp))

                    val curNote = curWhiteNote.toExt()

                    if (hasDarkKey(curNote) && curWhiteNote != noteRange.endNote) {
                        // Условие чтобы не рисовать последнюю чёрную клавишу
                        if (colorMap[curNote] == null)
                            colorMap[curNote] = toMarkMap[curNote] ?: Color.Black
                        PianoKey(
                            curNote,
                            darkKeySize,
                            0.dp,
                            5f,
                            color = colorMap[curNote]
                                ?: throw NullPointerException("Can't color key $curNote"),
                            player != null
                        )
                    }

                    curWhiteNote = curWhiteNote.next()
                }
            }
            // --- Подписи
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                var curNote = noteRange.fromNote
                repeat(noteRange.wholeNotesSize) {
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

    /**
     * Активирует клавишу
     * @throws IllegalArgumentException
     */
    @Composable
    fun mark(note: Note, markColor: Color? = null) {
        if (!noteRange.inRange(note))
            throw IllegalArgumentException("Can't mark such note, it doesn't exist in piano")

        val color = markColor ?: if (note.isWhole()) pressedWhiteButtonColor else pressedBlackButtonColor
        toMarkMap[note] = color
        if (::colorMap.isInitialized) {
            LaunchedEffect(colorMap) {
                colorMap[note] = color
            }
        }
    }

    /**
     * Возвращает клавишу в обычный цвет
     * @throws IllegalArgumentException
     */
    @Composable
    fun unmark(note: Note) {
        if (!noteRange.inRange(note))
            throw IllegalArgumentException("Can't mark such note, it doesn't exist in piano")

        toMarkMap.remove(note)
        if (::colorMap.isInitialized)
        {
            LaunchedEffect(null) {
                colorMap[note] = if (note.isWhole()) Color.White else Color.Black
            }
        }
    }

    /**
     * Рисует клавишу с указанными свойствами
     * */
    @Composable
    private fun PianoKey(
        note: Note,
        size: DpSize,
        padding: Dp,
        shapeRadius: Float,
        color: Color,
        canPress: Boolean = true
    ) {

        CompositionLocalProvider(LocalRippleTheme provides PianoRippleTheme(if (note.isWhole()) pressedWhiteButtonColor else pressedBlackButtonColor)) {
            Button(
                enabled = canPress,
                onClick = {
                    playSound(note)
                },
                modifier = Modifier
                    .size(size)
                    .padding(padding),
                shape = PianoKeyShape(shapeRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    disabledContainerColor = color
                )
            ) {}
        }
    }

    /**
     * Отрисовывает названия для клавиш
     * */
    @Composable
    private fun PianoKeyName(note: Note) {
        val text =
            nameMap[note.name] ?: throw IllegalArgumentException("Can't draw name for such note")

        val pad = if (text.length == 1)
            (whiteKeyWidth / 3).dp
        else
            (whiteKeyWidth / 4 - text.length).dp

        AutoResizedText(
            text = text,
            modifier = Modifier
                .alpha(0.5f)
                .padding(pad, textVertPadding),
            style = keyNameFont,
        )

    }

    private fun hasDarkKey(note: Note) = note.name != NoteName.Mi && note.name != NoteName.Si

    private fun playSound(note: Note) {
        val sound = player?.soundOf(context, note);

        if (sound != null && sound.isPlaying)
            sound.release()
        sound?.start()
    }
}