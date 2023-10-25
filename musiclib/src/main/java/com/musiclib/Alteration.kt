package com.musiclib

enum class Alteration(val value: Float) {
    NaturalSign(0f),    // Бекар
    FlatSign(-0.5f),    // Бемоль
    SharpSign(0.5f),    // Диез

    //    DoubleSharp(1f),       // Дубль-Диез
//    DoubleFlat(-1f),       // Дубль-Бемоль
    None(0f)
}