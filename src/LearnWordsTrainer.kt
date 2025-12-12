import java.io.File

data class Statistics(
    val totalCount: Int,
    val learnedCount: Int,
    val percent: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordsTrainer {

    val wordsFile: File by lazy {
       val file = File("words.txt")
        createFile(file)
        file
    }
    private val dictionary = loadDictionary()
    private var question: Question? = null

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount = dictionary.count { it.correctAnswersCount >= VALUE_OF_WORD_LEARNED }

        return Statistics(
            totalCount,
            learnedCount,
            if (totalCount != 0) learnedCount * 100 / totalCount else 0
        )
    }

    fun getQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < VALUE_OF_WORD_LEARNED }
        if (notLearnedList.isEmpty()) return null

        var questionWords = notLearnedList.shuffled().take(MAX_QUESTION_WORDS_SIZE).toMutableList()
        val correctAnswer = questionWords.random()

        if (questionWords.size != MAX_QUESTION_WORDS_SIZE) {
            dictionary.filterNot { it in (questionWords as Set<*>) }
                .shuffled().take(MAX_QUESTION_WORDS_SIZE - questionWords.size)
            questionWords = questionWords.shuffled() as MutableList
        }

        question = Question(questionWords, correctAnswer)
        return question
    }

    fun checkAnswer(userAnswerIndex: Int): Boolean {
        if (question == null) return false
        return question?.let {
            val correctAnswerIndex = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerIndex == userAnswerIndex) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                return true
            } else return false
        } ?: false
    }

    private fun createFile(file: File) {
        if (!file.exists()) {
            file.createNewFile()
            fillFile(file)
        }
    }

    private fun fillFile(file: File) {
        file.writeText(
            "hello|привет\ndog|собака\ncat|кошка\nowl|сова\nsnake|змея\nrain|дождь"
        )
    }

    private fun loadDictionary(): MutableList<Word> = wordsFile.readLines().map {
        val line = it.split("|")
        Word(line[0], line[1], line.getOrNull(2)?.toInt() ?: 0)
    } as MutableList

    private fun saveDictionary(dictionary: List<Word>) {
        wordsFile.writeText(
            dictionary.joinToString(separator = "\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
    }

}

