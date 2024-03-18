package com.musiclib.notes.interfaces

import com.musiclib.notes.note_metadata.NoteDuration

/** Представляет собой музыкальную паузу */
interface MusicPause {

    val duration: NoteDuration;
}