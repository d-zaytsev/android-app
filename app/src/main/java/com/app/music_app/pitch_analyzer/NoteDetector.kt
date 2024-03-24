package com.app.music_app.pitch_analyzer

import android.util.Log
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.Alteration
import com.musiclib.notes.note_metadata.NoteName
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.round

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

    // Поток, в котором мы слушаем микрофон и распознаём высоты
    private lateinit var recognizingThread: Thread
    private companion object {
        const val PROBABILITY_LIMIT: Float = 0.9f
        const val C4HZ: Double = 261.63 // Нота До главной октавы в Гц
        const val MIN_HZ: Double = 123.47 // Минимальная распознаваемая нота
        const val MAX_HZ: Double = 1046.5 // Максимальная распознаваемая нота
        const val OCTAVE_NOTES: Double = 12.0
    }

    /**
     * @param onDetection Действия, которые следует выполнять при распознавании очередной ноты
     */
    fun start(onDetection: (Note?) -> Unit) {
        if (::recognizingThread.isInitialized && recognizingThread.isAlive)
            throw IllegalStateException("Note recognizing already running")
        else if (::recognizingThread.isInitialized && !recognizingThread.isAlive)
            recognizingThread.start()
        else {
            // ^ У нас первый запуск распознавалки ---

            // Creates AudioDispatcher from configured default microphone,
            // AudioDispatcher - breaks sounds into float arrays. Needs for pitch detectors, audio players, ...
            val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(
                sampleRate,
                audioBufferSize,
                bufferOverlap
            )

            // PitchDetectionHandler - An interface to handle detected pitch
            val pdh = PitchDetectionHandler { result, _ ->
                // PitchDetectionResult - A class with information about the result of a pitch detection on a block of audio,
                // Contains:
                // 1) pitch in Hz
                // 2) Probability of this pitch
                // 3) A boolean that indicates if the algorithm thinks the signal is pitched or not
                val pitch = result.pitch.round(2)

                if (pitch in MIN_HZ..MAX_HZ && result.probability >= PROBABILITY_LIMIT && result.isPitched) {
                    // https://producelikeapro.com/blog/note-frequency-chart/

                    // Кол-во полутонов, на которое сдвинута нота от До главной октавы
                    val semitones = round(log(pitch / C4HZ, 2.0) * OCTAVE_NOTES).toInt()
                    // Смещение вправо или влево
                    val right = semitones >= 0
                    // На какое кол-во октав смещение
                    val octaves =
                        (if (right) (semitones / OCTAVE_NOTES).toInt() else (semitones / OCTAVE_NOTES).toInt()).toInt()
                    // Смещение внутри октавы
                    val octaveShift =
                        (if (right) (semitones - (octaves * OCTAVE_NOTES)) / 2 else
                            (OCTAVE_NOTES - (abs(semitones) - ((abs(octaves)) * OCTAVE_NOTES))) / 2)
                            .round(2)

                    Log.d("AMOGUS", "$pitch $semitones $octaves $octaveShift")

                    // Находим в октаве первую подходящую ноту
                    val noteName =
                        NoteName.entries.firstOrNull { it.value.toDouble() >= octaveShift }
                            ?: NoteName.Do

                    val note = if (noteName.value > octaveShift) Note(
                        noteName,
                        octaves
                    ).previousSemitone() else Note(noteName, octaves)

                    onDetection(note)
                } else {
                    onDetection(null)
                }
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

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
    private fun Float.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}