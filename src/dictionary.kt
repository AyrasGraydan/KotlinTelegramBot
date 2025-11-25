import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText("hello привет\ndog собака\ncat кошка")

    wordsFile.readLines().forEach { println(it) }
}