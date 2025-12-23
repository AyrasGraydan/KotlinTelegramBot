import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TelegramBotService() {

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(URI(urlGetUpdates)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(botToken: String, chatId: Long, text: String) {
        val urlSendMessage = "https://api.telegram.org/bot$botToken/sendMessage?chat_id=$chatId&text=$text"
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder().uri(URI(urlSendMessage)).build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun getUserMessage(updates: String): String? {
        val matchText: MatchResult? = "\"text\":\"(.*?)\"".toRegex().find(updates)
        return matchText?.groupValues?.get(1)
    }

    fun getLastUpdateId(updates: String): Int? {
        val matchUpdate = "\"update_id\":(\\d+)".toRegex().findAll(updates)
        return matchUpdate.lastOrNull()?.let { it.groupValues.getOrNull(1)?.toInt()?.plus(1) }
    }

    fun getChatId(updates: String): Long? {
        val matchChatId: MatchResult? = "\"chat\":\\{\"id\":(\\d+)".toRegex().find(updates)
        return matchChatId?.groupValues?.get(1)?.toLong()
    }
}