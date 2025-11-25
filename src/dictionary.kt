import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText("hello|привет|0\ndog|собака|0\ncat|кошка")

    val dictionary = mutableListOf<Word>()

    wordsFile.readLines().forEach {
        val lines = it.split("|")
        dictionary.add(
            Word(
                lines[0],
                lines[1],
                lines.getOrNull(2)?.toInt() ?: 0
            )
        )
    }

    dictionary.forEach { println(it) }
}