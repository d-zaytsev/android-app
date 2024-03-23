package com.app.music_app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root element
        setContent {
            // ЗАПРОС РАЗРЕШЕНИЙ
            if (ContextCompat.checkSelfPermission(
                    this.baseContext,
                    Manifest.permission.RECORD_AUDIO
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    1234
                )
            }

            val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

            val pdh = PitchDetectionHandler { result, _ ->
                val pitchInHz = result.pitch
                Log.d("PITCH", pitchInHz.toString())
            }
            val p: AudioProcessor =
                PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh)
            dispatcher.addAudioProcessor(p)
            Thread(dispatcher, "Audio Dispatcher").start()
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
