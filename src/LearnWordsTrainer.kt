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

class LearnWordsTrainer(
    private val valueOfWordLearned: Int = 3,
    private val maxQuestionWordsSize: Int = 4
) {

    val wordsFile: File by lazy {
        val file = File("words.txt")
        createFile(file)
        file
    }
    private val dictionary = loadDictionary()
    private var question: Question? = null

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount = dictionary.count { it.correctAnswersCount >= valueOfWordLearned }

        return Statistics(
            totalCount,
            learnedCount,
            if (totalCount != 0) learnedCount * 100 / totalCount else 100
        )
    }

    fun getQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswersCount < valueOfWordLearned }
        if (notLearnedList.isEmpty()) return null

        val questionWords = if (notLearnedList.size < maxQuestionWordsSize) {
            val learnedList = dictionary.filterNot { it in notLearnedList.toSet() }
            (notLearnedList + learnedList.shuffled().take(maxQuestionWordsSize - notLearnedList.size)).shuffled()
        } else {
            notLearnedList.shuffled().take(maxQuestionWordsSize)
        }

        val correctAnswer = questionWords.random()

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

    private fun loadDictionary(): MutableList<Word> = try {
        wordsFile.readLines().map {
            val line = it.split("|")
            Word(line[0], line[1], line.getOrNull(2)?.toInt() ?: 0)
        }.toMutableList()
    } catch (e: IndexOutOfBoundsException) {
        throw IllegalStateException("Incorrect words file")
    }

    private fun saveDictionary(dictionary: List<Word>) {
        wordsFile.writeText(
            dictionary.joinToString(separator = "\n") { "${it.original}|${it.translate}|${it.correctAnswersCount}" })
    }

}

