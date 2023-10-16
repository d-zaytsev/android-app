package com.musiclib.melody

import com.musiclib.notes.Note

data class Melody(override val notes: List<Note>, override val temp: Int) : SoundMelody;