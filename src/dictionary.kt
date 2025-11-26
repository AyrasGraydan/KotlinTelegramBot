import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText("hello|привет|0\ndog|собака|0\ncat|кошка")

    val dictionary = mutableListOf<Word>()

    wordsFile.readLines().forEach {
        val line = it.split("|")
        dictionary.add(
            Word(
                line[0],
                line[1],
                line.getOrNull(2)?.toInt() ?: 0
            )
        )
    }

    dictionary.forEach { println(it) }
}