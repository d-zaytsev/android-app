package com.musiclib.notes

enum class NoteDuration(val num: Float) {
    Whole(1f),         // целая
    Half(0.5f),        // половинная
    Quarter(0.25f),    // четвёртая
    Eight(0.125f),     // восьмая
    Sixteenth(0.0625f) // шестнадцатая
}