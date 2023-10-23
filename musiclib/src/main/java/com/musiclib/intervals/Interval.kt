package com.musiclib.intervals

import com.musiclib.notes.Note
import kotlin.math.abs

class Interval(note1: Note, note2: Note) : MusicInterval {

    override val name: IntervalName;
    override val type: IntervalType;

    init {
        // Перенёс таблицу интервалов в map
        val map = mapOf(
            0f to Pair(IntervalName.Prima, IntervalType.Pure),
            0.5f to Pair(IntervalName.Secunda, IntervalType.Small),
            1f to Pair(IntervalName.Secunda, IntervalType.Large),
            1.5f to Pair(IntervalName.Tertia, IntervalType.Small),
            2f to Pair(IntervalName.Tertia, IntervalType.Large),
            2.5f to Pair(IntervalName.Quarta, IntervalType.Pure),
            3f to Pair(IntervalName.Quarta, IntervalType.Extended), // = Small Quinta
            3.5f to Pair(IntervalName.Quinta, IntervalType.Pure),
            4f to Pair(IntervalName.Sexta, IntervalType.Small),
            4.5f to Pair(IntervalName.Sexta, IntervalType.Large),
            5f to Pair(IntervalName.Septima, IntervalType.Small),
            5.5f to Pair(IntervalName.Septima, IntervalType.Large),
            6f to Pair(IntervalName.Octava, IntervalType.Pure)
        )
        // Разница между нотами
        val diff =
            abs((note1.name.dist + note1.sign.value + note1.octave * 6) - (note2.name.dist + note2.sign.value + note2.octave * 6))
        val ans = map[diff]

        // Определяем текущий интервал с помощью данных из таблицы
        this.name = ans?.first
            ?: throw NullPointerException("Не удалось определить интервал по переданным нотам")
        this.type = ans.second
    }

    override fun compareTo(other: MusicInterval): Int =
        (name.stepsCount + type.tone).compareTo(other.name.stepsCount + other.type.tone);

    override fun toString(): String = "${type.toString()} ${name.toString()}"
}