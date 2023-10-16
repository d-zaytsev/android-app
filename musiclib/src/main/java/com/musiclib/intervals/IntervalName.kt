package com.musiclib.intervals

/**
 * Определяет простые интервалы, различающиеся по ширине
 * @param stepsCount Ширина интервала, количество ступеней
 * */
enum class IntervalName(val stepsCount: Int) {
    Prima(1),
    Secunda(2),
    Tertia(3),
    Quarta(4),
    Quinta(5),
    Sexta(6),
    Septima(7),
    Octava(8)
}