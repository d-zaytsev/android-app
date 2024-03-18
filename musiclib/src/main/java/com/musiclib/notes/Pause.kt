package com.musiclib.notes

import com.musiclib.notes.note_metadata.NoteDuration
import com.musiclib.notes.interfaces.MusicPause

/**
 * Музыкальная пауза
 */
class Pause(override val duration: NoteDuration) : MusicPause {
}