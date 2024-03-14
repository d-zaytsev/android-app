package com.musiclib.intervals

import com.musiclib.notes.Note
import kotlin.math.abs

class Interval(name: IntervalName = IntervalName.Prima, type: IntervalType = IntervalType.Pure) :
    MusicInterval {
    override var name: IntervalName
        private set
    override var type: IntervalType
        private set
    override var distance: Float
        private set

    /**
     * Таблица интервалов
     */
    companion object {
        private val distanceArray =
            arrayOf(0f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f, 5.5f, 6f)

        private val pairsArray = arrayOf(
            Pair(IntervalName.Prima, IntervalType.Pure),
            Pair(IntervalName.Secunda, IntervalType.Small),
            Pair(IntervalName.Secunda, IntervalType.Large),
            Pair(IntervalName.Tertia, IntervalType.Small),
            Pair(IntervalName.Tertia, IntervalType.Large),
            Pair(IntervalName.Quarta, IntervalType.Pure),
            Pair(IntervalName.Quarta, IntervalType.Extended),
            Pair(IntervalName.Quinta, IntervalType.Pure),
            Pair(IntervalName.Sexta, IntervalType.Small),
            Pair(IntervalName.Sexta, IntervalType.Large),
            Pair(IntervalName.Septima, IntervalType.Small),
            Pair(IntervalName.Septima, IntervalType.Large),
            Pair(IntervalName.Octava, IntervalType.Pure)
        )
    }

    /**
     * Создание интервала по его типу
     */
    init {
        require(pairsArray.contains(Pair(name, type))) { "Such music interval doesn't exist" }

        this.name = name
        this.type = type
        this.distance = distanceArray[pairsArray.indexOf(Pair(name, type))]
    }

    /**
     * Создание интервала через измерение удалённости двух нот друг от друга
     */
    constructor(note1: Note, note2: Note) : this() {
        // Дистанция между нотами
        val octaveDistance = 6
        val diff =
            abs((note1.name.value + note1.sign.value + note1.octave * octaveDistance) - (note2.name.value + note2.sign.value + note2.octave * octaveDistance))

        require(distanceArray.contains(diff)) { "Music interval with such distance doesn't exist" }

        val tablePair = pairsArray[distanceArray.indexOf(diff)]

        // Определяем текущий интервал с помощью данных из таблицы
        this.name = tablePair.first
        this.type = tablePair.second
        this.distance = diff

    }

    override fun compareTo(other: MusicInterval): Int =
        (name.stepsCount + type.tone).compareTo(other.name.stepsCount + other.type.tone)

    override fun toString(): String = "$type $name"
}