package com.app.music_app.study_manager.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.music_app.note_player.interfaces.AbstractInstrument
import com.app.music_app.view.colors.AppColor
import com.app.music_app.view.paino_box.PianoCheckbox
import com.app.music_app.view.piano_keyboard.PianoKeyboard
import com.app.music_app.view.play_button.PlayButton
import com.musiclib.notes.Melody
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Страница с упражнением
 * @param keyboards Клавиатуры в верном порядке
 * @param keyboards Клавиатуры для отрисовки (в правильном порядке)
 */
@Composable
fun CompareTaskPage(
    context: Context,
    melodyToPlay: Melody,
    playInstrument: AbstractInstrument,
    onEnd: (isSuccess: Boolean) -> Unit,
    vararg keyboards: PianoKeyboard
) {

    require(keyboards.size >= 2) { "Can't draw less than 2 intervals" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.WhiteSmoke),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Наполнение страницы ---
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        )
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(300.dp)
        ) {
            PlayButton(context = context, melody = melodyToPlay, instrument = playInstrument)
        }

        var success by remember { mutableStateOf(true) } // Текущий верный индекс
        var isNextVisible by remember { mutableStateOf(false) }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
        )

        var curInd by remember { mutableIntStateOf(0) } // Текущий верный индекс

        val shuffledKeyboards =
            remember { keyboards.clone().also { it.shuffle() } } // Чтобы порядок был непредсказуем

        Box(modifier = Modifier.padding(30.dp)) {
            // Через время переходим на следующий экран
            val coroutineScope = rememberCoroutineScope()

            PianoCheckbox(keyboards = shuffledKeyboards, onPianoClick =
            { keyboard, isLast ->
                val i = keyboards.indexOf(keyboard)
                if (i == curInd) {
                    // * Правильный ответ *
                    curInd++

                } else {
                    // * Неправильный ответ *
                    success = false
                }

                if (isLast) {
                    coroutineScope.launch {
                        delay(1500)
                        onEnd(success)
                    }
                }

            })
        }
    }
}
