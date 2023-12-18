package com.musiclib.intervals

interface MusicInterval : Comparable<MusicInterval> {
    /** Название интервала */
    val name: IntervalName
    /** Тип интервала */
    val type: IntervalType
    /** Расстояние, содержащееся в интервале */
    val distance: Float
}