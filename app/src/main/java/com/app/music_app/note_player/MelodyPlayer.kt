package com.app.music_app.note_player

import android.content.Context
import android.media.MediaPlayer
import com.app.music_app.note_player.instruments.AbstractInstrument
import com.musiclib.notes.Note
import com.musiclib.notes.NoteDuration
import com.musiclib.notes.NoteVolume
import com.musiclib.notes.SoundNote
import com.musiclib.notes.interfaces.MelodyNote

class MelodyPlayer(override val instrument: AbstractInstrument) : NotePlayer {
    override fun soundOf(context: Context, note: Note): MediaPlayer {
        val mp = MediaPlayer.create(context, instrument.resourceIdByNote(context, note))
        mp.setVolume(1f, 1f)
        return mp
    }

    override fun soundOf(context: Context, note: SoundNote): MediaPlayer {
        val mp = MediaPlayer.create(context, instrument.resourceIdByNote(context, note.toSimpleNote()))
        val volume = mapOf(
            NoteVolume.Fortissimo to 1f,
            NoteVolume.Forte to 0.8f,
            NoteVolume.Piano to 0.5f,
            NoteVolume.Pianissimo to 0.4f
        )[note.volume] ?: throw NullPointerException("MelodyPlayer can't find settings for such NoteVolume")
        mp.setVolume(volume, volume)

        return mp
    }
}