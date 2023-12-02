package com.musiclib.notes

import com.musiclib.notes.interfaces.Melody
import com.musiclib.notes.interfaces.MusicPause

class Melody(override val notes: List<MusicPause>, override val temp: Int = 2000) : Melody {

}