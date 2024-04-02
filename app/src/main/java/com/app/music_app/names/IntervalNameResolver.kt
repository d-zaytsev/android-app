package com.app.music_app.names

import android.content.Context
import com.example.android_app.R
import com.musiclib.intervals.Interval
import com.musiclib.intervals.IntervalName
import com.musiclib.intervals.IntervalType

class IntervalNameResolver {
    companion object {
        fun nameOf(context: Context, interval: Interval): String {
            return when (interval) {
                Interval(IntervalName.Prima, IntervalType.Pure) -> context.getString(R.string.pure_prima)
                Interval(IntervalName.Secunda, IntervalType.Small) -> context.getString(R.string.small_secunda)
                Interval(IntervalName.Secunda, IntervalType.Large) -> context.getString(R.string.large_secunda)
                Interval(IntervalName.Tertia, IntervalType.Small) -> context.getString(R.string.small_tertia)
                Interval(IntervalName.Tertia, IntervalType.Large) -> context.getString(R.string.large_tertia)
                Interval(IntervalName.Quarta, IntervalType.Pure) -> context.getString(R.string.pure_quarta)
                Interval(IntervalName.Quarta, IntervalType.Extended) -> context.getString(R.string.extended_quarta)
                Interval(IntervalName.Quinta, IntervalType.Pure) -> context.getString(R.string.pure_quinta)
                Interval(IntervalName.Sexta, IntervalType.Small) -> context.getString(R.string.small_sexta)
                Interval(IntervalName.Sexta, IntervalType.Large) -> context.getString(R.string.large_sexta)
                Interval(IntervalName.Septima, IntervalType.Small) -> context.getString(R.string.small_septima)
                Interval(IntervalName.Septima, IntervalType.Large) -> context.getString(R.string.large_septima)
                Interval(IntervalName.Octava, IntervalType.Pure) -> context.getString(R.string.pure_octava)

                else -> throw IllegalStateException("Can't find name for $interval")
            }
        }

        fun shortNameOf(context: Context, interval: Interval): String {
            return when (interval) {
                Interval(IntervalName.Prima, IntervalType.Pure) -> context.getString(R.string.pure_prima_short)
                Interval(IntervalName.Secunda, IntervalType.Small) -> context.getString(R.string.small_secunda_short)
                Interval(IntervalName.Secunda, IntervalType.Large) -> context.getString(R.string.large_secunda_short)
                Interval(IntervalName.Tertia, IntervalType.Small) -> context.getString(R.string.small_tertia_short)
                Interval(IntervalName.Tertia, IntervalType.Large) -> context.getString(R.string.large_tertia_short)
                Interval(IntervalName.Quarta, IntervalType.Pure) -> context.getString(R.string.pure_quarta_short)
                Interval(IntervalName.Quarta, IntervalType.Extended) -> context.getString(R.string.extended_quarta)
                Interval(IntervalName.Quinta, IntervalType.Pure) -> context.getString(R.string.pure_quinta_short)
                Interval(IntervalName.Sexta, IntervalType.Small) -> context.getString(R.string.small_sexta_short)
                Interval(IntervalName.Sexta, IntervalType.Large) -> context.getString(R.string.large_sexta_short)
                Interval(IntervalName.Septima, IntervalType.Small) -> context.getString(R.string.small_septima_short)
                Interval(IntervalName.Septima, IntervalType.Large) -> context.getString(R.string.large_septima_short)
                Interval(IntervalName.Octava, IntervalType.Pure) -> context.getString(R.string.pure_octave_short)

                else -> throw IllegalStateException("Can't find name for $interval")
            }
        }
    }
}