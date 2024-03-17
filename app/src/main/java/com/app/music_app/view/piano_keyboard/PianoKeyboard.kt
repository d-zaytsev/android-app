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
import com.musiclib.notes.note_metadata.NoteName
import com.musiclib.notes.range.NoteRange
import com.musiclib.notes.note_metadata.Alteration

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
    // Кол-во целых нот в диапазоне
    private val wholeNotesCount = noteRange.wholeNotesCount

    // Размеры клавиш
    private val whiteKeySize = DpSize((size.width) / wholeNotesCount, size.height)
    private val darkKeySize = DpSize((whiteKeySize.width / 3), whiteKeySize.height / 2)
    private val darkKeySide = darkKeySize.width.value / 2
    private val whiteKeyWidth = whiteKeySize.width.value

    // Text
    private val textVertPadding = (whiteKeySize.height.value / 10).dp
    private val keyNameFont = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)

    private val pressedWhiteButtonColor: Color = AppColor.LightCyan
    private val pressedBlackButtonColor: Color = AppColor.HonoluluBlue

    // Maps
    private var colorMap: MutableMap<Note, Color>

    private val nameMap: Map<NoteName, String> = mapOf(
        NoteName.Do to context.getString(R.string.note_name_do),
        NoteName.Re to context.getString(R.string.note_name_re),
        NoteName.Mi to context.getString(R.string.note_name_mi),
        NoteName.Fa to context.getString(R.string.note_name_fa),
        NoteName.Sol to context.getString(R.string.note_name_sol),
        NoteName.La to context.getString(R.string.note_name_la),
        NoteName.Si to context.getString(R.string.note_name_si)
    )

    init {
        require(noteRange.start.isWhole()) { "Can't draw piano keyboard with dark keys ob left border" }
        require(noteRange.endInclusive.isWhole()) { "Can't draw piano keyboard with dark keys ob right border" }

        colorMap = mutableStateMapOf()
    }

    @Composable
    fun Draw() {
        val keys = remember { noteRange.toList() }
        val whiteKeys = remember { keys.filter { it.isWhole() } }
        val blackKeys = remember { keys.filter { !it.isWhole() } }

        Box(
            modifier = Modifier
                .size(size)
        ) {
            // --- Белые клавиши
            Row(modifier = Modifier.fillMaxSize()) {
                for (whiteNote in whiteKeys) {
                    // Для изменения цвета в mark
                    colorMap[whiteNote] = colorMap[whiteNote] ?: Color.White

                    PianoKey(
                        note = whiteNote, size = whiteKeySize,
                        padding = 0.5.dp, // Расстояние между клавишами
                        shapeRadius = 15f,
                        color = colorMap[whiteNote]
                            ?: throw NullPointerException("Can't color key $whiteNote"),
                        canPress = player != null
                    )
                }
            }
            // --- Чёрные клавиши
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                var curWhiteNote = noteRange.start
                repeat(wholeNotesCount) {
                    // Расстояние от правого края прошлой чёрной ноты до левого края этой
                    val spaceSize =
                        if (noteRange.start == curWhiteNote && !hasDarkKey(curWhiteNote)) whiteKeyWidth
                        else if (noteRange.start != curWhiteNote && !hasDarkKey(curWhiteNote)) whiteKeyWidth - darkKeySide
                        else if (!hasDarkKey(curWhiteNote.previousWhole())) whiteKeyWidth - darkKeySide
                        else if (noteRange.start == curWhiteNote) whiteKeyWidth - darkKeySide
                        else whiteKeyWidth - darkKeySide * 2

                    Spacer(modifier = Modifier.width(spaceSize.dp))

                    val curNote = Note(
                        curWhiteNote.name,
                        octave = curWhiteNote.octave,
                        sign = Alteration.SharpSign
                    )

                    if (hasDarkKey(curNote) && curWhiteNote != noteRange.endInclusive) {
                        // Условие чтобы не рисовать последнюю чёрную клавишу
                        colorMap[curNote] = colorMap[curNote] ?: Color.Black
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

                    curWhiteNote = curWhiteNote.nextWhole()
                }
            }
            // --- Подписи
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                var curNote = noteRange.start
                repeat(wholeNotesCount) {
                    Column(
                        modifier = Modifier.size(whiteKeySize),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        PianoKeyName(curNote)
                        curNote = curNote.nextWhole()
                    }
                }
            }
        }
    }

    /**
     * Активирует клавишу
     * @throws IllegalArgumentException
     */
    fun mark(note: Note, markColor: Color? = null) {
        require(noteRange.inRange(note)) { "Can't mark such note, it doesn't exist in piano" }

        val color =
            markColor ?: if (note.isWhole()) pressedWhiteButtonColor else pressedBlackButtonColor

        colorMap[note] = color // Если не будет работать добавь LaunchedEffect 🤑
    }

    /**
     * Возвращает клавишу в обычный цвет
     * @throws IllegalArgumentException
     */
    fun unmark(note: Note) {
        require(noteRange.inRange(note)) { "Can't mark such note, it doesn't exist in piano" }

        colorMap[note] = if (note.isWhole()) Color.White else Color.Black
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

        // Применяем тему эффекта нажатия (зависит от изначального типа клавиши)
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