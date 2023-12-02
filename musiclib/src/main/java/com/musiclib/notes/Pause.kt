package com.musiclib.notes

import com.musiclib.notes.data.NoteDuration
import com.musiclib.notes.interfaces.MusicPause

class Pause(override val duration: NoteDuration) : MusicPause {
}