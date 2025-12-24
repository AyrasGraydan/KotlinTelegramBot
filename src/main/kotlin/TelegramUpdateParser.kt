val regexUpdateId = "\"update_id\":(\\d+)".toRegex()
val regexUserMessage = "\"text\":\"(.*?)\"".toRegex()
val regexChatId = "\"chat\":\\{\"id\":(\\d+)".toRegex()


class Parser {

    fun getLastInt(updates: String, regex: Regex): Int? {
        val match = regex.findAll(updates)
        return match.lastOrNull()?.let { it.groupValues.getOrNull(1)?.toInt()?.plus(1) }
    }

    fun getFirstLong(updates: String, regex: Regex): Long? {
        val match = regex.find(updates)
        return match?.groupValues?.get(1)?.toLong()
    }

    fun getFirstString(updates: String, regex: Regex): String? {
        val match = regex.find(updates)
        return match?.groupValues?.get(1)
    }
}