package com.app.music_app.names

import android.content.Context
import com.example.android_app.R
import com.musiclib.intervals.Interval
import com.musiclib.notes.Note
import com.musiclib.notes.note_metadata.NoteName

class NoteNameResolver {
    companion object {
        fun nameOf(context: Context, note: NoteName): String {
            return when (note) {
                NoteName.Do -> context.getString(R.string.note_name_do)
                NoteName.Re -> context.getString(R.string.note_name_re)
                NoteName.Mi -> context.getString(R.string.note_name_mi)
                NoteName.Fa -> context.getString(R.string.note_name_fa)
                NoteName.Sol -> context.getString(R.string.note_name_sol)
                NoteName.La -> context.getString(R.string.note_name_la)
                NoteName.Si -> context.getString(R.string.note_name_si)

                else -> throw IllegalStateException("Can't recognize note $note")
            }

        }
    }
}