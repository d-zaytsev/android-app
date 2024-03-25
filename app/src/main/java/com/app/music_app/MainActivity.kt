package com.app.music_app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.music_app.pitch_analyzer.NoteDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private fun checkAudio(context: Context, activity: Activity): Boolean {
        // Context - bridge between our app and the Android System,
        // контекст текущего состояния приложения или объекта. Пзволяет получать инф-ию об окружении,
        // Activity наследуется от контекста. .getContext()
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
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                checkAudio(LocalContext.current, this@MainActivity)

                var text by remember {
                    mutableStateOf("0f")
                }

                Text(
                    text,
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    fontSize = 50.sp
                )

                val composableScope = rememberCoroutineScope()

                // LaunchedEffect - создаёт CoroutineScope,
                // вместо Unit поставить какой-нибудь счётчик экранов
                LaunchedEffect(Unit) {
                    composableScope.launch(Dispatchers.IO) {
                        NoteDetector().run { note ->
                            if (note != null)
                                text = note.toString()
                        }
                    }
                }

            }
        }
    }

//    CompareTaskManager(
//    context = LocalContext.current,
//    range = NoteRange(Note(NoteName.Do), Note(NoteName.Mi)),
//    chooseVariants = 2..2,
//    taskCount = 5,
//    fixFirstNote = true,
//    fixDirection = true,
//    Interval(Note(NoteName.Do), Note(NoteName.Re)),
//    Interval(Note(NoteName.Do), Note(NoteName.Do, sign = Alteration.SharpSign))
//    )
}
