package com.app.music_app.pitch_analyzer

import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName
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

    // # ---------------------------------------------------------------------------- #
    //  ВАЖНО!!! Тут выставлены специальные ограничения на распознаваемые частоты,
    //  сделано это с целью повысить распознаваемость уже имеющихся нот!

    //  Можно убрать эти границы, но => баги, т.к. звук распадается на другие частоты.
    //  Это можно поправить, но ценой времени и производительности.
    // # ---------------------------------------------------------------------------- #

    // Creates AudioDispatcher from configured default microphone,
    // AudioDispatcher - breaks sounds into float arrays. Needs for pitch detectors, audio players, ...
    private val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(
        sampleRate,
        audioBufferSize,
        bufferOverlap
    )

    /**
     * Запоминаем часто нажимаемые ноты
     * */
    private val cache = mutableMapOf<Int,Note> ()

    private companion object {
        const val PROBABILITY_LIMIT: Float = 0.95f

        const val START_NOTE_HZ: Double = 130.81    // Нота, от которой мы считаем
        const val OCTAVE_SHIFT = -1                 // На сколько октав смещена наша нота До

        const val MIN_HZ: Double = 130.81           // Минимальная распознаваемая нота
        const val MAX_HZ: Double = 987.77           // Максимальная распознаваемая нота

        const val OCTAVE_SEMITONES: Int = 12        // Кол-во полутонов в октаве

        const val CACHE_LIMIT: Int = 12             // Лимит на размер кэша
    }

    /**
     * Запускает распознавание нот
     * @param onDetection Действия, которые следует выполнять при распознавании очередной ноты
     */
    suspend fun run(onDetection: (Note?) -> Unit) {
        // PitchDetectionHandler - An interface to handle detected pitch
        val pdh = PitchDetectionHandler { result, _ ->
            // PitchDetectionResult - A class with information about the result of a pitch detection on a block of audio,
            // Contains:
            // 1) pitch in Hz
            // 2) Probability of this pitch
            // 3) A boolean that indicates if the algorithm thinks the signal is pitched or not
            val pitch = result.pitch.round(2)

            // Некоторые *специальные* ограничения на распознаваемый сигнал
            if (pitch in MIN_HZ..MAX_HZ && result.probability >= PROBABILITY_LIMIT && result.isPitched) {
                // https://producelikeapro.com/blog/note-frequency-chart/ <-- табличка с частотами удобная

                // Кол-во полутонов, на которое сдвинута нота
                val relativeSemitones = pitch.semitoneShift()
                val cacheResult = cache[relativeSemitones]

                // Если в кэше такой интервал ещё сохранён не был
                if (cacheResult == null) {
                    // На какое кол-во октав смещение
                    val relativeOctave = relativeSemitones / OCTAVE_SEMITONES

                    // Смещение внутри октавы ( 1 = тон, 0.5 = полутон )
                    // Логика из NoteName
                    val shiftInOctave =
                        ((relativeSemitones % OCTAVE_SEMITONES) / 2.0f).format()

                    // Находим в октаве первую подходящую ноту
                    val noteName =
                        NoteName.entries.firstOrNull { it.value >= shiftInOctave }
                            ?: throw IllegalStateException("Can't recognize pitch")

                    // Находим связанную ноту
                    val note =
                        if (noteName.value > shiftInOctave)
                            Note(noteName, relativeOctave + OCTAVE_SHIFT).previousSemitone()
                        else
                            Note(noteName, relativeOctave + OCTAVE_SHIFT)

                    // Следим чтобы не выходить за рамки
                    if (cache.size == CACHE_LIMIT)
                        cache.remove((0 until cache.size).random())

                    cache[relativeSemitones] = note
                    onDetection(note)
                } else {
                    onDetection(cacheResult)
                }
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
        dispatcher.run()
    }

    /**
     * Конвертирует высоту звука в кол-во полутонов, на которые он сдвинут от START_NOTE_HZ
     */
    private fun Double.semitoneShift(): Int {
        val semitones = round(log(this / START_NOTE_HZ, 2.0) * OCTAVE_SEMITONES)
        if (semitones < 0)
            throw IllegalStateException("Pitch lower than the note from which the countdown is based")
        return semitones.toInt()
    }

    /**
     * Переводит кол-во полутонов в Float ( 1 = тон, 0.5 = полутон )
     */
    private fun Float.format(): Float = if (this > this.toInt()) this.toInt() + 0.5f else this

    /**
     * Обычное округление
     */
    private fun Float.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}