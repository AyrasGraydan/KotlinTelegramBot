import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val botApi = "https://api.telegram.org/bot"

class TelegramBotService() {

    val httpClient: HttpClient = HttpClient.newBuilder().build()

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "$botApi$botToken/getUpdates?offset=$updateId"
        val request = HttpRequest.newBuilder().uri(URI(urlGetUpdates)).build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(botToken: String, chatId: Long, text: String) {
        val formattedText = URLEncoder.encode(text, "UTF-8")
        val urlSendMessage = "$botApi$botToken/sendMessage?chat_id=$chatId&text=$formattedText"
        val request = HttpRequest.newBuilder().uri(URI(urlSendMessage)).build()
        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
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