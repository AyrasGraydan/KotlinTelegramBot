import java.io.File

const val VALUE_OF_WORD_LEARNED = 3
const val MAX_QUESTION_WORDS_SIZE = 4

const val RED = "\u001b[31m"
const val GREEN = "\u001b[32m"
const val ORANGE = "\u001b[33m"
const val BLUE = "\u001b[36m"
const val GRAY = "\u001b[37m"
const val RESET_COLOR = "\u001b[0m"

val wordsFile = File("words.txt")

fun main() {

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
                while (true) {
                    val notLearnedList = dictionary.filter { it.correctAnswersCount < VALUE_OF_WORD_LEARNED }

                    if (notLearnedList.isEmpty()) {
                        println("Вы выучили все слова в базе")
                        break
                    }

                    var questionWords = notLearnedList.shuffled().take(MAX_QUESTION_WORDS_SIZE).toMutableList()
                    val correctAnswer = questionWords.random()

                    if (questionWords.size != MAX_QUESTION_WORDS_SIZE) {
                        questionWords.addAll(dictionary.filter { it.correctAnswersCount >= VALUE_OF_WORD_LEARNED }
                            .shuffled().take(MAX_QUESTION_WORDS_SIZE - questionWords.size).toMutableList())
                        questionWords = questionWords.shuffled() as MutableList
                    }

                    println()
                    println("$BLUE${correctAnswer.original}")
                    questionWords.forEachIndexed { index, word ->
                        println(" $ORANGE${index + 1}$RESET_COLOR - ${word.translate}")
                    }
                    println("----------")
                    println(" ${GRAY}0 - Выход$RESET_COLOR")
                    print("Ваш выбор: ")

                    var userAnswerInput = readln().toIntOrNull() ?: -1

                    while (userAnswerInput !in 0..questionWords.size){
                       print("Такого варианта нет: ")
                        userAnswerInput = readln().toIntOrNull() ?: -1
                    }

                    if (userAnswerInput == 0) break

                    if (correctAnswer == questionWords[userAnswerInput - 1]
                    ) {
                        correctAnswer.correctAnswersCount++
                        println("${GREEN}Правильно!$RESET_COLOR")
                    } else println(
                        "${RED}Неправильно!$RESET_COLOR ${correctAnswer.original} - это \"${correctAnswer.translate}\""
                    )
                }
                wordsFile.saveDictionary(dictionary)
            }

            2 -> {
                val totalCount = dictionary.size
                val learnedCount = dictionary.count { it.correctAnswersCount >= VALUE_OF_WORD_LEARNED }
                val percent = if (totalCount != 0) (learnedCount * 100) / totalCount else 0
                println("Выучено $learnedCount из $totalCount слов | $percent%")
            }

            0 -> break
            else -> println("Такого варианта нет")
        }
        println()
    }

    println(wordsFile.readText())

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

fun File.saveDictionary(dictionary: List<Word>) {
    this.writeText(
        dictionary.joinToString(separator = "\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
}

fun fillFile(file: File) {
    file.writeText(
        "hello|привет\ndog|собака|0\ncat|кошка\nowl|сова|1\nsnake|змея\nrain|дождь"
    )
}