import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val BOT_API = "https://api.telegram.org/bot"

class TelegramBotService() {

    val httpClient: HttpClient = HttpClient.newBuilder().build()

    fun getUpdates(botToken: String, updateId: Int): String {
        val urlGetUpdates = "$BOT_API$botToken/getUpdates?offset=$updateId"
        val request = HttpRequest.newBuilder().uri(URI(urlGetUpdates)).build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(botToken: String, chatId: Long, text: String) {
        val formattedText = URLEncoder.encode(text, "UTF-8")
        val urlSendMessage = "$BOT_API$botToken/sendMessage?chat_id=$chatId&text=$formattedText"
        val request = HttpRequest.newBuilder().uri(URI(urlSendMessage)).build()
        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }
}