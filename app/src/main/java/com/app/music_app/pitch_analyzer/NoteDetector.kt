package com.app.music_app.pitch_analyzer

import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.musiclib.notes.Note
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

/**
 * @param sampleRate Частота дискретизации (кол-во измерений в секунду)
 * @param audioBufferSize Размер буфера для хранения звука
 * @param bufferOverlap Количество сэмплов, которые перекрываются между последовательными кадрами аудиобуфера
 * @param pitchEstimationAlgorithm Применяемый алгоритм распознавания высоты звука
 */
class NoteDetector(
    private val sampleRate: Int = 22050,
    private val audioBufferSize: Int = 1024,
    private val bufferOverlap: Int = 0,
    private val pitchEstimationAlgorithm: PitchEstimationAlgorithm = PitchEstimationAlgorithm.FFT_YIN
) {
    private lateinit var recognizingThread: Thread

    /**
     * @param onDetection Действия, которые следует выполнять при распознавании очередной ноты
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun start(onDetection: (Note) -> Unit) {
        if (::recognizingThread.isInitialized && recognizingThread.isAlive)
            throw IllegalStateException("Note recognizing already running")
        else if (!recognizingThread.isAlive)
            recognizingThread.start()
        else {
            // ^ У нас первый запуск распознавалки ---

            // Creates AudioDispatcher from configured default microphone,
            // AudioDispatcher - breaks sounds into float arrays. Needs for pitch detectors, audio players, ...
            val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, audioBufferSize, bufferOverlap)

            // PitchDetectionHandler - An interface to handle detected pitch
            val pdh = PitchDetectionHandler { result, _ ->
                // PitchDetectionResult - A class with information about the result of a pitch detection on a block of audio,
                // Contains:
                // 1) pitch in Hz
                // 2) Probability of this pitch
                // 3) A boolean that indicates if the algorithm thinks the signal is pitched or not

                // TODO реализация
            }

            // AudioProcessor: Digital signal processing, a process method that works on an AudioEvent object
            // PitchProcessor: Is responsible to call a pitch estimation algorithm
            val p: AudioProcessor =
                PitchProcessor(
                    pitchEstimationAlgorithm,
                    sampleRate.toFloat(),
                    audioBufferSize,
                    pdh
                )
            // AudioDispatcher (use micro to get sound and work with AudioProcessor)
            // ^ AudioProcessor (More common than PitchProcessor)
            // ^ PitchProcessor (Call pitch estimation alg + use pdh to react)
            // ^ PitchDetectionHandler (Define how to react on pitch)
            dispatcher.addAudioProcessor(p)
            recognizingThread = thread { dispatcher.run() }

        }
    }
}