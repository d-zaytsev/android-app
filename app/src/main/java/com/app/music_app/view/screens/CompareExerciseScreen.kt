package com.app.music_app.view.screens

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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.app.music_app.music_players.interfaces.AbstractInstrument
import com.app.music_app.view.app_theme.AppTheme
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
fun CompareExerciseScreen(
    context: Context,
    melodyToPlay: Melody,
    playInstrument: AbstractInstrument,
    onPianoClick: (keyboard: PianoKeyboard, isLast: Boolean) -> Boolean,
    vararg shuffledKeyboards: PianoKeyboard
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlayButton(
            modifier = Modifier
                .fillMaxHeight(0.08f)
                .fillMaxWidth(if (Locale.current.language == "ru") 0.6f else 0.5f),
            context = context,
            melody = melodyToPlay,
            instrument = playInstrument
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

        PianoCheckbox(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .fillMaxHeight(0.6f)
                .fillMaxWidth(0.9f),
            keyboards = shuffledKeyboards,
            onPianoClick = onPianoClick
        )
    }
}
