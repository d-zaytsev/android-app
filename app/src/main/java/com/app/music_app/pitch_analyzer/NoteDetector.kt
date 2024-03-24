package com.app.music_app.pitch_analyzer

import android.util.Log
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
import kotlin.concurrent.thread
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

    // ВАЖНО!!! Тут выставлены специальные ограничения на распознаваемые частоты,
    // сделано это с целью повысить распознаваемость уже имеющихся нот!

    // Можно убрать эти границы, но => баги, т.к. звук распадается на другие частоты.
    // Это можно поправить, но ценой времени и производительности.

    // Поток, в котором мы слушаем микрофон и распознаём высоты
    private lateinit var recognizingThread: Thread

    private companion object {
        const val PROBABILITY_LIMIT: Float = 0.9f

        const val MAIN_HZ: Double = 130.81   // Нота, от которой мы считаем
        const val MAIN_OCTAVE_SHIFT = -1    // На сколько октав смещена наша нота До

        const val MIN_HZ: Double = 130.81   // Минимальная распознаваемая нота
        const val MAX_HZ: Double = 987.77   // Максимальная распознаваемая нота
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

                    // Кол-во полутонов, на которое сдвинута нота от MAIN_HZ
                    val relativeSemitones = round(log(pitch / MAIN_HZ, 2.0) * OCTAVE_NOTES)
                    // На какое кол-во октав смещение
                    val relativeOctave = (relativeSemitones / OCTAVE_NOTES).toInt()
                    // Смещение внутри октавы
                    val shiftInOctave =
                        ((relativeSemitones - (relativeOctave * OCTAVE_NOTES)) / 2).round(1)

                    Log.d("AMOGUS", "$pitch $relativeSemitones $relativeOctave $shiftInOctave")

                    // Находим в октаве первую подходящую ноту
                    val noteName =
                        NoteName.entries.firstOrNull { it.value.toDouble() >= shiftInOctave }
                            ?: NoteName.Do

                    val note = if (noteName.value > shiftInOctave)
                        Note(
                            noteName,
                            relativeOctave + MAIN_OCTAVE_SHIFT
                        ).previousSemitone() else Note(noteName, relativeOctave + MAIN_OCTAVE_SHIFT)

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