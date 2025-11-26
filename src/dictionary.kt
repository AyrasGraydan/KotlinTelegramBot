import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText("hello|привет|0\ndog|собака|0\ncat|кошка")

    val dictionary = loadDictionary(wordsFile)

    while(true) {
        println("Меню:\n1 – Учить слова\n2 – Статистика\n0 – Выход")
        print("Ваш выбор: ")
        when(readln().toIntOrNull()){
            1 -> println("Выбор пункта: Учить слова")
            2 -> println("Выбор пункта: Статистика")
            0 -> break
            else -> println("Такого вырианта нет")
        }
        println()
    }
}

fun loadDictionary(file: File): MutableList<Word>{
    val dictionary = mutableListOf<Word>()
    file.readLines().forEach {
        val line = it.split("|")
        dictionary.add(
            Word(
                line[0],
                line[1],
                line.getOrNull(2)?.toInt() ?: 0
            )
        )
    }
    return dictionary
}