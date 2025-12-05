import java.io.File

const val VALUE_OF_WORD_LEARNED = 3
const val MAX_QUESTION_WORDS_SIZE = 4

const val ORANGE = "\u001b[33m"
const val BLUE = "\u001b[36m"
const val GRAY = "\u001b[37m"
const val RESET_COLOR = "\u001b[0m"

fun main() {
    val wordsFile = File("words.txt")

    if (!wordsFile.exists()) {
        wordsFile.createNewFile()
        fillFile(wordsFile)
    }

    val dictionary = loadDictionary(wordsFile)

    while (true) {
        println("Меню:\n1 – Учить слова\n2 – Статистика\n0 – Выход")
        print("Ваш выбор: ")
        when (readln().toIntOrNull()) {
            1 -> {
                println()
                println("${GRAY}Введите 0 для выхода ")

                val notLearnedList = mutableListOf<Word>()
                dictionary.forEach {
                    if (it.correctAnswersCount < VALUE_OF_WORD_LEARNED)
                        notLearnedList.add(it)
                }

                if (notLearnedList.isEmpty()) {
                    println("Все слова выучены!")
                } else {
                    while (true) {
                        val questionWords =
                            notLearnedList.shuffled().take(MAX_QUESTION_WORDS_SIZE)
                        val correctAnswer = questionWords.random()

                        println()
                        println("$BLUE${correctAnswer.original}")
                        questionWords.forEachIndexed { index, word ->
                            println("$ORANGE${index + 1}$RESET_COLOR - ${word.translate}")
                        }
                        print("Ваш выбор: ")
                        if (readln().toIntOrNull() == 0)
                            break
                    }
                }
            }

            2 -> {
                val totalCount = dictionary.size
                val learnedCount = dictionary
                    .count { it.correctAnswersCount >= VALUE_OF_WORD_LEARNED }
                val percent = if (totalCount != 0) (learnedCount * 100) / totalCount else 0
                println("Выучено $learnedCount из $totalCount слов | $percent%")
            }

            0 -> break
            else -> println("Такого варианта нет")
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

fun fillFile(file: File) {
    file.writeText(
        "hello|привет|1\ndog|собака|0\ncat|кошка" +
                "\nowl|сова|2\nsnake|змея\nrain|дождь|6"
    )
}