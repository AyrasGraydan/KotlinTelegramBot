const val VALUE_OF_WORD_LEARNED = 3
const val MAX_QUESTION_WORDS_SIZE = 4

const val RED = "\u001b[31m"
const val GREEN = "\u001b[32m"
const val ORANGE = "\u001b[33m"
const val BLUE = "\u001b[36m"
const val GRAY = "\u001b[37m"
const val RESET_COLOR = "\u001b[0m"

fun statisticsToString(value: Statistics) = value.run { "Выучено $learnedCount из $totalCount слов | $percent%" }

fun questionToString(value: Question) = value.variants.mapIndexed { index, word ->
    " $ORANGE${index + 1}$RESET_COLOR - ${word.translate}"
}.joinToString(separator = "\n") + "\n----------\n" + " ${GRAY}0 - Выход$RESET_COLOR"

fun main() {

    val trainer = LearnWordsTrainer()

    while (true) {
        println("Меню:\n1 – Учить слова\n2 – Статистика\n0 – Выход")
        print("Ваш выбор: ")
        when (readln().toIntOrNull()) {
            1 -> {
                while (true) {
                    val question = trainer.getQuestion()
                    if (question == null) {
                        println("Вы выучили все слова в базе")
                        break
                    }

                    println()
                    println("$BLUE${question.correctAnswer.original}")
                    println(questionToString(question))
                    print("Ваш выбор: ")

                    var userAnswerInput = readln().toIntOrNull() ?: -1
                    while (userAnswerInput !in 0..question.variants.size) {
                        print("Такого варианта нет: ")
                        userAnswerInput = readln().toIntOrNull() ?: -1
                    }

                    if (userAnswerInput == 0) break

                    if (trainer.checkAnswer(userAnswerInput - 1)) {
                        println("${GREEN}Правильно!$RESET_COLOR")
                    } else {
                        println(
                            "${RED}Неправильно!$RESET_COLOR " +
                                    "${question.correctAnswer.original} - это \"${question.correctAnswer.translate}\""
                        )
                    }
                }
            }

            2 -> {
                val statistics = trainer.getStatistics()
                println(statisticsToString(statistics))
            }

            0 -> break
            else -> println("Такого варианта нет")
        }
        println()
    }
}