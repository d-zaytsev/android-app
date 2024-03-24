package com.app.music_app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor


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

                Text(text, modifier = Modifier.fillMaxSize(), color = Color.White, fontSize = 50.sp)

                // Creates AudioDispatcher from configured default microphone,
                // AudioDispatcher - breaks sounds into float arrays. Needs for pitch detectors, audio players, ...
                val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

                // PitchDetectionHandler - An interface to handle detected pitch
                // https://kotlinlang.org/docs/fun-interfaces.html
                val pdh = PitchDetectionHandler { result, _ ->
                    // PitchDetectionResult - A class with information about the result of a pitch detection on a block of audio,
                    // Contains:
                    // 1) pitch in Hz
                    // 2) Probability of this pitch
                    // 3) A boolean that indicates if the algorithm thinks the signal is pitched or not
                    text = result.pitch.toString()
                }

                // AudioProcessor: Digital signal processing, a process method that works on an AudioEvent object
                // PitchProcessor: Is responsible to call a pitch estimation algorithm
                val p: AudioProcessor =
                    PitchProcessor(
                        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
                        22050f,
                        1024,
                        pdh
                    )
                // AudioDispatcher (use micro to get sound and work with AudioProcessor)
                // ^ AudioProcessor (More common than PitchProcessor)
                // ^ PitchProcessor (Call pitch estimation alg + use pdh to react)
                // ^ PitchDetectionHandler (Define how to react on pitch)
                dispatcher.addAudioProcessor(p)
                Thread(dispatcher, "Audio Dispatcher").start()
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
