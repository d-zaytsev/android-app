package com.app.music_app.exercises.pages

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.music_app.music_players.interfaces.AbstractInstrument
import com.app.music_app.view.app_theme.AppColor
import com.app.music_app.view.components.paino_box.PianoCheckbox
import com.app.music_app.view.components.piano_keyboard.PianoKeyboard
import com.app.music_app.view.components.play_button.PlayButton
import com.musiclib.notes.Melody

/**
 * Страница с упражнением (выбор интервалов в порядке их звучания)
 * @param keyboards Клавиатуры для отрисовки (в правильном порядке)
 * @param onPianoClick Действия, необходимые производить при выборе пользователя
 */
@Composable
fun ChooseTaskPage(
    context: Context,
    melodyToPlay: Melody,
    playInstrument: AbstractInstrument,
    onPianoClick: (keyboard: PianoKeyboard, isLast: Boolean) -> Boolean,
    vararg shuffledKeyboards: PianoKeyboard
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.WhiteSmoke),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
        )
        Box(modifier = Modifier.padding(30.dp)) {
            PianoCheckbox(keyboards = shuffledKeyboards, onPianoClick = onPianoClick)
        }
    }
}
