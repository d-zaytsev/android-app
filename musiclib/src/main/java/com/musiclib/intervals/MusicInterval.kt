package com.musiclib.intervals

interface MusicInterval : Comparable<MusicInterval> {
    val name: IntervalName
    val type: IntervalType
}