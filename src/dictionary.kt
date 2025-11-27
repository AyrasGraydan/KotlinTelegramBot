import java.io.File

const val VALUE_OF_WORD_LEARNED = 3

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText(
        "hello|привет|6\ndog|собака|0\ncat|кошка" +
                "\nowl|сова|4\nsnake|змея|7\nrain|дождь|6"
    )

    val dictionary = loadDictionary(wordsFile)

    while (true) {
        println("Меню:\n1 – Учить слова\n2 – Статистика\n0 – Выход")
        print("Ваш выбор: ")
        when (readln().toIntOrNull()) {
            1 -> println("Выбор пункта: Учить слова")
            2 -> {
                val totalCount = dictionary.size
                val learnedCount = dictionary
                    .count { it.correctAnswersCount >= VALUE_OF_WORD_LEARNED }
                val percent = 100 * learnedCount / totalCount
                println("Выучено $learnedCount из $totalCount слов | $percent%")
            }

            0 -> break
            else -> println("Такого вырианта нет")
        }
        println()
    }
}

fun loadDictionary(file: File): MutableList<Word> {
    val dictionary = mutableListOf<Word>()
    file.readLines().forEach {
        val line = it.split("|")
        dictionary.add(
            Word(
                line[0], line[1], line.getOrNull(2)?.toInt() ?: 0
            )
        )
    }
    return dictionary
}