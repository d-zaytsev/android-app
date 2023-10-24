package instruments

import com.musiclib.notes.Note

/**
 * Представляет собой класс, связывающий ноты (класс Note) и их физическое представление на устройстве (файл)
 */
abstract class AbstractInstrument {
    /**
     * @param note Нота, для которой нужно извлечь звук
     * @return Путь к файлу с нужным звуком
     * @throws IllegalArgumentException
     * */
    abstract fun soundPath(note: Note): String
    protected fun rangeCheck(note: Note) = instrumentRange.inRange(note)

    abstract val instrumentRange: InstrumentRange;

}